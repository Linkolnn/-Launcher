package com.customlauncher.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.customlauncher.app.R
import com.customlauncher.app.ui.MainActivity
import android.app.KeyguardManager
import com.customlauncher.app.LauncherApplication
import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.customlauncher.app.manager.HiddenModeStateManager

class TouchBlockService : Service() {
    
    private var blockView: View? = null
    private var windowManager: WindowManager? = null
    private var screenUnlockReceiver: BroadcastReceiver? = null
    private var shouldBlockAfterUnlock = false
    
    override fun onCreate() {
        super.onCreate()
        // Service should always be created when requested
        // The decision to block or not should be made in HiddenModeStateManager
        android.util.Log.d(TAG, "TouchBlockService created")
        
        // Immediately check if screen is locked on creation
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        android.util.Log.d(TAG, "Service created - Screen locked: ${keyguardManager?.isKeyguardLocked}")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            when (intent?.action) {
                ACTION_BLOCK_TOUCH -> {
                    // Always start touch blocking when requested
                    // HiddenModeStateManager handles the logic of when to call this
                    if (DEBUG) android.util.Log.d(TAG, "Starting touch blocking")
                    
                    // Check if screen is locked FIRST
                    val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
                    if (keyguardManager?.isKeyguardLocked == true) {
                        android.util.Log.d(TAG, "Screen is locked on service start - registering for unlock")
                        // Don't create overlay, just register for unlock
                        registerScreenUnlockReceiver()
                        shouldBlockAfterUnlock = true
                        // Keep service alive to wait for unlock
                        return START_NOT_STICKY
                    }
                    
                    blockTouch()
                    // Register receiver to handle screen lock/unlock events
                    registerScreenUnlockReceiver()
                }
                ACTION_UNBLOCK_TOUCH -> {
                    unblockTouch()
                    unregisterScreenUnlockReceiver()
                    shouldBlockAfterUnlock = false
                    // Stop service after unblocking
                    stopSelf()
                }
                else -> {
                    // Unknown action, stop service
                    stopSelf()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error in onStartCommand", e)
            stopSelf()
        }
        return START_NOT_STICKY // Don't restart if killed
    }
    
    // Removed startForegroundService() method - we're not using foreground service
    // to avoid showing any notification in the status bar
    
    private fun blockTouch() {
        try {
            // Service is only called when needed by HiddenModeStateManager
            if (blockView != null) {
                if (DEBUG) android.util.Log.d(TAG, "Block view already exists")
                return
            }
            
            // Lock screen check is already done in onStartCommand
            // If we reach here, it's safe to create the overlay
            
            // Check permission first
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!android.provider.Settings.canDrawOverlays(this)) {
                    android.util.Log.e(TAG, "No overlay permission")
                    return
                }
            }
            
            if (DEBUG) android.util.Log.d(TAG, "Starting touch blocking in hidden mode...")
            
            // Get preferences to check if screenshot blocking is enabled
            val preferences = LauncherApplication.instance.preferences
            val shouldBlockScreenshots = preferences.blockScreenshotsInHiddenMode
            
            // Get window manager safely
            windowManager = try {
                getSystemService(WINDOW_SERVICE) as WindowManager
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Failed to get WindowManager", e)
                return
            }
        
        // Use highest priority overlay type
        val layoutFlag = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 -> {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
            }
            else -> {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
        }
        
        // Flags to block all touch events completely on all screens
        var flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
        
        // Add FLAG_SECURE if screenshot blocking is enabled
        if (shouldBlockScreenshots) {
            flags = flags or WindowManager.LayoutParams.FLAG_SECURE
            android.util.Log.d(TAG, "Screenshot blocking enabled - adding FLAG_SECURE to overlay")
        }
        
        // Don't add FLAG_SHOW_WHEN_LOCKED or FLAG_DISMISS_KEYGUARD
        // We don't want to interfere with lock screen at all
        val finalFlags = flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        
        try {
            // Final safety check - NEVER create overlay on lock screen
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            if (keyguardManager?.isKeyguardLocked == true) {
                android.util.Log.e(TAG, "CRITICAL: Attempted to create overlay on lock screen - aborting!")
                return
            }
            
            // Create blocking view with proper touch interception and multitouch blocking
            blockView = object : FrameLayout(this@TouchBlockService) {
                private var activeTouchId = -1
                
                override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
                    ev?.let { event ->
                        // Block multitouch - only track first finger
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                // First finger down - remember its ID
                                activeTouchId = event.getPointerId(0)
                                android.util.Log.d("TouchBlockService", "First touch down, ID: $activeTouchId")
                            }
                            MotionEvent.ACTION_POINTER_DOWN -> {
                                // Additional finger down - block it completely
                                android.util.Log.d("TouchBlockService", "MULTITOUCH BLOCKED! Fingers: ${event.pointerCount}")
                                return true
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                // Reset when all fingers up
                                activeTouchId = -1
                                android.util.Log.d("TouchBlockService", "Touch ended")
                            }
                            MotionEvent.ACTION_POINTER_UP -> {
                                // One finger lifted but not all - keep blocking
                                android.util.Log.d("TouchBlockService", "Pointer up, still blocking multitouch")
                                return true
                            }
                            MotionEvent.ACTION_MOVE -> {
                                // Block move events if multiple fingers detected
                                if (event.pointerCount > 1) {
                                    android.util.Log.d("TouchBlockService", "Multitouch move blocked")
                                    return true
                                } else {
                                    // Single touch move is allowed but still blocked
                                }
                            }
                            else -> {
                                // Handle other actions
                            }
                        }
                    }
                    // Block all touch events
                    android.util.Log.d("TouchBlockService", "Touch blocked: ${ev?.actionMasked}, pointers: ${ev?.pointerCount}")
                    return true
                }
                
                override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
                    // Also intercept at this level for extra safety
                    return true
                }
                
                override fun onTouchEvent(event: MotionEvent?): Boolean {
                    // Final level of touch interception
                    android.util.Log.d("TouchBlockService", "Touch blocked at onTouchEvent: ${event?.action}")
                    return true
                }
            }.apply {
                // Completely transparent overlay - можно сделать слегка видимым для отладки
                // setBackgroundColor(android.graphics.Color.argb(10, 0, 0, 0))
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                
                // Set touch listener as additional safeguard with multitouch blocking
                setOnTouchListener { _, event ->
                    if (event.pointerCount > 1) {
                        // Multitouch blocked
                        return@setOnTouchListener true
                    }
                    // Touch blocked
                    true
                }
                
                // Don't make focusable since window has FLAG_NOT_FOCUSABLE
                // This allows keyboard events to pass through
                isFocusable = false
                isFocusableInTouchMode = false
                isClickable = true
                isLongClickable = true
            }
            
            // Create params for full screen overlay
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutFlag,
                // Добавляем FLAG_NOT_FOCUSABLE чтобы не блокировать клавиатуру
                // но оставляем FLAG_NOT_TOUCH_MODAL чтобы блокировать касания
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_FULLSCREEN or
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or
                finalFlags,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 0
                y = 0
                
                // Ensure we cover the entire screen including system bars
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
                
                // Покрываем весь экран включая статус бар и навигацию
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    fitInsetsTypes = 0 // Не учитываем insets, покрываем всё
                }
            }
            
            windowManager?.addView(blockView, params)
            if (DEBUG) android.util.Log.d(TAG, "Touch blocking overlay created and added - full screen coverage")
            
            // Don't create additional overlays for status and navigation bars
            // The main overlay should cover everything
            // This reduces memory usage and improves performance on weak devices
        } catch (e: Exception) {
            if (DEBUG) android.util.Log.e("TouchBlockService", "Failed to add overlay", e)
        }
        } catch (e: OutOfMemoryError) {
            if (DEBUG) android.util.Log.e(TAG, "Out of memory creating overlays", e)
            System.gc() // Force garbage collection
        } catch (e: Exception) {
            if (DEBUG) android.util.Log.e(TAG, "Unexpected error in blockTouch", e)
        }
    }
    
    // Removed additional overlay methods to reduce memory usage
    // The main overlay covers the entire screen including status bar and navigation
    
    private fun unblockTouch() {
        try {
            if (DEBUG) android.util.Log.d(TAG, "Removing touch blocking overlay...")
            
            // Remove overlay view immediately
            windowManager?.let { wm ->
                // Collect view if exists
                val viewToRemove = blockView
                
                // Clear reference immediately to prevent double removal
                blockView = null
                
                // Remove view if it exists
                viewToRemove?.let { view ->
                    try {
                        // Use removeViewImmediate for faster removal
                        wm.removeViewImmediate(view)
                    } catch (e: Exception) {
                        // Ignore - view might already be removed
                    }
                }
            }
            
            if (DEBUG) android.util.Log.d(TAG, "Touch blocking overlay removed")
        } catch (e: Exception) {
            if (DEBUG) android.util.Log.e(TAG, "Error in unblockTouch", e)
        }
    }
    
    private fun getNavigationBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
    
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
    
    private fun registerScreenUnlockReceiver() {
        if (screenUnlockReceiver != null) return  // Already registered
        
        screenUnlockReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_USER_PRESENT -> {
                        // Screen unlocked - now we can block touch if we're in hidden mode
                        android.util.Log.d(TAG, "Screen unlocked - checking if should block touch")
                        if (HiddenModeStateManager.currentState) {
                            android.util.Log.d(TAG, "Blocking touch after unlock in hidden mode")
                            blockTouch()
                        }
                        shouldBlockAfterUnlock = false
                    }
                    Intent.ACTION_SCREEN_OFF -> {
                        // Screen locked - IMMEDIATELY remove overlay to allow unlock
                        android.util.Log.d(TAG, "Screen locked - removing overlay immediately")
                        unblockTouch()  // Remove overlay right away
                        shouldBlockAfterUnlock = true  // Will block again after next unlock
                    }
                    Intent.ACTION_SCREEN_ON -> {
                        // Screen turned on but not unlocked yet - ensure no overlay
                        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
                        if (keyguardManager?.isKeyguardLocked == true) {
                            android.util.Log.d(TAG, "Screen on but locked - ensuring no overlay")
                            unblockTouch()
                            shouldBlockAfterUnlock = true
                        }
                    }
                }
            }
        }
        
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)  // Screen unlocked
            addAction(Intent.ACTION_SCREEN_OFF)    // Screen locked  
            addAction(Intent.ACTION_SCREEN_ON)     // Screen on but maybe still locked
        }
        
        try {
            registerReceiver(screenUnlockReceiver, filter)
            android.util.Log.d(TAG, "Screen unlock receiver registered")
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Failed to register screen unlock receiver", e)
        }
    }
    
    private fun unregisterScreenUnlockReceiver() {
        screenUnlockReceiver?.let { receiver ->
            try {
                unregisterReceiver(receiver)
                android.util.Log.d(TAG, "Screen unlock receiver unregistered")
            } catch (e: Exception) {
                // Receiver might not be registered
            }
            screenUnlockReceiver = null
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (DEBUG) android.util.Log.d(TAG, "TouchBlockService onDestroy called")
        // Remove all overlays when service is destroyed
        unblockTouch()
        // Unregister receiver if registered
        unregisterScreenUnlockReceiver()
        // Clear window manager reference
        windowManager = null
    }
    
    companion object {
        const val ACTION_BLOCK_TOUCH = "com.customlauncher.app.BLOCK_TOUCH"
        const val ACTION_UNBLOCK_TOUCH = "com.customlauncher.app.UNBLOCK_TOUCH"
        private const val NOTIFICATION_ID = 1001
        private const val TAG = "TouchBlockService"
        private const val DEBUG = true // Temporarily enabled for debugging lock screen issue
    }
}

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

class TouchBlockService : Service() {
    
    private var blockView: View? = null
    private var statusBarBlockView: View? = null
    private var navigationBarBlockView: View? = null
    private var lockScreenBlockView: View? = null
    private var windowManager: WindowManager? = null
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_BLOCK_TOUCH -> {
                startForegroundService()
                blockTouch()
            }
            ACTION_UNBLOCK_TOUCH -> {
                unblockTouch()
                stopForeground(true)
                stopSelf()
            }
        }
        return START_STICKY
    }
    
    private fun startForegroundService() {
        val channelId = "touch_block_channel"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Touch Block Service",
                NotificationManager.IMPORTANCE_HIGH  // High importance for lock screen
            ).apply {
                description = "Blocks touch input when apps are hidden"
                setShowBadge(false)
                setSound(null, null)
                enableVibration(false)
                setLockscreenVisibility(Notification.VISIBILITY_PUBLIC) // Show on lock screen
                setBypassDnd(true) // Bypass Do Not Disturb
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // High priority
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)  // Show on lock screen
            .setSilent(true)
            .setOngoing(true) // Keep it ongoing
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun blockTouch() {
        if (blockView != null) {
            android.util.Log.d("TouchBlockService", "Block view already exists")
            return
        }
        
        // Check permission first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                android.util.Log.e("TouchBlockService", "No overlay permission")
                return
            }
        }
        
        android.util.Log.d("TouchBlockService", "Starting touch blocking...")
        
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
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
        val flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
        
        // Add flags to show on lock screen and dismiss keyguard
        val finalFlags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            }
            else -> {
                @Suppress("DEPRECATION")
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            }
        }
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            finalFlags,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        
        // Set to cover entire screen including system UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        
        blockView = object : FrameLayout(this@TouchBlockService) {
            override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
                // Intercept and consume ALL touch events at dispatch level
                android.util.Log.d("TouchBlockService", "Touch intercepted at dispatch: ${ev?.action}")
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
            // Completely transparent overlay
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            
            // Make it intercept ALL touches
            isClickable = true
            isFocusable = false  // Don't take focus to allow key events
            isFocusableInTouchMode = false
            isLongClickable = true
            isEnabled = true
            
            // Set system UI visibility to hide status bar and navigation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android 11+
                windowInsetsController?.hide(android.view.WindowInsets.Type.systemBars())
            } else {
                @Suppress("DEPRECATION")
                systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            }
            
            setOnTouchListener { _, event ->
                // Block ALL touch events
                android.util.Log.d("TouchBlockService", "Touch blocked: action=${event.action}, x=${event.x}, y=${event.y}")
                true // Always consume the touch event
            }
            
            // Also set a generic touch interceptor
            setOnGenericMotionListener { _, event ->
                android.util.Log.d("TouchBlockService", "Motion blocked: action=${event.action}")
                true
            }
        }
        
        try {
            windowManager?.addView(blockView, params)
            android.util.Log.d("TouchBlockService", "Full screen touch blocking overlay added")
            
            // Add additional overlays for status bar and navigation bar
            addStatusBarBlock()
            addNavigationBarBlock()
            
            // Add extra layer for lock screen if device is locked
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (keyguardManager.isDeviceLocked) {
                    addLockScreenBlock()
                }
            } else if (keyguardManager.isKeyguardLocked) {
                addLockScreenBlock()
            }
        } catch (e: Exception) {
            android.util.Log.e("TouchBlockService", "Failed to add overlay", e)
        }
    }
    
    private fun addStatusBarBlock() {
        if (statusBarBlockView != null) return
        
        val statusBarHeight = getStatusBarHeight()
        
        val layoutFlag = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
            else -> {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
            }
        }
        
        // Aggressive parameters for complete status bar blocking
        val flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        
        // Add lock screen flags
        val finalFlags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            }
            else -> {
                @Suppress("DEPRECATION")
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            }
        }
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            statusBarHeight + 200, // Extra height to catch all swipes
            layoutFlag,
            finalFlags,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = -100 // Even more aggressive offset to cover pull-down area
        
        // Set to cover display cutout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        
        statusBarBlockView = object : View(this@TouchBlockService) {
            override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
                android.util.Log.d("TouchBlockService", "Status bar touch intercepted: ${event?.action}")
                return true
            }
        }.apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            isClickable = true
            isFocusable = false
            setOnTouchListener { _, event ->
                // Consume all touch events
                android.util.Log.d("TouchBlockService", "Status bar touch blocked: ${event.action}")
                true
            }
        }
        
        try {
            windowManager?.addView(statusBarBlockView, params)
            android.util.Log.d("TouchBlockService", "Status bar block added")
        } catch (e: Exception) {
            android.util.Log.e("TouchBlockService", "Failed to add status bar block", e)
        }
    }
    
    private fun addNavigationBarBlock() {
        if (navigationBarBlockView != null) return
        
        val navBarHeight = getNavigationBarHeight()
        
        val layoutFlag = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
            else -> {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
            }
        }
        
        // Aggressive parameters for complete navigation bar blocking
        val flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        
        // Add lock screen flags
        val finalFlags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            }
            else -> {
                @Suppress("DEPRECATION")
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            }
        }
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            navBarHeight + 200, // Extra height to catch all swipes
            layoutFlag,
            finalFlags,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.BOTTOM or Gravity.START
        params.x = 0
        params.y = -100 // Even more aggressive offset to cover swipe-up area
        
        navigationBarBlockView = object : View(this@TouchBlockService) {
            override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
                android.util.Log.d("TouchBlockService", "Navigation bar touch intercepted: ${event?.action}")
                return true
            }
        }.apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            isClickable = true
            isFocusable = false
            setOnTouchListener { _, event ->
                // Consume all touch events
                android.util.Log.d("TouchBlockService", "Navigation bar touch blocked: ${event.action}")
                true
            }
        }
        
        try {
            windowManager?.addView(navigationBarBlockView, params)
            android.util.Log.d("TouchBlockService", "Navigation bar block added")
        } catch (e: Exception) {
            android.util.Log.e("TouchBlockService", "Failed to add navigation bar block", e)
        }
    }
    
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
    
    private fun addLockScreenBlock() {
        if (lockScreenBlockView != null) return
        
        android.util.Log.d("TouchBlockService", "Adding lock screen block layer")
        
        val layoutFlag = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
            else -> {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
            }
        }
        
        // Maximum priority flags for lock screen
        val flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        
        val finalFlags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            }
            else -> {
                @Suppress("DEPRECATION")
                flags or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            }
        }
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            finalFlags,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.CENTER
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        
        lockScreenBlockView = object : FrameLayout(this@TouchBlockService) {
            override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
                android.util.Log.d("TouchBlockService", "Lock screen touch intercepted: ${ev?.action}")
                return true
            }
            
            override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
                return true
            }
        }.apply {
            setBackgroundColor(0x05000000) // Very slight tint to show it's working
            isClickable = true
            isFocusable = false
            isLongClickable = true
            
            setOnTouchListener { _, event ->
                android.util.Log.d("TouchBlockService", "Lock screen touch blocked: ${event.action}")
                true
            }
        }
        
        try {
            windowManager?.addView(lockScreenBlockView, params)
            android.util.Log.d("TouchBlockService", "Lock screen block layer added")
        } catch (e: Exception) {
            android.util.Log.e("TouchBlockService", "Failed to add lock screen block", e)
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
    
    private fun unblockTouch() {
        try {
            blockView?.let {
                windowManager?.removeView(it)
                blockView = null
            }
            statusBarBlockView?.let {
                windowManager?.removeView(it)
                statusBarBlockView = null
            }
            navigationBarBlockView?.let {
                windowManager?.removeView(it)
                navigationBarBlockView = null
            }
            lockScreenBlockView?.let {
                windowManager?.removeView(it)
                lockScreenBlockView = null
            }
            android.util.Log.d("TouchBlockService", "Touch unblocked - all layers removed")
        } catch (e: Exception) {
            android.util.Log.e("TouchBlockService", "Error removing overlay", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        unblockTouch()
    }
    
    companion object {
        const val ACTION_BLOCK_TOUCH = "com.customlauncher.app.BLOCK_TOUCH"
        const val ACTION_UNBLOCK_TOUCH = "com.customlauncher.app.UNBLOCK_TOUCH"
        private const val NOTIFICATION_ID = 1001
        private const val TAG = "TouchBlockService"
    }
}

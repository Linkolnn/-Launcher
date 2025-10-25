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

class TouchBlockService : Service() {
    
    private var windowManager: WindowManager? = null
    private var blockView: View? = null
    private var statusBarBlockView: View? = null
    private var navigationBarBlockView: View? = null
    
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
                NotificationManager.IMPORTANCE_NONE  // Changed to NONE to hide notification
            ).apply {
                description = "Blocks touch input when apps are hidden"
                setShowBadge(false)
                setSound(null, null)
                enableVibration(false)
                setLockscreenVisibility(Notification.VISIBILITY_SECRET)
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
            .setPriority(NotificationCompat.PRIORITY_MIN)  // Minimum priority
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)  // Hide from lock screen
            .setSilent(true)
            .setOngoing(false)
            .setContentIntent(pendingIntent)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun blockTouch() {
        if (blockView != null) return
        
        // Check permission first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                android.util.Log.e("TouchBlockService", "No overlay permission")
                return
            }
        }
        
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        
        // Enhanced flags to block everything including system UI
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        } else {
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        }
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            flags,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        
        // Set to cover entire screen including system UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        
        blockView = FrameLayout(this).apply {
            // Transparent overlay - no darkening of screen
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            
            // Make it clickable to intercept all touches
            isClickable = true
            isFocusable = false
            
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
                // Block all touch events including swipes from edges
                when (event.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_MOVE,
                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_OUTSIDE -> {
                        android.util.Log.d("TouchBlockService", "Touch blocked: ${event.action} at ${event.x}, ${event.y}")
                        true
                    }
                    else -> true
                }
            }
        }
        
        try {
            windowManager?.addView(blockView, params)
            android.util.Log.d("TouchBlockService", "Full screen touch blocking overlay added")
            
            // Add additional overlays for status bar and navigation bar
            addStatusBarBlock()
            addNavigationBarBlock()
        } catch (e: Exception) {
            android.util.Log.e("TouchBlockService", "Failed to add overlay", e)
        }
    }
    
    private fun addStatusBarBlock() {
        if (statusBarBlockView != null) return
        
        val statusBarHeight = getStatusBarHeight()
        
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        }
        
        // More aggressive parameters for status bar blocking
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            statusBarHeight + 100, // Increased height to better catch swipes
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = -50 // More aggressive offset
        
        statusBarBlockView = View(this).apply {
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
        
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        }
        
        // More aggressive parameters for navigation bar blocking
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            navBarHeight + 100, // Increased height to better catch swipes
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.BOTTOM or Gravity.START
        params.x = 0
        params.y = -50 // More aggressive offset
        
        navigationBarBlockView = View(this).apply {
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
    
    private fun getNavigationBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
    
    private fun unblockTouch() {
        blockView?.let {
            windowManager?.removeView(it)
            blockView = null
        }
        
        statusBarBlockView?.let {
            try {
                windowManager?.removeView(it)
            } catch (e: Exception) {
                android.util.Log.e("TouchBlockService", "Error removing status bar block", e)
            }
            statusBarBlockView = null
        }
        
        navigationBarBlockView?.let {
            try {
                windowManager?.removeView(it)
            } catch (e: Exception) {
                android.util.Log.e("TouchBlockService", "Error removing navigation bar block", e)
            }
            navigationBarBlockView = null
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
    }
}

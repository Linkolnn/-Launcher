package com.customlauncher.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import com.customlauncher.app.LauncherApplication

class SystemBlockAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "SystemBlockService"
        var instance: SystemBlockAccessibilityService? = null
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        
        val info = AccessibilityServiceInfo().apply {
            // Listen to all event types
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            
            // Set flags to intercept gestures
            flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                    AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
            
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
        }
        
        serviceInfo = info
        Log.d(TAG, "Accessibility service connected")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val preferences = LauncherApplication.instance.preferences
        
        if (preferences.appsHidden && preferences.touchScreenBlocked) {
            // Block system UI interactions when in hidden mode
            when (event?.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    val packageName = event.packageName?.toString()
                    
                    // Block status bar and system UI
                    if (packageName == "com.android.systemui" || 
                        packageName == "com.android.keyguard") {
                        
                        // Go back to home
                        performGlobalAction(GLOBAL_ACTION_HOME)
                        Log.d(TAG, "Blocked system UI: $packageName")
                    }
                }
                
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    // Block notification shade
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    Log.d(TAG, "Blocked notification shade")
                }
            }
        }
    }
    
    override fun onKeyEvent(event: KeyEvent?): Boolean {
        val preferences = LauncherApplication.instance.preferences
        
        if (preferences.appsHidden && preferences.touchScreenBlocked) {
            // Allow only volume keys for unlocking
            when (event?.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP,
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    return super.onKeyEvent(event)
                }
                KeyEvent.KEYCODE_HOME,
                KeyEvent.KEYCODE_BACK,
                KeyEvent.KEYCODE_APP_SWITCH,
                KeyEvent.KEYCODE_MENU -> {
                    Log.d(TAG, "Blocked key: ${event.keyCode}")
                    return true // Consume the event
                }
            }
        }
        
        return super.onKeyEvent(event)
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.d(TAG, "Accessibility service destroyed")
    }
    
    fun blockSystemGestures(block: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (block) {
                // Disable gesture detection
                disableSelf()
                Thread.sleep(100)
                // Re-enable with blocking
                val intent = Intent(this, SystemBlockAccessibilityService::class.java)
                startService(intent)
            }
        }
    }
}

package com.customlauncher.app.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.service.TouchBlockService
import com.customlauncher.app.service.SensorControlService
import com.customlauncher.app.service.SystemBlockAccessibilityService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Centralized state manager for hidden mode
 * Ensures consistent state across all components
 */
object HiddenModeStateManager {
    private const val TAG = "HiddenModeStateManager"
    
    // Observable state flow for reactive updates
    private val _isHiddenMode = MutableStateFlow(false)
    val isHiddenMode: StateFlow<Boolean> = _isHiddenMode
    
    // Current state getter
    val currentState: Boolean
        get() = _isHiddenMode.value
    
    /**
     * Toggle hidden mode state
     * This is the single source of truth for state changes
     */
    fun toggleHiddenMode(context: Context) {
        val newState = !currentState
        setHiddenMode(context, newState)
    }
    
    /**
     * Set hidden mode to specific state
     */
    fun setHiddenMode(context: Context, enabled: Boolean) {
        Log.d(TAG, "Setting hidden mode: $enabled (was: $currentState)")
        
        // Force update even if state seems the same (for sync issues)
        
        // Update the state
        _isHiddenMode.value = enabled
        
        // Update preferences - force commit for immediate persistence
        val prefs = context.getSharedPreferences("launcher_preferences", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("apps_hidden", enabled).apply()
        
        // Handle touch blocking - prioritize overlay method as it works without root
        if (enabled) {
            // Close all apps and go to home screen first
            closeAllAppsAndGoHome(context)
            
            // Start overlay blocking immediately (works without root)
            startTouchBlockService(context)
            
            // Enable Do Not Disturb mode
            enableDoNotDisturb(context)
            
            // Also try system methods if available (requires root or special permissions)
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                disableTouchSensor(context)
            }, 100)
        } else {
            // Optimize exit from hidden mode - do operations asynchronously
            // to prevent UI freezing
            
            // First, update state immediately for responsiveness
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            
            // Stop touch blocking service immediately
            stopTouchBlockService(context)
            
            // Enable touch sensor and DND with small delays to prevent UI freeze
            handler.postDelayed({
                enableTouchSensor(context)
            }, 50)
            
            handler.postDelayed({
                disableDoNotDisturb(context)
            }, 100)
        }
        
        // Send broadcast about state change
        val intent = Intent("com.customlauncher.HIDDEN_MODE_CHANGED")
        intent.putExtra("hidden", enabled)
        context.sendBroadcast(intent)
        
        Log.d(TAG, "Hidden mode set to: $enabled, broadcast sent")
    }
    
    /**
     * Initialize state from preferences
     */
    fun initializeState() {
        val preferences = LauncherApplication.instance.preferences
        val savedState = preferences.appsHidden
        _isHiddenMode.value = savedState
        Log.d(TAG, "Initialized state from preferences: $savedState")
    }
    
    /**
     * Force refresh the current state
     */
    fun refreshState(context: Context) {
        val preferences = LauncherApplication.instance.preferences
        val currentPrefState = preferences.appsHidden
        
        if (currentPrefState != currentState) {
            Log.d(TAG, "State mismatch detected. Preferences: $currentPrefState, Manager: $currentState")
            setHiddenMode(context, currentPrefState)
        }
    }
    
    private fun closeAllAppsAndGoHome(context: Context) {
        try {
            Log.d(TAG, "Closing all apps and going to home screen")
            
            // Method 1: Use accessibility service first for immediate action
            val accessibilityIntent = Intent(context, SystemBlockAccessibilityService::class.java).apply {
                action = "com.customlauncher.CLOSE_ALL_APPS"
            }
            context.startService(accessibilityIntent)
            
            // Method 2: Broadcast to close system dialogs immediately
            val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            context.sendBroadcast(closeIntent)
            
            // Method 3: Send home intent as backup (with small delay)
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or 
                           Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                context.startActivity(homeIntent)
            }, 150)
            
            Log.d(TAG, "Home screen activated, apps closing")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing apps and going home", e)
        }
    }
    
    private fun startTouchBlockService(context: Context) {
        Log.d(TAG, "Starting touch block service")
        val intent = Intent(context, TouchBlockService::class.java)
        intent.action = TouchBlockService.ACTION_BLOCK_TOUCH
        
        try {
            // Use regular startService since we're not using foreground anymore
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start TouchBlockService", e)
        }
    }
    
    private fun stopTouchBlockService(context: Context) {
        Log.d(TAG, "Stopping touch block service")
        
        // First send unblock action
        val intent = Intent(context, TouchBlockService::class.java)
        intent.action = TouchBlockService.ACTION_UNBLOCK_TOUCH
        
        try {
            // Use regular startService since we're not using foreground anymore
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send unblock action", e)
        }
        
        // Stop service immediately after sending unblock action
        // The service will handle cleanup in its onDestroy
        try {
            context.stopService(Intent(context, TouchBlockService::class.java))
            Log.d(TAG, "TouchBlockService stopped")
        } catch (e: Exception) {
            // Service might already be stopped
        }
    }
    
    private fun disableTouchSensor(context: Context) {
        Log.d(TAG, "Disabling touch sensor")
        
        // First try AccessibilityService method
        val accessibilityIntent = Intent(context, SystemBlockAccessibilityService::class.java).apply {
            action = SystemBlockAccessibilityService.ACTION_BLOCK_TOUCHES
        }
        context.startService(accessibilityIntent)
        
        // Also use SensorControlService as backup
        val sensorIntent = Intent(context, SensorControlService::class.java).apply {
            action = SensorControlService.ACTION_DISABLE_SENSOR
        }
        context.startService(sensorIntent)
    }
    
    private fun enableTouchSensor(context: Context) {
        // Disable via AccessibilityService
        val accessibilityIntent = Intent(context, SystemBlockAccessibilityService::class.java).apply {
            action = SystemBlockAccessibilityService.ACTION_UNBLOCK_TOUCHES
        }
        context.startService(accessibilityIntent)
        
        // Also stop SensorControlService
        val sensorIntent = Intent(context, SensorControlService::class.java).apply {
            action = SensorControlService.ACTION_ENABLE_SENSOR
        }
        context.startService(sensorIntent)
    }
    
    private fun enableDoNotDisturb(context: Context) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            
            // Check if we have DND access
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (!notificationManager.isNotificationPolicyAccessGranted) {
                    Log.w(TAG, "No DND access permission")
                    // Request permission
                    val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                    return
                }
                
                // Enable DND - Priority only mode
                notificationManager.setInterruptionFilter(android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY)
                Log.d(TAG, "Do Not Disturb enabled")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to enable DND", e)
        }
    }
    
    private fun disableDoNotDisturb(context: Context) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (notificationManager.isNotificationPolicyAccessGranted) {
                    // Disable DND - All notifications
                    notificationManager.setInterruptionFilter(android.app.NotificationManager.INTERRUPTION_FILTER_ALL)
                    Log.d(TAG, "Do Not Disturb disabled")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to disable DND", e)
        }
    }
}

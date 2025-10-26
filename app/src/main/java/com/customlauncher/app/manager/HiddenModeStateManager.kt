package com.customlauncher.app.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.service.TouchBlockService
import com.customlauncher.app.service.SensorControlService
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
        val preferences = LauncherApplication.instance.preferences
        preferences.appsHidden = enabled
        preferences.touchScreenBlocked = enabled
        
        // Force sync to disk immediately
        val prefs = context.getSharedPreferences("launcher_preferences", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean("apps_hidden", enabled)
            putBoolean("touch_blocked", enabled)
            commit() // Use commit() for immediate sync
        }
        
        // Handle touch blocking - prioritize overlay method as it works without root
        if (enabled) {
            // Start overlay blocking immediately (works without root)
            startTouchBlockService(context)
            
            // Also try system methods if available (requires root or special permissions)
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                disableTouchSensor(context)
            }, 100)
        } else {
            // Stop all blocking methods
            stopTouchBlockService(context)
            enableTouchSensor(context)
        }
        
        // Send broadcast to update UI components
        val intent = Intent("com.customlauncher.HIDDEN_MODE_CHANGED")
        intent.putExtra("hidden", enabled)
        intent.setPackage(context.packageName) // Make it explicit
        context.sendBroadcast(intent)
        
        Log.d(TAG, "Hidden mode changed to: $enabled (forced update)")
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
    
    private fun startTouchBlockService(context: Context) {
        Log.d(TAG, "Starting touch block service")
        val intent = Intent(context, TouchBlockService::class.java)
        intent.action = TouchBlockService.ACTION_BLOCK_TOUCH
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            Log.d(TAG, "Touch block service start command sent")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start touch block service", e)
        }
    }
    
    private fun stopTouchBlockService(context: Context) {
        Log.d(TAG, "Stopping touch block service")
        val intent = Intent(context, TouchBlockService::class.java)
        intent.action = TouchBlockService.ACTION_UNBLOCK_TOUCH
        context.startService(intent)
    }
    
    private fun disableTouchSensor(context: Context) {
        Log.d(TAG, "Disabling touch sensor")
        val intent = Intent(context, SensorControlService::class.java)
        intent.action = SensorControlService.ACTION_DISABLE_SENSOR
        context.startService(intent)
    }
    
    private fun enableTouchSensor(context: Context) {
        Log.d(TAG, "Enabling touch sensor")
        val intent = Intent(context, SensorControlService::class.java)
        intent.action = SensorControlService.ACTION_ENABLE_SENSOR
        context.startService(intent)
    }
}

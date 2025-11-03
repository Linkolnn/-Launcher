package com.customlauncher.app.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import com.customlauncher.app.data.preferences.LauncherPreferences

/**
 * Manager for handling Home Screen modes (Touch/Button phone)
 */
class HomeScreenModeManager(private val context: Context) {
    
    companion object {
        private const val TAG = "HomeScreenModeManager"
        
        // Mode constants
        const val MODE_TOUCH = 0
        const val MODE_BUTTON = 1
        
        // Broadcast action for mode changes
        const val ACTION_MODE_CHANGED = "com.customlauncher.app.HOME_SCREEN_MODE_CHANGED"
        const val EXTRA_MODE = "mode"
        const val EXTRA_PREVIOUS_MODE = "previous_mode"
        
        private const val DEBUG = true // Enable for detailed logging
    }
    
    // Interface for mode change listeners
    interface ModeChangeListener {
        fun onModeChanged(newMode: Int, previousMode: Int)
    }
    
    private val preferences = LauncherPreferences(context)
    private val listeners = mutableListOf<ModeChangeListener>()
    
    /**
     * Get current home screen mode
     */
    fun getCurrentMode(): Int {
        return preferences.homeScreenMode
    }
    
    /**
     * Set home screen mode
     */
    fun setMode(mode: Int) {
        if (mode != MODE_TOUCH && mode != MODE_BUTTON) {
            Log.e(TAG, "Invalid mode: $mode")
            return
        }
        
        val currentMode = getCurrentMode()
        if (currentMode == mode) {
            Log.d(TAG, "Mode already set to: ${getModeString(mode)}")
            return
        }
        
        Log.d(TAG, "Changing mode from ${getModeString(currentMode)} to ${getModeString(mode)}")
        
        // Save to preferences
        preferences.homeScreenMode = mode
        
        // Notify listeners
        notifyModeChanged(mode, currentMode)
        
        // Send broadcast for system-wide notification
        sendModeBroadcast(mode, currentMode)
    }
    
    /**
     * Check if current mode is Touch mode
     */
    fun isTouchMode(): Boolean {
        return getCurrentMode() == MODE_TOUCH
    }
    
    /**
     * Check if current mode is Button mode
     */
    fun isButtonMode(): Boolean {
        return getCurrentMode() == MODE_BUTTON
    }
    
    /**
     * Toggle between modes
     */
    fun toggleMode() {
        val currentMode = getCurrentMode()
        val newMode = if (currentMode == MODE_TOUCH) MODE_BUTTON else MODE_TOUCH
        setMode(newMode)
    }
    
    /**
     * Add mode change listener
     */
    fun addModeChangeListener(listener: ModeChangeListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }
    
    /**
     * Remove mode change listener
     */
    fun removeModeChangeListener(listener: ModeChangeListener) {
        listeners.remove(listener)
    }
    
    /**
     * Clear all listeners (call in onDestroy)
     */
    fun clearListeners() {
        listeners.clear()
    }
    
    /**
     * Get string representation of mode
     */
    fun getModeString(mode: Int): String {
        return when (mode) {
            MODE_TOUCH -> "Touch Mode"
            MODE_BUTTON -> "Button Mode"
            else -> "Unknown Mode"
        }
    }
    
    /**
     * Get user-friendly mode name for UI
     */
    fun getModeName(mode: Int): String {
        return when (mode) {
            MODE_TOUCH -> "Для сенсорных смартфонов"
            MODE_BUTTON -> "Для кнопочных смартфонов"
            else -> "Неизвестный режим"
        }
    }
    
    /**
     * Notify all listeners about mode change
     */
    private fun notifyModeChanged(newMode: Int, previousMode: Int) {
        listeners.forEach { listener ->
            try {
                if (DEBUG) {
                    Log.d(TAG, "DEBUG: Notifying listener - Previous: $previousMode, New: $newMode")
                }
                listener.onModeChanged(newMode, previousMode)
            } catch (e: Exception) {
                Log.e(TAG, "Error notifying listener", e)
            }
        }
        if (DEBUG) {
            Log.d(TAG, "DEBUG: Total listeners: ${listeners.size}")
        }
    }
    
    /**
     * Send broadcast about mode change
     */
    private fun sendModeBroadcast(newMode: Int, previousMode: Int) {
        val intent = Intent(ACTION_MODE_CHANGED).apply {
            putExtra(EXTRA_MODE, newMode)
            putExtra(EXTRA_PREVIOUS_MODE, previousMode)
            setPackage(context.packageName) // Keep broadcast local to app
        }
        
        context.sendBroadcast(intent)
        Log.d(TAG, "Mode change broadcast sent: $previousMode -> $newMode")
        if (DEBUG) {
            Log.d(TAG, "DEBUG: Broadcast sent for mode change")
        }
    }
    
    /**
     * Check if the device supports button mode
     * (Can be used to hide button mode option on pure touch devices)
     */
    fun isButtonModeSupported(): Boolean {
        // For now, always return true. 
        // In future, can check for physical keyboard or other indicators
        return true
    }
}

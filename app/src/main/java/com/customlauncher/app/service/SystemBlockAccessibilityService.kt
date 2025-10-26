package com.customlauncher.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.content.Intent
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import com.customlauncher.app.LauncherApplication
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import com.customlauncher.app.data.model.KeyCombination
import com.customlauncher.app.receiver.KeyCombinationReceiver
import android.app.KeyguardManager
import android.content.ComponentName
import android.provider.Settings
import com.customlauncher.app.manager.HiddenModeStateManager

class SystemBlockAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "SystemBlockService"
        private const val ACTION_TOGGLE_APPS = "com.customlauncher.app.TOGGLE_APPS"
        var instance: SystemBlockAccessibilityService? = null
        private const val KEYGUARD_CHECK_DELAY = 50L
    }
    
    // For key combination detection
    private var volumeUpPressed = false
    private var volumeDownPressed = false
    private var powerPressed = false
    private val keyPressHandler = Handler(Looper.getMainLooper())
    private var longPressRunnable: Runnable? = null
    private var lastKeyEventTime = 0L
    private val KEY_EVENT_TIMEOUT = 100L // milliseconds
    private var isScreenLocked = false
    private lateinit var keyguardManager: KeyguardManager
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        
        // Initialize KeyguardManager
        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        
        val info = AccessibilityServiceInfo().apply {
            // Listen to key events primarily
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                        AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
            
            // Set flags to intercept key events
            flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
        }
        
        serviceInfo = info
        Log.d(TAG, "Accessibility service connected")
        
        // Check initial lock state
        checkScreenLockState()
        
        // Initialize state manager
        HiddenModeStateManager.initializeState()
        Log.d(TAG, "Initial state - Hidden mode: ${HiddenModeStateManager.currentState}")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val preferences = LauncherApplication.instance.preferences
        
        // Check for screen lock state changes
        when (event?.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                val packageName = event.packageName?.toString()
                
                // Detect keyguard (lock screen) state
                if (packageName == "com.android.keyguard" || 
                    packageName == "com.android.systemui.keyguard") {
                    checkScreenLockState()
                    Log.d(TAG, "Keyguard state changed, locked: $isScreenLocked")
                }
                
                // Only block system UI when apps are actually hidden
                if (HiddenModeStateManager.currentState) {
                    if (packageName == "com.android.systemui" && !isScreenLocked) {
                        // Only block system UI when not on lock screen and apps are hidden
                        performGlobalAction(GLOBAL_ACTION_HOME)
                        Log.d(TAG, "Blocked system UI: $packageName (hidden mode active)")
                    }
                }
            }
            
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                if (HiddenModeStateManager.currentState && !isScreenLocked) {
                    // Block notification shade only when not on lock screen
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    Log.d(TAG, "Blocked notification shade")
                }
            }
        }
    }
    
    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) return false
        
        val preferences = LauncherApplication.instance.preferences
        val currentTime = System.currentTimeMillis()
        
        // Update screen lock state
        checkScreenLockState()
        
        // Reset key states if timeout exceeded
        if (currentTime - lastKeyEventTime > KEY_EVENT_TIMEOUT * 10) {
            resetKeyStates()
        }
        lastKeyEventTime = currentTime
        
        Log.d(TAG, "KeyEvent: action=${event.action}, keyCode=${event.keyCode}, hidden=${preferences.appsHidden}, locked=$isScreenLocked")
        
        // Handle key events for combination detection
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                when (event.keyCode) {
                    KeyEvent.KEYCODE_VOLUME_UP -> {
                        volumeUpPressed = true
                        Log.d(TAG, "Volume UP pressed")
                        checkKeyCombination()
                        handleLongPress(event.keyCode)
                    }
                    KeyEvent.KEYCODE_VOLUME_DOWN -> {
                        volumeDownPressed = true
                        Log.d(TAG, "Volume DOWN pressed")
                        checkKeyCombination()
                        handleLongPress(event.keyCode)
                    }
                    KeyEvent.KEYCODE_POWER -> {
                        powerPressed = true
                        Log.d(TAG, "Power pressed")
                        checkKeyCombination()
                        handleLongPress(event.keyCode)
                    }
                }
            }
            KeyEvent.ACTION_UP -> {
                when (event.keyCode) {
                    KeyEvent.KEYCODE_VOLUME_UP -> {
                        volumeUpPressed = false
                        cancelLongPress()
                    }
                    KeyEvent.KEYCODE_VOLUME_DOWN -> {
                        volumeDownPressed = false
                        cancelLongPress()
                    }
                    KeyEvent.KEYCODE_POWER -> {
                        powerPressed = false
                        cancelLongPress()
                    }
                }
            }
        }
        
        // Block navigation keys when apps are hidden (but not on lock screen)
        if (HiddenModeStateManager.currentState && !isScreenLocked) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_HOME,
                KeyEvent.KEYCODE_BACK,
                KeyEvent.KEYCODE_APP_SWITCH,
                KeyEvent.KEYCODE_MENU -> {
                    Log.d(TAG, "Blocked navigation key: ${event.keyCode}")
                    return true // Consume the event
                }
            }
        }
        
        // Always allow key combinations to work, even on lock screen
        return false // Let the system handle other keys
    }
    
    private fun resetKeyStates() {
        volumeUpPressed = false
        volumeDownPressed = false
        powerPressed = false
        cancelLongPress()
    }
    
    private fun checkKeyCombination() {
        val preferences = LauncherApplication.instance.preferences
        val combo = preferences.selectedKeyCombination
        
        Log.d(TAG, "Checking key combination: combo=$combo, volUp=$volumeUpPressed, volDown=$volumeDownPressed, power=$powerPressed")
        
        var detected = false
        
        when (combo) {
            KeyCombination.BOTH_VOLUME -> {
                if (volumeUpPressed && volumeDownPressed) {
                    Log.d(TAG, "Both volume buttons detected!")
                    detected = true
                }
            }
            KeyCombination.POWER_VOL_UP -> {
                if (powerPressed && volumeUpPressed) {
                    Log.d(TAG, "Power + Vol Up detected!")
                    detected = true
                }
            }
            KeyCombination.POWER_VOL_DOWN -> {
                if (powerPressed && volumeDownPressed) {
                    Log.d(TAG, "Power + Vol Down detected!")
                    detected = true
                }
            }
            KeyCombination.POWER_HOLD,
            KeyCombination.VOL_UP_LONG,
            KeyCombination.VOL_DOWN_LONG -> {
                // These are handled in handleLongPress method
            }
        }
        
        if (detected) {
            toggleAppsVisibility()
            resetKeyStates()
        }
    }
    
    private fun handleLongPress(keyCode: Int) {
        val preferences = LauncherApplication.instance.preferences
        val combo = preferences.keyCombination
        
        when (combo) {
            KeyCombination.VOL_UP_LONG -> {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    startLongPressTimer()
                }
            }
            KeyCombination.VOL_DOWN_LONG -> {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    startLongPressTimer()
                }
            }
            KeyCombination.POWER_HOLD -> {
                if (keyCode == KeyEvent.KEYCODE_POWER) {
                    startLongPressTimer()
                }
            }
            else -> {}
        }
    }
    
    private fun startLongPressTimer() {
        cancelLongPress()
        longPressRunnable = Runnable {
            Log.d(TAG, "Long press triggered!")
            toggleAppsVisibility()
            resetKeyStates()
        }
        keyPressHandler.postDelayed(longPressRunnable!!, 1000)
    }
    
    private fun cancelLongPress() {
        longPressRunnable?.let {
            keyPressHandler.removeCallbacks(it)
            longPressRunnable = null
        }
    }
    
    private fun checkScreenLockState() {
        try {
            isScreenLocked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                keyguardManager.isDeviceLocked
            } else {
                keyguardManager.isKeyguardLocked
            }
            Log.d(TAG, "Screen lock state: $isScreenLocked")
        } catch (e: Exception) {
            Log.e(TAG, "Error checking screen lock state", e)
            isScreenLocked = false
        }
    }
    
    private fun toggleAppsVisibility() {
        val wasBefore = HiddenModeStateManager.currentState
        Log.d(TAG, "Toggling apps visibility: wasHidden=$wasBefore, fromLockScreen=$isScreenLocked")
        
        // Use centralized state manager for consistent state
        HiddenModeStateManager.toggleHiddenMode(this)
        
        val isNow = HiddenModeStateManager.currentState
        Log.d(TAG, "State toggled: wasHidden=$wasBefore -> isHidden=$isNow")
        
        // Send broadcast to notify about the change
        val intent = Intent(ACTION_TOGGLE_APPS)
        intent.setPackage(packageName) // Make it explicit
        intent.putExtra("from_lock_screen", isScreenLocked)
        intent.putExtra("hidden", isNow)
        sendBroadcast(intent)
        
        Log.d(TAG, "Broadcast sent for state change")
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        cancelLongPress()
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

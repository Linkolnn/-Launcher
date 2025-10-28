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
import com.customlauncher.app.receiver.KeyCombinationReceiver
import com.customlauncher.app.receiver.CustomKeyListener
import com.customlauncher.app.data.model.CustomKeyCombination
import android.app.KeyguardManager
import android.provider.Settings
import com.customlauncher.app.manager.HiddenModeStateManager
import android.view.accessibility.AccessibilityNodeInfo

class SystemBlockAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "SystemBlockAccessibility"
        const val ACTION_BLOCK_TOUCHES = "com.customlauncher.app.BLOCK_TOUCHES"
        const val ACTION_UNBLOCK_TOUCHES = "com.customlauncher.app.UNBLOCK_TOUCHES"
        const val ACTION_REFRESH_KEYS = "com.customlauncher.app.REFRESH_KEYS"
        private const val ACTION_TOGGLE_APPS = "com.customlauncher.app.TOGGLE_APPS"
        var instance: SystemBlockAccessibilityService? = null
        private const val KEYGUARD_CHECK_DELAY = 50L
    }
    
    // For key combination detection
    private var customKeyListener: CustomKeyListener? = null
    private val keyPressHandler = Handler(Looper.getMainLooper())
    private var lastKeyEventTime = 0L
    private val KEY_EVENT_TIMEOUT = 100L // milliseconds
    private var isScreenLocked = false
    private lateinit var keyguardManager: KeyguardManager
    private var isTouchExplorationEnabled = false
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        
        // Initialize KeyguardManager
        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        
        val info = AccessibilityServiceInfo().apply {
            // Listen to key events primarily
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            // Set flags to intercept key events BUT NOT touch exploration by default
            flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                    AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            // Remove FLAG_REQUEST_TOUCH_EXPLORATION_MODE from default flags
            
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
        }
        
        serviceInfo = info
        Log.d(TAG, "Accessibility service connected - touch blocking OFF by default")
        
        // Check initial lock state
        checkScreenLockState()
        
        // Initialize state manager
        HiddenModeStateManager.initializeState()
        Log.d(TAG, "Initial state - Hidden mode: ${HiddenModeStateManager.currentState}")
        
        // Only enable touch blocking if already in hidden mode
        if (HiddenModeStateManager.currentState) {
            enableTouchBlocking()
        }
        
        // Setup custom key listener
        setupCustomKeyListener()
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        try {
            // Block ALL events when in hidden mode
            if (HiddenModeStateManager.currentState) {
                // Block all touch and gesture events
                when (event?.eventType) {
                    AccessibilityEvent.TYPE_TOUCH_INTERACTION_START,
                    AccessibilityEvent.TYPE_TOUCH_INTERACTION_END,
                    AccessibilityEvent.TYPE_GESTURE_DETECTION_START,
                    AccessibilityEvent.TYPE_GESTURE_DETECTION_END,
                    AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START,
                    AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END,
                    AccessibilityEvent.TYPE_VIEW_CLICKED,
                    AccessibilityEvent.TYPE_VIEW_LONG_CLICKED,
                    AccessibilityEvent.TYPE_VIEW_SCROLLED,
                    AccessibilityEvent.TYPE_VIEW_HOVER_ENTER,
                    AccessibilityEvent.TYPE_VIEW_HOVER_EXIT -> {
                        Log.d(TAG, "Event blocked in hidden mode: ${event.eventType}")
                        try {
                            event.recycle()
                        } catch (e: Exception) {
                            Log.e(TAG, "Error recycling event", e)
                        }
                        return
                    }
                    AccessibilityEvent.TYPE_VIEW_FOCUSED,
                    AccessibilityEvent.TYPE_VIEW_SELECTED,
                    AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                    AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                        // Block the event completely
                        Log.d(TAG, "Event blocked in hidden mode: ${event.eventType}")
                        
                        // Try to prevent system UI interactions
                        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                            val packageName = event.packageName?.toString()
                            if (packageName == "com.android.systemui") {
                                // Block system UI interactions (status bar, navigation)
                                performGlobalAction(GLOBAL_ACTION_BACK)
                                Log.d(TAG, "Blocked SystemUI interaction")
                            }
                        }
                        
                        event.recycle()
                        return
                    }
                }
                
                // Disable interaction with any UI element
                event?.source?.let { node ->
                    disableNode(node)
                    node.recycle()
                }
            }
            
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
            
            // Disable interaction with any UI element
            event?.source?.let { node ->
                disableNode(node)
                node.recycle()
            }
        
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
        } catch (e: Exception) {
            Log.e(TAG, "Error in onAccessibilityEvent", e)
        }
    }
    
    override fun onKeyEvent(event: KeyEvent): Boolean {
        // Only handle key down events
        if (event.action != KeyEvent.ACTION_DOWN) {
            return false
        }
        
        val currentTime = System.currentTimeMillis()
        lastKeyEventTime = currentTime
        
        // Check if screen is locked with delay to ensure accurate state
        keyPressHandler.postDelayed({
            checkScreenLockState()
        }, KEYGUARD_CHECK_DELAY)
        
        Log.d(TAG, "KeyEvent: action=${event.action}, keyCode=${event.keyCode}, hidden=${HiddenModeStateManager.currentState}, locked=$isScreenLocked")
        
        // Handle custom key combinations if enabled
        val preferences = LauncherApplication.instance.preferences
        if (preferences.useCustomKeys) {
            customKeyListener?.let { listener ->
                if (listener.onKeyEvent(event.keyCode)) {
                    return true
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
        
        // Let other keys pass through
        return false // Let the system handle other keys
    }
    
    private fun setupCustomKeyListener() {
        // Clean up old listener first
        customKeyListener?.destroy()
        customKeyListener = null
        
        val preferences = LauncherApplication.instance.preferences
        
        if (preferences.useCustomKeys) {
            val customKeysString = preferences.customKeyCombination
            if (customKeysString != null) {
                val keys = customKeysString.split(",").mapNotNull { it.toIntOrNull() }
                if (keys.isNotEmpty()) {
                    val combination = CustomKeyCombination(keys)
                    customKeyListener = CustomKeyListener {
                        Log.d(TAG, "Custom key combination detected!")
                        sendKeyCombinationBroadcast()
                    }
                    customKeyListener?.setCombination(combination)
                    Log.d(TAG, "Custom key listener setup with keys: $keys")
                }
            }
        }
    }
    
    private fun sendKeyCombinationBroadcast() {
        Log.d(TAG, "Key combination detected, toggling hidden mode")
        
        // Get current state
        val currentState = HiddenModeStateManager.currentState
        val newState = !currentState
        
        Log.d(TAG, "Toggling from $currentState to $newState")
        
        // Toggle the state
        HiddenModeStateManager.setHiddenMode(this, newState)
        
        // Also send broadcast to MainActivity to update UI
        val intent = Intent("com.customlauncher.HIDDEN_MODE_CHANGED")
        intent.putExtra("hidden", newState)
        sendBroadcast(intent)
    }
    
    private fun disableNode(node: AccessibilityNodeInfo) {
        try {
            // Make the node non-clickable and non-focusable
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                node.isClickable = false
                node.isLongClickable = false
                node.isFocusable = false
                node.isEnabled = false
            }
            
            // Recursively disable child nodes
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { child ->
                    disableNode(child)
                    child.recycle()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error disabling node", e)
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
    
    private fun toggleHiddenMode() {
        Log.d(TAG, "Toggling hidden mode from accessibility service")
        
        val currentState = HiddenModeStateManager.currentState
        val newState = !currentState
        
        Log.d(TAG, "Current state: $currentState, new state: $newState")
        
        // Enable/disable touch blocking based on new state
        if (newState) {
            enableTouchBlocking()
        } else {
            disableTouchBlocking()
        }
        
        // Update the state through the manager
        HiddenModeStateManager.setHiddenMode(this, newState)
        
        // Send broadcast to notify other components
        val intent = Intent(ACTION_TOGGLE_APPS)
        val isNow = HiddenModeStateManager.currentState
        intent.putExtra("hidden", isNow)
        sendBroadcast(intent)
        
        Log.d(TAG, "Broadcast sent for state change")
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }
    
    override fun onGesture(gestureId: Int): Boolean {
        // Block all gestures in hidden mode
        if (HiddenModeStateManager.currentState) {
            Log.d(TAG, "GESTURE BLOCKED in hidden mode: $gestureId")
            
            // Block multitouch gestures specifically
            when (gestureId) {
                AccessibilityService.GESTURE_2_FINGER_SINGLE_TAP,
                AccessibilityService.GESTURE_2_FINGER_DOUBLE_TAP,
                AccessibilityService.GESTURE_2_FINGER_TRIPLE_TAP,
                AccessibilityService.GESTURE_3_FINGER_SINGLE_TAP,
                AccessibilityService.GESTURE_3_FINGER_DOUBLE_TAP,
                AccessibilityService.GESTURE_3_FINGER_TRIPLE_TAP,
                AccessibilityService.GESTURE_2_FINGER_SWIPE_UP,
                AccessibilityService.GESTURE_2_FINGER_SWIPE_DOWN,
                AccessibilityService.GESTURE_2_FINGER_SWIPE_LEFT,
                AccessibilityService.GESTURE_2_FINGER_SWIPE_RIGHT,
                AccessibilityService.GESTURE_3_FINGER_SWIPE_UP,
                AccessibilityService.GESTURE_3_FINGER_SWIPE_DOWN,
                AccessibilityService.GESTURE_3_FINGER_SWIPE_LEFT,
                AccessibilityService.GESTURE_3_FINGER_SWIPE_RIGHT -> {
                    Log.d(TAG, "MULTITOUCH GESTURE BLOCKED: $gestureId")
                    return true // Consume the gesture
                }
            }
            return true // Block all gestures
        }
        return super.onGesture(gestureId)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action}")
        
        when (intent?.action) {
            ACTION_BLOCK_TOUCHES -> {
                enableTouchBlocking()
                // Refresh key listener when entering hidden mode
                setupCustomKeyListener()
            }
            ACTION_UNBLOCK_TOUCHES -> {
                disableTouchBlocking()
                // Refresh key listener when exiting hidden mode
                setupCustomKeyListener()
            }
            ACTION_REFRESH_KEYS -> {
                Log.d(TAG, "Refreshing custom key listener")
                setupCustomKeyListener()
            }
            "com.customlauncher.CLOSE_ALL_APPS" -> {
                closeAllApps()
                // Also refresh keys after closing apps
                setupCustomKeyListener()
            }
            Intent.ACTION_SCREEN_OFF -> {
                // Optional: Can lock screen if needed
                // performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
            }
        }
        
        return super.onStartCommand(intent, flags, startId)
    }
    
    private fun enableTouchBlocking() {
        try {
            Log.d(TAG, "Enabling touch blocking...")
            
            // Hide keyboard if visible
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val controller = softKeyboardController
                controller?.showMode = AccessibilityService.SHOW_MODE_HIDDEN
            }
            
            // Add touch exploration flag to enable touch blocking with multitouch control
            val info = serviceInfo
            info.flags = info.flags or 
                AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
                AccessibilityServiceInfo.FLAG_REQUEST_MULTI_FINGER_GESTURES or
                AccessibilityServiceInfo.FLAG_SEND_MOTION_EVENTS
            serviceInfo = info
            
            isTouchExplorationEnabled = true
            Log.d(TAG, "Touch blocking ENABLED - exploration mode active")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to enable touch blocking", e)
        }
    }
    
    private fun closeAllApps() {
        try {
            Log.d(TAG, "Attempting to close all apps via accessibility service")
            
            // Method 1: Go directly to home screen (fastest)
            performGlobalAction(GLOBAL_ACTION_HOME)
            
            // Method 2: Send back button to ensure current app is closed
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                performGlobalAction(GLOBAL_ACTION_BACK)
                // One more home to ensure we're on launcher
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    performGlobalAction(GLOBAL_ACTION_HOME)
                }, 50)
            }, 100)
            
            Log.d(TAG, "Close all apps commands sent")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to close all apps", e)
        }
    }
    
    private fun disableTouchBlocking() {
        try {
            Log.d(TAG, "Disabling touch blocking...")
            
            // Restore keyboard to normal mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val controller = softKeyboardController
                controller?.showMode = AccessibilityService.SHOW_MODE_AUTO
                Log.d(TAG, "Keyboard restored to auto mode")
            }
            
            // Remove touch exploration and multitouch flags to disable touch blocking
            val info = serviceInfo
            info.flags = info.flags and 
                (AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
                AccessibilityServiceInfo.FLAG_REQUEST_MULTI_FINGER_GESTURES or
                AccessibilityServiceInfo.FLAG_SEND_MOTION_EVENTS).inv()
            serviceInfo = info
            
            isTouchExplorationEnabled = false
            Log.d(TAG, "Touch blocking DISABLED - exploration mode inactive")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to disable touch blocking", e)
        }
    }
}

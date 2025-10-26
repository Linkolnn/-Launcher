package com.customlauncher.app.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log

class SensorControlService : Service() {
    
    companion object {
        private const val TAG = "SensorControlService"
        const val ACTION_DISABLE_SENSOR = "com.customlauncher.app.DISABLE_SENSOR"
        const val ACTION_ENABLE_SENSOR = "com.customlauncher.app.ENABLE_SENSOR"
        
        private var isDisabled = false
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "SensorControlService created")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DISABLE_SENSOR -> {
                disableTouchSensor()
            }
            ACTION_ENABLE_SENSOR -> {
                enableTouchSensor()
            }
        }
        return START_STICKY
    }
    
    private fun disableTouchSensor() {
        if (isDisabled) {
            Log.d(TAG, "Touch already disabled")
            return
        }

        Log.d(TAG, "Disabling touch via AccessibilityService")

        try {
            // Method 1: Try to disable via accessibility settings
            val settingsSuccess = tryDisableTouchViaSettings()
            
            // Method 2: Request AccessibilityService to handle blocking
            val intent = Intent("com.customlauncher.app.BLOCK_TOUCH")
            intent.setPackage(packageName)
            sendBroadcast(intent)
            
            isDisabled = true
            Log.d(TAG, "Touch disable request sent to AccessibilityService")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error disabling touch", e)
        }
    }
    
    private fun enableTouchSensor() {
        if (!isDisabled) {
            Log.d(TAG, "Touch already enabled")
            return
        }

        Log.d(TAG, "Enabling touch via AccessibilityService")

        try {
            // Re-enable via settings
            tryEnableTouchViaSettings()
            
            // Request AccessibilityService to stop blocking
            val intent = Intent("com.customlauncher.app.UNBLOCK_TOUCH")
            intent.setPackage(packageName)
            sendBroadcast(intent)
            
            isDisabled = false
            Log.d(TAG, "Touch enable request sent to AccessibilityService")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error enabling touch", e)
        }
    }
    
    private fun tryDisableTouchViaSettings(): Boolean {
        return try {
            // Check if we have WRITE_SETTINGS permission
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Log.w(TAG, "No WRITE_SETTINGS permission")
                    return false
                }
            }
            
            // Try to modify touch settings
            Settings.System.putInt(contentResolver, "touch_disable_mode", 1)
            Settings.System.putInt(contentResolver, "touch_exploration_enabled", 0)
            Settings.System.putInt(contentResolver, "accessibility_touch_exploration_enabled", 0)
            
            Log.d(TAG, "Touch settings modified")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Settings method failed", e)
            false
        }
    }
    
    private fun tryEnableTouchViaSettings(): Boolean {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    return false
                }
            }
            
            Settings.System.putInt(contentResolver, "touch_disable_mode", 0)
            Settings.System.putInt(contentResolver, "touch_exploration_enabled", 1)
            Settings.System.putInt(contentResolver, "accessibility_touch_exploration_enabled", 0)
            
            Log.d(TAG, "Touch settings restored")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Settings method failed", e)
            false
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        enableTouchSensor()
    }
}

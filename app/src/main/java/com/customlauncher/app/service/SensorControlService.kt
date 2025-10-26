package com.customlauncher.app.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import java.io.DataOutputStream
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import com.customlauncher.app.receiver.LauncherDeviceAdminReceiver

class SensorControlService : Service() {
    
    companion object {
        private const val TAG = "SensorControlService"
        const val ACTION_DISABLE_SENSOR = "com.customlauncher.app.DISABLE_SENSOR"
        const val ACTION_ENABLE_SENSOR = "com.customlauncher.app.ENABLE_SENSOR"
        
        private var isDisabled = false
    }
    
    private lateinit var powerManager: PowerManager
    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var componentName: ComponentName
    
    override fun onCreate() {
        super.onCreate()
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        componentName = ComponentName(this, LauncherDeviceAdminReceiver::class.java)
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
            Log.d(TAG, "Sensor already disabled")
            return
        }
        
        Log.d(TAG, "Attempting to disable touch sensor")
        
        try {
            // Don't use lockNow() as it turns off the screen
            // Instead try alternative methods
            
            // Method 1: Try to disable touch via root (most effective)
            val rootSuccess = tryDisableTouchViaRoot()
            
            // Method 2: Try to disable via accessibility settings
            val settingsSuccess = tryDisableTouchViaSettings()
            
            // Method 3: Use Device Admin for other restrictions if available
            if (devicePolicyManager.isAdminActive(componentName)) {
                // Keep screen on but try to restrict interaction
                @Suppress("DEPRECATION")
                wakeLock = powerManager.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "LauncherApp:TouchDisable"
                )
                wakeLock.acquire(10*60*1000L /*10 minutes*/)
                
                // Try to disable keyguard features instead of locking
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    devicePolicyManager.setKeyguardDisabledFeatures(
                        componentName,
                        DevicePolicyManager.KEYGUARD_DISABLE_FINGERPRINT or
                        DevicePolicyManager.KEYGUARD_DISABLE_TRUST_AGENTS or
                        DevicePolicyManager.KEYGUARD_DISABLE_UNREDACTED_NOTIFICATIONS
                    )
                }
                
                Log.d(TAG, "Device Admin restrictions applied")
            }
            
            if (rootSuccess || settingsSuccess) {
                isDisabled = true
                Log.d(TAG, "Touch sensor disabled successfully")
            } else {
                Log.w(TAG, "Could not disable touch sensor via system methods")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to disable touch sensor", e)
        }
    }
    
    private fun enableTouchSensor() {
        if (!isDisabled) {
            Log.d(TAG, "Sensor already enabled")
            return
        }
        
        Log.d(TAG, "Attempting to enable touch sensor")
        
        try {
            // Release wake lock if held
            if (::wakeLock.isInitialized && wakeLock.isHeld) {
                wakeLock.release()
            }
            
            // Try to re-enable touch
            tryEnableTouchViaRoot()
            tryEnableTouchViaSettings()
            
            isDisabled = false
            Log.d(TAG, "Touch sensor enabled")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to enable touch sensor", e)
        }
    }
    
    private fun tryDisableTouchViaRoot(): Boolean {
        return try {
            // This requires root access
            val process = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(process.outputStream)
            
            // Find all touch input devices and disable them
            os.writeBytes("for i in /sys/class/input/input*/name; do\n")
            os.writeBytes("  name=$(cat \"\$i\")\n")
            os.writeBytes("  if [[ \"\$name\" == *touch* ]] || [[ \"\$name\" == *Touch* ]]; then\n")
            os.writeBytes("    dir=\$(dirname \"\$i\")\n")
            os.writeBytes("    echo 0 > \"\$dir/enabled\" 2>/dev/null\n")
            os.writeBytes("  fi\n")
            os.writeBytes("done\n")
            
            // Also try common touch device paths
            os.writeBytes("echo 0 > /sys/class/input/input0/enabled 2>/dev/null\n")
            os.writeBytes("echo 0 > /sys/class/input/input1/enabled 2>/dev/null\n")
            os.writeBytes("echo 0 > /sys/class/input/input2/enabled 2>/dev/null\n")
            os.writeBytes("echo 0 > /sys/class/input/input3/enabled 2>/dev/null\n")
            
            // Alternative methods
            os.writeBytes("echo 0 > /sys/android_touch/enabled 2>/dev/null\n")
            os.writeBytes("echo 0 > /sys/devices/virtual/input/input1/enabled 2>/dev/null\n")
            
            os.writeBytes("exit\n")
            os.flush()
            os.close()
            
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                Log.d(TAG, "Touch disabled via root")
                true
            } else {
                Log.e(TAG, "Root command failed with exit code: $exitCode")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Root method failed", e)
            false
        }
    }
    
    private fun tryEnableTouchViaRoot(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(process.outputStream)
            
            // Find all touch input devices and enable them
            os.writeBytes("for i in /sys/class/input/input*/name; do\n")
            os.writeBytes("  name=$(cat \"\$i\")\n")
            os.writeBytes("  if [[ \"\$name\" == *touch* ]] || [[ \"\$name\" == *Touch* ]]; then\n")
            os.writeBytes("    dir=\$(dirname \"\$i\")\n")
            os.writeBytes("    echo 1 > \"\$dir/enabled\" 2>/dev/null\n")
            os.writeBytes("  fi\n")
            os.writeBytes("done\n")
            
            // Also try common touch device paths
            os.writeBytes("echo 1 > /sys/class/input/input0/enabled 2>/dev/null\n")
            os.writeBytes("echo 1 > /sys/class/input/input1/enabled 2>/dev/null\n")
            os.writeBytes("echo 1 > /sys/class/input/input2/enabled 2>/dev/null\n")
            os.writeBytes("echo 1 > /sys/class/input/input3/enabled 2>/dev/null\n")
            
            // Alternative methods
            os.writeBytes("echo 1 > /sys/android_touch/enabled 2>/dev/null\n")
            os.writeBytes("echo 1 > /sys/devices/virtual/input/input1/enabled 2>/dev/null\n")
            
            os.writeBytes("exit\n")
            os.flush()
            os.close()
            
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                Log.d(TAG, "Touch enabled via root")
                true
            } else {
                Log.e(TAG, "Root command failed with exit code: $exitCode")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Root method failed", e)
            false
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

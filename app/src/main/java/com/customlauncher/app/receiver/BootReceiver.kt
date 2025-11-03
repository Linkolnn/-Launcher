package com.customlauncher.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.ui.HomeScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Receiver for handling device boot and package updates
 * Ensures launcher remains the default and permissions are maintained
 */
class BootReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received intent: ${intent.action}")
        
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                handleBootCompleted(context)
            }
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                handlePackageReplaced(context)
            }
        }
    }
    
    private fun handleBootCompleted(context: Context) {
        Log.d(TAG, "Device boot completed, initializing launcher")
        
        val preferences = LauncherApplication.instance.preferences
        
        // Start coroutine for delayed initialization
        CoroutineScope(Dispatchers.Main).launch {
            // Wait a bit for system to stabilize
            delay(3000)
            
            // Check if we should be the default launcher
            if (preferences.showHomeScreen) {
                // Start home screen activity if we're the default launcher
                val homeIntent = Intent(context, HomeScreenActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(homeIntent)
            }
            
            // Re-enable accessibility service if it was previously used
            // Check based on accessibility permissions need
            checkAndRequestAccessibilityService(context)
        }
    }
    
    private fun handlePackageReplaced(context: Context) {
        Log.d(TAG, "Package updated, restoring permissions and state")
        
        val preferences = LauncherApplication.instance.preferences
        
        // Re-check all permissions
        CoroutineScope(Dispatchers.Main).launch {
            // Wait for update to complete
            delay(2000)
            
            // Check accessibility service
            checkAndRequestAccessibilityService(context)
            
            // Сбрасываем и переинициализируем состояние скрытого режима
            com.customlauncher.app.manager.HiddenModeStateManager.resetAndReinitialize(context)
            
            // Дополнительная задержка перед запуском активности
            delay(1000)
            
            // Start main activity if needed
            if (preferences.showHomeScreen) {
                val intent = Intent(context, HomeScreenActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        }
    }
    
    private fun checkAndRequestAccessibilityService(context: Context) {
        // Check if accessibility service is enabled using PermissionManager
        if (!com.customlauncher.app.manager.PermissionManager.isAccessibilityServiceEnabled(context)) {
            Log.w(TAG, "Accessibility service is not enabled after boot/update")
            // We can't automatically enable it, but we can notify the user
            // Consider showing a notification to remind the user to re-enable it
        } else {
            Log.d(TAG, "Accessibility service is already enabled")
        }
    }
}

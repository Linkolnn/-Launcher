package com.customlauncher.app.manager

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import com.customlauncher.app.service.SystemBlockAccessibilityService

/**
 * Manager for handling app permissions and ensuring they persist
 */
object PermissionManager {
    private const val TAG = "PermissionManager"
    
    /**
     * Check if overlay permission is granted
     */
    fun canDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }
    
    /**
     * Check if Do Not Disturb permission is granted
     */
    fun isNotificationPolicyAccessGranted(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager.isNotificationPolicyAccessGranted
        } else {
            true
        }
    }
    
    /**
     * Check if accessibility service is enabled
     */
    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        
        val colonSplitter = enabledServices.split(":")
        val packageName = context.packageName
        val serviceName = SystemBlockAccessibilityService::class.java.simpleName
        
        for (service in colonSplitter) {
            if (service.contains(packageName) && service.contains(serviceName)) {
                return true
            }
        }
        return false
    }
    
    /**
     * Check if usage stats permission is granted
     */
    fun hasUsageStatsPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } else {
            true
        }
    }
    
    /**
     * Check if write settings permission is granted
     */
    fun canWriteSettings(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            true
        }
    }
    
    /**
     * Request overlay permission
     */
    fun requestOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
    
    /**
     * Request Do Not Disturb permission
     */
    fun requestNotificationPolicyAccess(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
    
    /**
     * Request accessibility service
     */
    fun requestAccessibilityService(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
    
    /**
     * Request usage stats permission
     */
    fun requestUsageStatsPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
    
    /**
     * Request write settings permission
     */
    fun requestWriteSettingsPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:${context.packageName}")
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
    
    /**
     * Check all critical permissions
     */
    fun checkAllPermissions(context: Context): PermissionStatus {
        return PermissionStatus(
            overlay = canDrawOverlays(context),
            notificationPolicy = isNotificationPolicyAccessGranted(context),
            accessibility = isAccessibilityServiceEnabled(context),
            usageStats = hasUsageStatsPermission(context),
            writeSettings = canWriteSettings(context)
        )
    }
    
    /**
     * Log permission status
     */
    fun logPermissionStatus(context: Context) {
        val status = checkAllPermissions(context)
        Log.d(TAG, "Permission Status:")
        Log.d(TAG, "  - Overlay: ${status.overlay}")
        Log.d(TAG, "  - Notification Policy: ${status.notificationPolicy}")
        Log.d(TAG, "  - Accessibility: ${status.accessibility}")
        Log.d(TAG, "  - Usage Stats: ${status.usageStats}")
        Log.d(TAG, "  - Write Settings: ${status.writeSettings}")
    }
    
    data class PermissionStatus(
        val overlay: Boolean,
        val notificationPolicy: Boolean,
        val accessibility: Boolean,
        val usageStats: Boolean,
        val writeSettings: Boolean
    ) {
        val allGranted: Boolean
            get() = overlay && notificationPolicy && accessibility && usageStats && writeSettings
    }
}

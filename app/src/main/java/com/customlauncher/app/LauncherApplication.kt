package com.customlauncher.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.customlauncher.app.data.database.LauncherDatabase
import com.customlauncher.app.data.preferences.LauncherPreferences
import com.customlauncher.app.data.repository.AppRepository
import com.customlauncher.app.manager.PermissionManager
import com.customlauncher.app.manager.HiddenModeStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LauncherApplication : Application() {
    
    val database by lazy { LauncherDatabase.getDatabase(this) }
    val repository by lazy { AppRepository(this, database.hiddenAppDao()) }
    val preferences by lazy { LauncherPreferences(this) }
    
    // BroadcastReceiver for package changes
    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_PACKAGE_REMOVED,
                Intent.ACTION_PACKAGE_ADDED,
                Intent.ACTION_PACKAGE_REPLACED,
                Intent.ACTION_PACKAGE_CHANGED -> {
                    val packageName = intent.data?.schemeSpecificPart
                    Log.d("LauncherApplication", "Package change detected: ${intent.action} for $packageName")
                    
                    // Send broadcast to notify all components
                    sendBroadcast(Intent("com.customlauncher.PACKAGE_CHANGED").apply {
                        putExtra("action", intent.action)
                        putExtra("package", packageName)
                    })
                }
            }
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Register package change receiver
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }
        registerReceiver(packageChangeReceiver, filter)
        
        // Don't reset state - preserve user's choice
        // The hidden mode should persist until user explicitly changes it
        
        // Check and log permission status
        checkPermissionsOnStartup()
        
        // Проверяем и обрабатываем обновление приложения
        checkAndHandleAppUpdate()
    }
    
    private fun checkPermissionsOnStartup() {
        // Log current permission status
        PermissionManager.logPermissionStatus(this)
        
        // Check critical permissions after a delay to ensure system is ready
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // Wait for system to stabilize
            
            val status = PermissionManager.checkAllPermissions(this@LauncherApplication)
            
            // Log permission status changes
            Log.d("LauncherApplication", "Permission check complete:"
                + " overlay=${status.overlay}"
                + " notification=${status.notificationPolicy}"
                + " accessibility=${status.accessibility}"
                + " usageStats=${status.usageStats}"
                + " writeSettings=${status.writeSettings}")
            
            if (!status.allGranted) {
                Log.w("LauncherApplication", "Some permissions are missing after startup")
                // We can't automatically request them, but we track the state
            }
        }
    }
    
    private fun checkAndHandleAppUpdate() {
        try {
            val packageInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, 0)
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0)
            }
            
            val currentVersionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
            
            val prefs = getSharedPreferences("launcher_preferences", Context.MODE_PRIVATE)
            val savedVersionCode = prefs.getInt("app_version_code", -1)
            
            if (savedVersionCode != -1 && savedVersionCode < currentVersionCode) {
                Log.d("LauncherApplication", "App updated from version $savedVersionCode to $currentVersionCode")
                
                // Сбрасываем состояние accessibility service после обновления
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000) // Ждем стабилизации системы
                    
                    // Переинициализируем скрытый режим если он был включен
                    if (preferences.appsHidden) {
                        Log.d("LauncherApplication", "Reinitializing hidden mode after update")
                        HiddenModeStateManager.resetAndReinitialize(this@LauncherApplication)
                    }
                }
            }
            
            // Сохраняем текущую версию
            prefs.edit().putInt("app_version_code", currentVersionCode).apply()
            
        } catch (e: Exception) {
            Log.e("LauncherApplication", "Failed to check app version", e)
        }
    }
    
    override fun onTerminate() {
        super.onTerminate()
        try {
            unregisterReceiver(packageChangeReceiver)
        } catch (e: Exception) {
            Log.e("LauncherApplication", "Failed to unregister receiver", e)
        }
    }
    
    companion object {
        lateinit var instance: LauncherApplication
            private set
    }
}

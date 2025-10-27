package com.customlauncher.app.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.data.database.HiddenApp
import com.customlauncher.app.data.database.HiddenAppDao
import com.customlauncher.app.data.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepository(
    private val context: Context,
    private val hiddenAppDao: HiddenAppDao
) {
    
    private val packageManager = context.packageManager
    private val preferences = LauncherApplication.instance.preferences
    private var cachedHiddenApps: Set<String> = emptySet()
    private var lastCacheUpdate: Long = 0
    private val CACHE_VALIDITY_MS = 1000L // 1 second cache
    
    @Synchronized
    private fun getHiddenPackages(): Set<String> {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastCacheUpdate > CACHE_VALIDITY_MS) {
            // Refresh cache from preferences
            cachedHiddenApps = preferences.getHiddenApps()
            lastCacheUpdate = currentTime
        }
        return cachedHiddenApps
    }
    
    @Synchronized
    fun invalidateCache() {
        lastCacheUpdate = 0
    }

    fun getHiddenAppsFlow(): Flow<List<HiddenApp>> {
        return hiddenAppDao.getAllHiddenApps()
    }
    
    suspend fun getAllInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        
        val apps = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ requires ResolveInfoFlags
            packageManager.queryIntentActivities(
                intent,
                android.content.pm.PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            // Android 12 and below
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, 0)
        }
        val hiddenPackages = getHiddenPackages()
        
        // Use default icon initially, load real icons lazily
        val defaultIcon = context.packageManager.defaultActivityIcon
        
        apps.mapNotNull { resolveInfo ->
            try {
                val appName = resolveInfo.loadLabel(packageManager).toString()
                val packageName = resolveInfo.activityInfo.packageName
                val isHidden = hiddenPackages.contains(packageName)
                
                // Use placeholder icon first, real icon will be loaded in adapter
                AppInfo(appName, packageName, defaultIcon, isHidden)
            } catch (e: Exception) {
                null // Skip apps that cause errors
            }
        }.sortedBy { it.appName.lowercase() }
    }
    
    suspend fun getVisibleApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        getAllInstalledApps().filter { !it.isHidden }
    }
    
    suspend fun getHiddenApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        getAllInstalledApps().filter { it.isHidden }
    }
    
    suspend fun hideApp(packageName: String) {
        hiddenAppDao.insert(HiddenApp(packageName))
    }
    
    suspend fun hideApps(packageNames: List<String>) {
        hiddenAppDao.insertAll(packageNames.map { HiddenApp(it) })
    }
    
    suspend fun unhideApp(packageName: String) {
        hiddenAppDao.deleteByPackageName(packageName)
    }
    
    suspend fun unhideApps(packageNames: List<String>) {
        packageNames.forEach { hiddenAppDao.deleteByPackageName(it) }
    }
    
    suspend fun isAppHidden(packageName: String): Boolean {
        return hiddenAppDao.isAppHidden(packageName)
    }
    
    fun launchApp(packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(it)
        }
    }
}

package com.customlauncher.app.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache
import com.customlauncher.app.LauncherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object IconCache {
    // Cache size - 10MB for icons
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8 // Use 1/8th of available memory
    
    private val memoryCache = LruCache<String, Bitmap>(cacheSize)
    
    fun getCachedIcon(packageName: String): Bitmap? {
        return memoryCache.get(packageName)
    }
    
    fun putIcon(packageName: String, icon: Bitmap) {
        memoryCache.put(packageName, icon)
    }
    
    suspend fun loadIcon(
        context: Context,
        packageName: String,
        packageManager: PackageManager,
        componentName: ComponentName? = null
    ): Drawable = withContext(Dispatchers.IO) {
        try {
            // Generate cache key including icon pack
            val iconPackPackage = LauncherApplication.instance.preferences.iconPackPackageName
            val cacheKey = if (iconPackPackage != null) {
                "$packageName:$iconPackPackage"
            } else {
                packageName
            }
            
            // Check cache first
            val cached = getCachedIcon(cacheKey)
            if (cached != null) {
                return@withContext BitmapDrawable(context.resources, cached)
            }
            
            var drawable: Drawable? = null
            
            // Try to load from icon pack first
            if (iconPackPackage != null && componentName != null) {
                val iconPackManager = IconPackManager(context)
                drawable = iconPackManager.getIconFromPack(iconPackPackage, componentName)
            }
            
            // Fall back to system icon
            if (drawable == null) {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                drawable = packageManager.getApplicationIcon(appInfo)
            }
            
            // Convert to bitmap and cache
            val bitmap = drawableToBitmap(drawable)
            putIcon(cacheKey, bitmap)
            
            drawable
        } catch (e: Exception) {
            // Return default icon on error
            context.packageManager.defaultActivityIcon
        }
    }
    
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth.coerceAtLeast(1),
            drawable.intrinsicHeight.coerceAtLeast(1),
            Bitmap.Config.ARGB_8888
        )
        
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        
        return bitmap
    }
    
    fun clear() {
        memoryCache.evictAll()
    }
}

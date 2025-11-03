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
    // Cache size - adaptive based on available memory
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 6 // Use 1/6th of available memory for better performance
    
    // Two-level cache: memory cache for fast access
    private val memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            // Return the size of the bitmap in kilobytes
            return bitmap.byteCount / 1024
        }
        
        override fun entryRemoved(evicted: Boolean, key: String, oldValue: Bitmap, newValue: Bitmap?) {
            if (evicted) {
                android.util.Log.d("IconCache", "Evicted icon from cache: $key")
            }
        }
    }
    
    // Track cache statistics
    private var cacheHits = 0
    private var cacheMisses = 0
    
    fun getCachedIcon(cacheKey: String): Bitmap? {
        val bitmap = memoryCache.get(cacheKey)
        if (bitmap != null) {
            cacheHits++
        } else {
            cacheMisses++
        }
        return bitmap
    }
    
    fun putIcon(cacheKey: String, icon: Bitmap) {
        memoryCache.put(cacheKey, icon)
    }
    
    /**
     * Get cache statistics for debugging
     */
    fun getCacheStats(): String {
        val hitRate = if (cacheHits + cacheMisses > 0) {
            (cacheHits * 100.0 / (cacheHits + cacheMisses)).toInt()
        } else 0
        return "Cache: ${memoryCache.size()}/${memoryCache.maxSize()} KB, Hit rate: $hitRate% ($cacheHits/${cacheHits + cacheMisses})"
    }
    
    /**
     * Clear the entire cache
     */
    fun clearCache() {
        memoryCache.evictAll()
        cacheHits = 0
        cacheMisses = 0
        android.util.Log.d("IconCache", "Cache cleared")
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

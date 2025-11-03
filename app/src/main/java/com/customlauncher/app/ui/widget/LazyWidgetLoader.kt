package com.customlauncher.app.ui.widget

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.customlauncher.app.data.model.HomeItemModel
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Lazy loader for widgets to improve performance
 * Loads widgets only when they are visible on screen
 */
class LazyWidgetLoader(
    private val context: Context,
    private val appWidgetHost: AppWidgetHost,
    private val appWidgetManager: AppWidgetManager
) {
    private val loadedWidgets = ConcurrentHashMap<Int, AppWidgetHostView>()
    private val pendingLoads = ConcurrentHashMap<Int, Job>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val mainHandler = Handler(Looper.getMainLooper())
    
    companion object {
        private const val TAG = "LazyWidgetLoader"
        private const val LOAD_DELAY_MS = 100L // Delay before loading to avoid loading during fast scroll
    }
    
    /**
     * Request widget loading with delay
     */
    fun loadWidget(
        item: HomeItemModel,
        onLoaded: (AppWidgetHostView) -> Unit
    ) {
        val widgetId = item.widgetId ?: return
        
        // Check if already loaded
        loadedWidgets[widgetId]?.let { 
            onLoaded(it)
            return 
        }
        
        // Cancel any pending load for this widget
        pendingLoads[widgetId]?.cancel()
        
        // Schedule new load with delay
        val job = scope.launch {
            delay(LOAD_DELAY_MS)
            
            try {
                val hostView = withContext(Dispatchers.IO) {
                    createWidgetView(widgetId)
                }
                
                if (hostView != null) {
                    loadedWidgets[widgetId] = hostView
                    withContext(Dispatchers.Main) {
                        onLoaded(hostView)
                    }
                    Log.d(TAG, "Loaded widget $widgetId, total loaded: ${loadedWidgets.size}")
                }
            } catch (e: CancellationException) {
                Log.d(TAG, "Widget load cancelled for $widgetId")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading widget $widgetId", e)
            } finally {
                pendingLoads.remove(widgetId)
            }
        }
        
        pendingLoads[widgetId] = job
    }
    
    /**
     * Create widget view on background thread
     */
    private suspend fun createWidgetView(widgetId: Int): AppWidgetHostView? {
        return suspendCancellableCoroutine { continuation ->
            mainHandler.post {
                try {
                    val appWidgetInfo = appWidgetManager.getAppWidgetInfo(widgetId)
                    if (appWidgetInfo != null) {
                        val hostView = appWidgetHost.createView(
                            context,
                            widgetId,
                            appWidgetInfo
                        )
                        continuation.resume(hostView, null)
                    } else {
                        continuation.resume(null, null)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error creating widget view", e)
                    continuation.resume(null, null)
                }
            }
        }
    }
    
    /**
     * Cancel loading for widget
     */
    fun cancelLoad(widgetId: Int) {
        pendingLoads[widgetId]?.cancel()
        pendingLoads.remove(widgetId)
        Log.d(TAG, "Cancelled load for widget $widgetId")
    }
    
    /**
     * Unload widget from memory
     */
    fun unloadWidget(widgetId: Int) {
        cancelLoad(widgetId)
        loadedWidgets.remove(widgetId)
        Log.d(TAG, "Unloaded widget $widgetId, remaining: ${loadedWidgets.size}")
    }
    
    /**
     * Clear all loaded widgets
     */
    fun clearAll() {
        // Cancel all pending loads
        pendingLoads.values.forEach { it.cancel() }
        pendingLoads.clear()
        
        // Clear loaded widgets
        loadedWidgets.clear()
        
        Log.d(TAG, "Cleared all widgets")
    }
    
    /**
     * Get statistics for debugging
     */
    fun getStats(): String {
        return "Loaded: ${loadedWidgets.size}, Pending: ${pendingLoads.size}"
    }
    
    /**
     * Clean up resources
     */
    fun destroy() {
        clearAll()
        scope.cancel()
    }
}

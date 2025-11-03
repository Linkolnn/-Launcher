package com.customlauncher.app.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.customlauncher.app.R
import java.util.LinkedList
import java.util.Queue

/**
 * View pool manager for recycling views on home screen
 * Improves performance by reusing views instead of creating new ones
 */
class HomeScreenViewPool(
    private val inflater: LayoutInflater
) {
    private val appViewPool: Queue<View> = LinkedList()
    private val widgetContainerPool: Queue<View> = LinkedList()
    
    // Pool size limits
    private val maxPoolSize = 20
    
    companion object {
        private const val TAG = "HomeScreenViewPool"
    }
    
    /**
     * Get or create app view from pool
     */
    fun obtainAppView(parent: ViewGroup? = null): View {
        var view = appViewPool.poll()
        if (view == null) {
            Log.d(TAG, "Creating new app view, pool size: ${appViewPool.size}")
            view = inflater.inflate(R.layout.item_home_app, parent, false)
        } else {
            Log.d(TAG, "Reusing app view from pool, remaining: ${appViewPool.size}")
        }
        return view
    }
    
    /**
     * Get or create widget container from pool
     */
    fun obtainWidgetContainer(parent: ViewGroup? = null): View {
        var view = widgetContainerPool.poll()
        if (view == null) {
            Log.d(TAG, "Creating new widget container, pool size: ${widgetContainerPool.size}")
            view = android.widget.FrameLayout(inflater.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        } else {
            Log.d(TAG, "Reusing widget container from pool, remaining: ${widgetContainerPool.size}")
        }
        return view
    }
    
    /**
     * Return app view to pool for reuse
     */
    fun recycleAppView(view: View) {
        if (appViewPool.size < maxPoolSize) {
            // Clear the view state before returning to pool
            clearAppView(view)
            appViewPool.offer(view)
            Log.d(TAG, "Recycled app view to pool, new size: ${appViewPool.size}")
        }
    }
    
    /**
     * Return widget container to pool for reuse
     */
    fun recycleWidgetContainer(view: View) {
        if (widgetContainerPool.size < maxPoolSize) {
            // Clear the container before returning to pool
            if (view is ViewGroup) {
                view.removeAllViews()
            }
            widgetContainerPool.offer(view)
            Log.d(TAG, "Recycled widget container to pool, new size: ${widgetContainerPool.size}")
        }
    }
    
    /**
     * Clear app view state
     */
    private fun clearAppView(view: View) {
        view.findViewById<android.widget.ImageView>(R.id.appIcon)?.apply {
            setImageDrawable(null)
            tag = null
        }
        view.findViewById<android.widget.TextView>(R.id.appLabel)?.apply {
            text = ""
        }
        view.setOnClickListener(null)
        view.setOnLongClickListener(null)
        view.setOnTouchListener(null)
        view.tag = null
        
        // Clear focus and background to prevent white highlight
        view.background = null
        view.isSelected = false
        view.clearFocus()
    }
    
    /**
     * Clear all pools
     */
    fun clear() {
        appViewPool.clear()
        widgetContainerPool.clear()
        Log.d(TAG, "Cleared all view pools")
    }
    
    /**
     * Get current pool sizes for debugging
     */
    fun getPoolStats(): String {
        return "AppViews: ${appViewPool.size}/$maxPoolSize, Widgets: ${widgetContainerPool.size}/$maxPoolSize"
    }
}

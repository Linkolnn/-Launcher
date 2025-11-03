package com.customlauncher.app.ui.touch

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

/**
 * Detects whether a long press should trigger drag or context menu
 */
class DragOrMenuDetector(
    private val context: Context,
    private val onDragStart: (View) -> Boolean,
    private val onContextMenu: (View) -> Unit
) : View.OnTouchListener {
    
    private val handler = Handler(Looper.getMainLooper())
    private val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    
    private var initialX = 0f
    private var initialY = 0f
    private var longPressScheduled = false
    private var isDragging = false
    private var menuShown = false
    private var longPressRunnable: Runnable? = null
    
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.x
                initialY = event.y
                longPressScheduled = true
                isDragging = false
                menuShown = false
                
                // Schedule long press detection
                longPressRunnable = Runnable {
                    if (longPressScheduled && !isDragging) {
                        // User held without moving - show menu
                        menuShown = true
                        onContextMenu(view)
                        longPressScheduled = false
                    }
                }
                handler.postDelayed(longPressRunnable!!, longPressTimeout)
                
                // Don't consume - allow click listener to work
                return false
            }
            
            MotionEvent.ACTION_MOVE -> {
                if (longPressScheduled && !isDragging && !menuShown) {
                    val deltaX = event.x - initialX
                    val deltaY = event.y - initialY
                    
                    // Check if moved enough to be considered a drag
                    if (Math.abs(deltaX) > touchSlop || Math.abs(deltaY) > touchSlop) {
                        // Cancel long press and start drag
                        longPressRunnable?.let { handler.removeCallbacks(it) }
                        longPressScheduled = false
                        isDragging = true
                        
                        // Start drag operation
                        return onDragStart(view)
                    }
                }
                return false
            }
            
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                // Clean up
                longPressRunnable?.let { handler.removeCallbacks(it) }
                longPressScheduled = false
                
                // If nothing happened yet, it was a simple click
                // Click listener will handle it
                return false
            }
        }
        return false
    }
}

package com.customlauncher.app.ui.widget

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.customlauncher.app.R
import com.customlauncher.app.data.model.HomeItemModel
import com.customlauncher.app.data.model.GridConfiguration
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Manager for handling widget resize operations
 */
class WidgetResizeManager(
    private val context: Context,
    private val gridContainer: ViewGroup,
    private val gridConfig: GridConfiguration,
    private val onResizeComplete: (HomeItemModel, Int, Int) -> Unit,
    private val onResizeUpdate: ((HomeItemModel, Int, Int) -> Unit)? = null
) {
    companion object {
        private const val TAG = "WidgetResizeManager"
        private const val MIN_SPAN = 1
        private const val TOUCH_SLOP = 5 // pixels - more sensitive
        private const val UPDATE_DELAY_MS = 0L // instant update
    }
    
    private var resizeOverlay: View? = null
    private var activeWidget: View? = null
    private var activeItem: HomeItemModel? = null
    private var isResizing = false
    
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var initialSpanX = 1
    private var initialSpanY = 1
    private var currentSpanX = 1
    private var currentSpanY = 1
    
    private var cellWidth = 0
    private var cellHeight = 0
    
    /**
     * Start resize mode for a widget
     */
    fun startResize(widgetView: View, item: HomeItemModel) {
        try {
            if (isResizing) {
                stopResize(false)
            }
            
            Log.d(TAG, "Starting resize for widget: ${item.label}, component: ${item.componentName}")
            Log.d(TAG, "Initial size: ${item.spanX}x${item.spanY}, position: ${item.cellX},${item.cellY}")
            
            // Validate container dimensions
            if (gridContainer.width <= 0 || gridContainer.height <= 0) {
                Log.e(TAG, "Invalid grid container dimensions: ${gridContainer.width}x${gridContainer.height}")
                return
            }
            
            activeWidget = widgetView
            activeItem = item
            isResizing = true
            
            // Store initial state
            initialSpanX = item.spanX
            initialSpanY = item.spanY
            currentSpanX = item.spanX
            currentSpanY = item.spanY
            
            Log.d(TAG, "Resize initialized: initial=${initialSpanX}x${initialSpanY}")
            
            // Calculate cell dimensions with half-cell threshold for easier resizing
            cellWidth = gridContainer.width / gridConfig.columns
            cellHeight = gridContainer.height / gridConfig.rows
            
            if (cellWidth <= 0 || cellHeight <= 0) {
                Log.e(TAG, "Invalid cell dimensions: ${cellWidth}x${cellHeight}")
                stopResize(false)
                return
            }
            
            // Store initial spans
            initialSpanX = item.spanX
            initialSpanY = item.spanY
            currentSpanX = initialSpanX
            currentSpanY = initialSpanY
            
            // Create and show resize overlay
            showResizeOverlay(widgetView)
            
            // Set touch listener to stop resize when clicking outside
            // Using onTouchListener instead of onClickListener to not block long press
            gridContainer.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Check if touch is outside the widget bounds
                        val widgetBounds = android.graphics.Rect()
                        activeWidget?.getGlobalVisibleRect(widgetBounds)
                        
                        // Expand bounds to include resize handles (20dp handles, half outside)
                        val handleSize = (20 * context.resources.displayMetrics.density).toInt()
                        widgetBounds.inset(-handleSize, -handleSize)
                        
                        if (!widgetBounds.contains(event.rawX.toInt(), event.rawY.toInt())) {
                            // Touch is outside widget and handles, stop resize
                            stopResize(false)
                            return@setOnTouchListener true
                        }
                    }
                }
                false // Don't consume the event, allow it to pass through
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting resize", e)
            isResizing = false
        }
    }
    
    /**
     * Show resize overlay for the widget
     */
    private fun showResizeOverlay(widgetView: View) {
        try {
            // Create overlay
            val inflater = LayoutInflater.from(context)
            resizeOverlay = inflater.inflate(R.layout.widget_resize_overlay, null)
            
            // Position overlay over widget
            val overlayParams = FrameLayout.LayoutParams(
                widgetView.width.coerceAtLeast(1),
                widgetView.height.coerceAtLeast(1)
            )
            overlayParams.leftMargin = widgetView.left
            overlayParams.topMargin = widgetView.top
            
            // Add overlay to grid container
            if (gridContainer is FrameLayout) {
                // Ensure clipChildren is false to allow handles to extend outside
                gridContainer.clipChildren = false
                gridContainer.clipToPadding = false
                gridContainer.addView(resizeOverlay, overlayParams)
            } else {
                // Create a temporary FrameLayout to hold the overlay
                val overlayContainer = FrameLayout(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    // Allow handles to extend outside
                    clipChildren = false
                    clipToPadding = false
                }
                
                // Add overlay with absolute positioning
                overlayContainer.addView(resizeOverlay, overlayParams)
                
                // Add overlay container on top of grid container
                (gridContainer.parent as? ViewGroup)?.addView(overlayContainer)
                
                // Store reference to remove later
                resizeOverlay?.tag = overlayContainer
            }
            
            // Setup resize handles
            setupResizeHandles()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing resize overlay", e)
            stopResize(false)
        }
    }
    
    /**
     * Setup touch listeners for resize handles
     */
    private fun setupResizeHandles() {
        resizeOverlay?.let { overlay ->
            // Top handle - resize vertically from top
            overlay.findViewById<View>(R.id.resizeHandleTop)?.setOnTouchListener { _, event ->
                handleVerticalResize(event, fromTop = true)
                true
            }
            
            // Bottom handle - resize vertically from bottom
            overlay.findViewById<View>(R.id.resizeHandleBottom)?.setOnTouchListener { _, event ->
                handleVerticalResize(event, fromTop = false)
                true
            }
            
            // Left handle - resize horizontally from left
            overlay.findViewById<View>(R.id.resizeHandleLeft)?.setOnTouchListener { _, event ->
                handleHorizontalResize(event, fromLeft = true)
                true
            }
            
            // Right handle - resize horizontally from right
            overlay.findViewById<View>(R.id.resizeHandleRight)?.setOnTouchListener { _, event ->
                handleHorizontalResize(event, fromLeft = false)
                true
            }
        }
    }
    
    /**
     * Handle vertical resize with snap-to-grid
     */
    private fun handleVerticalResize(event: MotionEvent, fromTop: Boolean) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialTouchY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val item = activeItem ?: return
                val deltaY = event.rawY - initialTouchY
                
                // Calculate target cells with half-cell threshold for better UX
                val draggedCells = if (deltaY > 0) {
                    ((deltaY + cellHeight / 2) / cellHeight).toInt()
                } else {
                    ((deltaY - cellHeight / 2) / cellHeight).toInt()
                }
                
                // Determine widget constraints
                val isClockWidget = item.componentName == "clock" || 
                                   item.componentName == "com.customlauncher.widget.clock"
                val minSpanY = 1
                // For clock widget, allow up to 2 cells vertically, but respect grid bounds
                val maxSpanY = if (isClockWidget) {
                    min(2, gridConfig.rows - item.cellY)
                } else {
                    gridConfig.rows - item.cellY
                }
                
                // Calculate new span based on direction
                val newSpanY = if (fromTop) {
                    // Top handle - inverted logic
                    (initialSpanY - draggedCells).coerceIn(minSpanY, maxSpanY)
                } else {
                    // Bottom handle - normal logic
                    (initialSpanY + draggedCells).coerceIn(minSpanY, maxSpanY)
                }
                
                // Log constraints and calculations
                Log.d(TAG, "Y constraints: min=$minSpanY, max=$maxSpanY, initial=$initialSpanY, dragged=$draggedCells, new=$newSpanY")
                
                // Only update if span changed
                if (newSpanY != currentSpanY) {
                    Log.d(TAG, "Vertical resize: ${currentSpanY} -> $newSpanY")
                    currentSpanY = newSpanY
                    updateOverlaySize()
                    scheduleWidgetUpdate()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "Vertical resize complete: final size = ${currentSpanX}x$currentSpanY")
                applyResize()
            }
        }
    }
    
    /**
     * Handle horizontal resize with snap-to-grid
     */
    private fun handleHorizontalResize(event: MotionEvent, fromLeft: Boolean) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialTouchX = event.rawX
            }
            MotionEvent.ACTION_MOVE -> {
                val item = activeItem ?: return
                val deltaX = event.rawX - initialTouchX
                
                // Calculate target cells with half-cell threshold for better UX
                val draggedCells = if (deltaX > 0) {
                    ((deltaX + cellWidth / 2) / cellWidth).toInt()
                } else {
                    ((deltaX - cellWidth / 2) / cellWidth).toInt()
                }
                
                // Determine widget constraints
                val isClockWidget = item.componentName == "clock" || 
                                   item.componentName == "com.customlauncher.widget.clock"
                val minSpanX = 1
                // For clock widget, allow up to 4 cells horizontally, but respect grid bounds
                val maxSpanX = if (isClockWidget) {
                    min(4, gridConfig.columns - item.cellX)
                } else {
                    gridConfig.columns - item.cellX
                }
                
                // Calculate new span based on direction
                val newSpanX = if (fromLeft) {
                    // Left handle - inverted logic
                    (initialSpanX - draggedCells).coerceIn(minSpanX, maxSpanX)
                } else {
                    // Right handle - normal logic
                    (initialSpanX + draggedCells).coerceIn(minSpanX, maxSpanX)
                }
                
                // Log constraints and calculations
                Log.d(TAG, "X constraints: min=$minSpanX, max=$maxSpanX, initial=$initialSpanX, dragged=$draggedCells, new=$newSpanX")
                
                // Only update if span changed
                if (newSpanX != currentSpanX) {
                    Log.d(TAG, "Horizontal resize: ${currentSpanX} -> $newSpanX")
                    currentSpanX = newSpanX
                    updateOverlaySize()
                    scheduleWidgetUpdate()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "Horizontal resize complete: final size = ${currentSpanX}x$currentSpanY")
                applyResize()
            }
        }
    }
    
    /**
     * Update widget immediately
     */
    private fun scheduleWidgetUpdate() {
        // Instant update without delay
        activeItem?.let { item ->
            Log.d(TAG, "Updating widget to ${currentSpanX}x${currentSpanY}")
            onResizeUpdate?.invoke(item, currentSpanX, currentSpanY)
        }
    }
    
    /**
     * Update overlay size based on current spans
     */
    private fun updateOverlaySize() {
        try {
            resizeOverlay?.let { overlay ->
                activeItem?.let { item ->
                    val layoutParams = overlay.layoutParams as? FrameLayout.LayoutParams
                    if (layoutParams == null) {
                        Log.e(TAG, "Failed to get overlay layout params")
                        return
                    }
                    
                    layoutParams.width = currentSpanX * cellWidth
                    layoutParams.height = currentSpanY * cellHeight
                    
                    overlay.layoutParams = layoutParams
                    
                    // Also update the widget size preview
                    activeWidget?.let { widget ->
                        val widgetParams = widget.layoutParams
                        if (widgetParams != null) {
                            widgetParams.width = currentSpanX * cellWidth
                            widgetParams.height = currentSpanY * cellHeight
                            widget.layoutParams = widgetParams
                        }
                    }
                    
                    Log.d(TAG, "Updated size: ${currentSpanX}x${currentSpanY}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating overlay size", e)
        }
    }
    
    /**
     * Apply resize (save the new size)
     */
    private fun applyResize() {
        // Log final size before saving
        Log.d(TAG, "Applying resize: ${currentSpanX}x${currentSpanY} (was ${initialSpanX}x${initialSpanY})")
        
        // Ensure we update the item with current values before stopping
        activeItem?.let { item ->
            val updatedItem = item.copy(
                spanX = currentSpanX,
                spanY = currentSpanY
            )
            activeItem = updatedItem
        }
        
        stopResize(save = true)
    }
    
    /**
     * Stop resize mode
     */
    fun stopResize(save: Boolean = true) {
        if (!isResizing) return
        
        // Immediately set flag to prevent re-entry
        isResizing = false
        
        Log.d(TAG, "Stopping resize, save: $save")
        
        // Clear touch listener first to prevent events during cleanup
        gridContainer.setOnTouchListener(null)
        
        // Apply or restore size
        if (save && (currentSpanX != initialSpanX || currentSpanY != initialSpanY)) {
            // Save the new size
            activeItem?.let { item ->
                Log.d(TAG, "Saving resize: ${item.spanX}x${item.spanY} -> ${currentSpanX}x${currentSpanY}")
                onResizeComplete(item, currentSpanX, currentSpanY)
            }
        } else if (!save && (currentSpanX != initialSpanX || currentSpanY != initialSpanY)) {
            // Restore original size
            activeItem?.let { item ->
                Log.d(TAG, "Restoring original size: ${initialSpanX}x${initialSpanY}")
                onResizeUpdate?.invoke(item, initialSpanX, initialSpanY)
            }
        }
        
        // Remove overlay
        resizeOverlay?.let { overlay ->
            // Check if we have an overlay container to remove
            val overlayContainer = overlay.tag as? ViewGroup
            if (overlayContainer != null) {
                (overlayContainer.parent as? ViewGroup)?.removeView(overlayContainer)
            } else {
                (overlay.parent as? ViewGroup)?.removeView(overlay)
            }
        }
        resizeOverlay = null
        
        // Reset state
        activeWidget = null
        activeItem = null
    }
    
    /**
     * Check if currently resizing
     */
    fun isResizing(): Boolean = isResizing
}

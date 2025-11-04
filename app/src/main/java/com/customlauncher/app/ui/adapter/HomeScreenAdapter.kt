package com.customlauncher.app.ui.adapter

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.customlauncher.app.R
import com.customlauncher.app.data.model.HomeItemModel
import com.customlauncher.app.ui.layout.HomeScreenGridLayout
import com.customlauncher.app.ui.widget.ClockWidget
import com.customlauncher.app.ui.widget.LazyWidgetLoader
import com.customlauncher.app.utils.IconCache
import com.customlauncher.app.utils.AdaptiveSizeCalculator
import com.customlauncher.app.utils.PerformanceMonitor
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.widget.FrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.cancel

/**
 * Adapter for displaying items on the home screen grid
 */
class HomeScreenAdapter(
    private val context: Context,
    private val gridLayout: HomeScreenGridLayout,
    private val onItemClick: (HomeItemModel) -> Unit,
    private val onItemLongClick: (HomeItemModel, View) -> Boolean,
    private val onItemStartDrag: ((View, HomeItemModel) -> Boolean)? = null,
    private val appWidgetHost: AppWidgetHost? = null,
    private val appWidgetManager: AppWidgetManager? = null
) {
    private val items = mutableListOf<HomeItemModel>()
    private val itemViews = mutableMapOf<Long, View>()
    private val packageManager = context.packageManager
    private val inflater = LayoutInflater.from(context)
    private val adapterScope = CoroutineScope(Dispatchers.Main + Job())
    
    // Focus management
    private var focusedPosition: Int = -1
    private var isButtonMode: Boolean = false
    
    // Performance optimizations
    private val viewPool = HomeScreenViewPool(inflater)
    private val lazyWidgetLoader = if (appWidgetHost != null && appWidgetManager != null) {
        LazyWidgetLoader(context, appWidgetHost, appWidgetManager)
    } else null
    
    companion object {
        private const val TAG = "HomeScreenAdapter"
    }
    
    /**
     * Get current item count
     */
    fun getItemCount(): Int = items.size
    
    /**
     * Update displayed items with animation when items appear (e.g. when disabling hidden mode)
     */
    fun submitListAnimated(newItems: List<HomeItemModel>) {
        submitList(newItems, animate = true)
    }
    
    /**
     * Update displayed items
     */
    fun submitList(newItems: List<HomeItemModel>, animate: Boolean = false) {
        PerformanceMonitor.measureTime("submitList") {
            Log.d(TAG, "Updating items, count: ${newItems.size}, animate: $animate")
            
            // Check if items are actually different
            if (items == newItems) {
                Log.d(TAG, "Items haven't changed, skipping update")
                return@measureTime
            }
            
            // Check if this is just a removal (items decreased)
            val removedItems = items.filter { oldItem ->
                newItems.none { it.id == oldItem.id }
            }
            val addedItems = newItems.filter { newItem ->
                items.none { it.id == newItem.id }
            }
            
            // If only items were removed and no items added, use animation
            if (removedItems.isNotEmpty() && addedItems.isEmpty()) {
                Log.d(TAG, "Only removing items, using animation")
                removedItems.forEach { item ->
                    removeItemAnimated(item)
                }
                return@measureTime
            }
            
            // Otherwise do full refresh
            // Batch updates to prevent multiple visual refreshes
            gridLayout.post {
                // Always clear completely first to prevent duplicates when switching modes
                clearAllViews()
                items.clear()
                
                // If list is empty, we're done
                if (newItems.isEmpty()) {
                    Log.d(TAG, "Empty item list, grid cleared")
                    return@post
                }
                
                // Add all new items
                items.addAll(newItems)
                
                // Add views to grid in a single batch
                items.forEach { item ->
                    try {
                        addItemToGrid(item, animate)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error adding item ${item.id} to grid", e)
                    }
                }
                
                Log.d(TAG, "Grid updated with ${items.size} items")
                Log.d(TAG, "View pool stats: ${viewPool.getPoolStats()}")
            }
        }
    }
    
    /**
     * Clear all views with animation
     */
    private fun clearAllViewsAnimated() {
        itemViews.values.forEach { view ->
            view.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(150)
                .withEndAction {
                    gridLayout.removeView(view)
                    if (view.tag is HomeItemModel) {
                        val item = view.tag as HomeItemModel
                        if (item.type == HomeItemModel.ItemType.APP) {
                            viewPool.recycleAppView(view)
                        }
                    }
                }
                .start()
        }
        itemViews.clear()
    }
    
    /**
     * Update internal items list without rebuilding views
     * Used when deleting items to preserve positions
     */
    fun updateItemsList(newItems: List<HomeItemModel>) {
        Log.d(TAG, "Updating internal items list, count: ${newItems.size}")
        
        // Just update the internal list without rebuilding views
        items.clear()
        items.addAll(newItems)
    }
    
    /**
     * Update single item (e.g., after resize)
     */
    fun updateItem(updatedItem: HomeItemModel) {
        Log.d(TAG, "Updating item: ${updatedItem.id} with new size ${updatedItem.spanX}x${updatedItem.spanY}")
        
        // Update in the items list
        val index = items.indexOfFirst { it.id == updatedItem.id }
        if (index >= 0) {
            items[index] = updatedItem
            
            // Update the view if it's a widget
            if (updatedItem.type == HomeItemModel.ItemType.WIDGET) {
                val view = itemViews[updatedItem.id]
                if (view != null) {
                    // Calculate new dimensions
                    val cellWidth = gridLayout.width / gridLayout.getGridConfiguration().columns
                    val cellHeight = gridLayout.height / gridLayout.getGridConfiguration().rows
                    val newWidth = cellWidth * updatedItem.spanX
                    val newHeight = cellHeight * updatedItem.spanY
                    
                    // Update CellLayoutParams with new spans
                    val params = view.layoutParams as? HomeScreenGridLayout.CellLayoutParams
                    if (params != null) {
                        // Update spans in CellLayoutParams
                        params.spanX = updatedItem.spanX
                        params.spanY = updatedItem.spanY
                        params.width = newWidth
                        params.height = newHeight
                        view.layoutParams = params
                    }
                    
                    // For AppWidgetHostView in FrameLayout wrapper
                    if (view is FrameLayout) {
                        val hostView = view.getChildAt(0) as? AppWidgetHostView
                        if (hostView != null && updatedItem.widgetId != null) {
                            // Update AppWidgetHostView size
                            val displayMetrics = context.resources.displayMetrics
                            val padding = (4 * displayMetrics.density).toInt()
                            val widgetWidth = newWidth - (padding * 2)
                            val widgetHeight = newHeight - (padding * 2)
                            
                            // Update widget options with new size
                            val options = android.os.Bundle()
                            val widgetWidthDp = (widgetWidth / displayMetrics.density).toInt()
                            val widgetHeightDp = (widgetHeight / displayMetrics.density).toInt()
                            
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, widgetWidthDp)
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, widgetHeightDp)
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, widgetWidthDp)
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, widgetHeightDp)
                            
                            // Update widget
                            hostView.updateAppWidgetOptions(options)
                            appWidgetManager?.updateAppWidgetOptions(updatedItem.widgetId, options)
                            
                            // Force re-layout
                            hostView.measure(
                                android.view.View.MeasureSpec.makeMeasureSpec(widgetWidth, android.view.View.MeasureSpec.EXACTLY),
                                android.view.View.MeasureSpec.makeMeasureSpec(widgetHeight, android.view.View.MeasureSpec.EXACTLY)
                            )
                            hostView.layout(0, 0, widgetWidth, widgetHeight)
                        }
                    }
                    
                    // For ClockWidget
                    else if (view is ClockWidget) {
                        // Update ClockWidget spans for proper text sizing
                        view.updateSize(updatedItem.spanX, updatedItem.spanY)
                    }
                    
                    // Force layout update
                    view.requestLayout()
                    view.invalidate()
                    gridLayout.requestLayout() // Force grid to re-layout
                    gridLayout.invalidate()
                    
                    Log.d(TAG, "Widget view updated with new size: ${newWidth}x${newHeight}")
                }
            }
        }
    }
    
    /**
     * Remove single item with animation
     */
    fun removeItemAnimated(item: HomeItemModel) {
        Log.d(TAG, "Removing item with animation: ${item.packageName}")
        
        // Find the view for this item
        val view = itemViews[item.id] ?: return
        
        // Remove from tracking immediately to prevent interactions
        itemViews.remove(item.id)
        items.removeAll { it.id == item.id }
        
        // Use hardware acceleration for smoother animation
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        
        // Animate fade out with smooth interpolator
        view.animate()
            .alpha(0f)
            .scaleX(0.9f)  // Less aggressive scale for smoother look
            .scaleY(0.9f)
            .setDuration(250)  // Slightly longer for smoothness
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .withEndAction {
                // Remove from grid
                gridLayout.post {
                    gridLayout.removeView(view)
                    
                    // Return view to pool if it's an app view
                    if (item.type == HomeItemModel.ItemType.APP) {
                        viewPool.recycleAppView(view)
                    }
                    
                    // Reset layer type after animation
                    view.setLayerType(View.LAYER_TYPE_NONE, null)
                    
                    Log.d(TAG, "Item removed: ${item.packageName}")
                }
            }
            .start()
    }
    
    /**
     * Remove item by package name with animation
     */
    fun removePackageAnimated(packageName: String) {
        Log.d(TAG, "Removing package with animation: $packageName")
        
        // Find all items with this package
        val itemsToRemove = items.filter { it.packageName == packageName }.toList()
        
        if (itemsToRemove.isEmpty()) {
            Log.d(TAG, "No items found for package: $packageName")
            return
        }
        
        // If single item, remove immediately
        if (itemsToRemove.size == 1) {
            removeItemAnimated(itemsToRemove[0])
            return
        }
        
        // For multiple items, stagger the animations for smoother effect
        itemsToRemove.forEachIndexed { index, item ->
            gridLayout.postDelayed({
                removeItemAnimated(item)
            }, index * 50L)  // 50ms delay between each animation
        }
    }
    
    /**
     * Add single item to grid with optional animation
     */
    private fun addItemToGrid(item: HomeItemModel, animate: Boolean = false) {
        when (item.type) {
            HomeItemModel.ItemType.APP, HomeItemModel.ItemType.SHORTCUT -> {
                addAppShortcut(item, animate)
            }
            HomeItemModel.ItemType.WIDGET -> {
                addWidget(item, animate)
            }
            HomeItemModel.ItemType.FOLDER -> {
                // Folders are not yet implemented
                Log.w(TAG, "Folder type not yet implemented: ${item.label}")
            }
        }
    }
    
    /**
     * Add item with animation when dropped from drag
     */
    fun addItemAnimated(item: HomeItemModel) {
        // Add to internal list first
        items.add(item)
        // Add to grid with animation
        addItemToGrid(item, animate = true)
    }
    /**
     * Add app shortcut to grid with optional animation
     */
    private fun addAppShortcut(item: HomeItemModel, animate: Boolean = false) {
        // Get view from pool for better performance
        val view = viewPool.obtainAppView()
        
        val iconView = view.findViewById<ImageView>(R.id.appIcon)
        val labelView = view.findViewById<TextView>(R.id.appLabel)
        
        // Get grid configuration for adaptive sizing
        val gridConfig = gridLayout.getGridConfiguration()
        
        // Apply adaptive icon size
        val iconSize = AdaptiveSizeCalculator.calculateIconSize(context, gridConfig)
        val layoutParams = iconView.layoutParams
        layoutParams.width = iconSize
        layoutParams.height = iconSize
        iconView.layoutParams = layoutParams
        
        // Apply adaptive text size
        val textSize = AdaptiveSizeCalculator.calculateTextSize(context, gridConfig)
        labelView.textSize = textSize
        
        // Apply adaptive padding
        val padding = AdaptiveSizeCalculator.calculatePadding(context, gridConfig)
        view.setPadding(padding, padding, padding, padding)
        
        // Configure text visibility and max lines
        labelView.visibility = if (AdaptiveSizeCalculator.shouldShowText(gridConfig)) {
            View.VISIBLE
        } else {
            View.GONE
        }
        labelView.maxLines = AdaptiveSizeCalculator.calculateMaxTextLines(gridConfig)
        
        // Set label
        val displayLabel = item.label ?: item.packageName?.substringAfterLast('.')
        labelView.text = displayLabel
        
        // Debug log for suspicious items
        if (displayLabel == "Открыть" || item.packageName == null || item.componentName == null) {
            Log.w(TAG, "Suspicious item detected: label=$displayLabel, package=${item.packageName}, component=${item.componentName}")
        }
        
        // Load icon with adaptive size
        loadAppIcon(item, iconView)
        
        // Set click listener for launching app
        view.setOnClickListener {
            onItemClick(item)
        }
        
        // Track state for drag/menu detection
        var isLongPressHandled = false
        var isDragStarted = false
        var startX = 0f
        var startY = 0f
        
        view.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    isLongPressHandled = false
                    isDragStarted = false
                    startX = event.rawX
                    startY = event.rawY
                    false // Let other listeners work
                }
                android.view.MotionEvent.ACTION_MOVE -> {
                    if (isLongPressHandled && !isDragStarted) {
                        val deltaX = Math.abs(event.rawX - startX)
                        val deltaY = Math.abs(event.rawY - startY)
                        val slop = android.view.ViewConfiguration.get(context).scaledTouchSlop
                        
                        // If user moved finger after long press, start drag
                        if (deltaX > slop || deltaY > slop) {
                            Log.d(TAG, "Starting drag for item ${item.id} after movement")
                            isDragStarted = true
                            // This will dismiss the menu and start drag
                            val dragStarted = onItemStartDrag?.invoke(v, item) ?: false
                            if (dragStarted) {
                                return@setOnTouchListener true
                            }
                        }
                    }
                    false
                }
                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> {
                    isLongPressHandled = false
                    isDragStarted = false
                    false
                }
                else -> false
            }
        }
        
        // Set long click listener for context menu
        view.setOnLongClickListener { v ->
            Log.d(TAG, "Long click on item ${item.id}")
            isLongPressHandled = true
            
            // Trigger haptic feedback
            v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
            
            // Show context menu (will be dismissed if drag starts)
            onItemLongClick(item, v)
            
            true // Important: consume the event so it doesn't bubble up
        }
        
        // Make sure the view is clickable and long-clickable
        view.isClickable = true
        view.isLongClickable = true
        
        // Add to grid
        if (gridLayout.addItemAtPosition(view, item.cellX, item.cellY, item.spanX, item.spanY)) {
            itemViews[item.id] = view
            view.tag = item
            
            if (animate) {
                // Smooth fade in animation with scale
                view.alpha = 0f
                view.scaleX = 0.8f
                view.scaleY = 0.8f
                view.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .setInterpolator(android.view.animation.DecelerateInterpolator())
                    .start()
            } else {
                // No animation, just show normally
                view.alpha = 1f
                view.scaleX = 1f
                view.scaleY = 1f
            }
        } else {
            Log.e(TAG, "Failed to add item at position ${item.cellX}, ${item.cellY}")
        }
    }
    
    /**
     * Add a widget to the grid with optional animation
     */
    private fun addWidget(item: HomeItemModel, animate: Boolean = false) {
        Log.d(TAG, "Adding widget ${item.id} at position ${item.cellX}, ${item.cellY} with span ${item.spanX}x${item.spanY}")
        
        // Create widget view based on type
        val widget = when {
            // Custom clock widget
            item.componentName == "clock" || item.componentName == "com.customlauncher.widget.clock" -> {
                ClockWidget(context).apply {
                    // Set initial size
                    updateSize(item.spanX, item.spanY)
                    // Ensure proper layout on first display
                    post {
                        updateSize(item.spanX, item.spanY)
                    }
                }
            }
            // System widget
            item.widgetId != null && item.widgetId > 0 && appWidgetHost != null && appWidgetManager != null -> {
                try {
                    val appWidgetInfo = appWidgetManager.getAppWidgetInfo(item.widgetId)
                    if (appWidgetInfo != null) {
                        // Create host view for the widget
                        val hostView = appWidgetHost.createView(context, item.widgetId, appWidgetInfo)
                        
                        // Calculate actual widget size based on grid
                        val displayMetrics = context.resources.displayMetrics
                        val screenWidth = displayMetrics.widthPixels
                        val screenHeight = displayMetrics.heightPixels
                        
                        // Get grid configuration
                        val gridColumns = gridLayout.getGridConfiguration().columns
                        val gridRows = gridLayout.getGridConfiguration().rows
                        
                        // Calculate cell dimensions
                        val cellWidth = screenWidth / gridColumns
                        val cellHeight = screenHeight / gridRows
                        val padding = (4 * displayMetrics.density).toInt() // 4dp padding
                        
                        // Widget dimensions with padding
                        val widgetWidth = (cellWidth * item.spanX) - (padding * 2)
                        val widgetHeight = (cellHeight * item.spanY) - (padding * 2)
                        
                        hostView.apply {
                            // Set exact dimensions for the widget
                            setAppWidget(item.widgetId, appWidgetInfo)
                            
                            // Important: Set layout params first
                            layoutParams = HomeScreenGridLayout.CellLayoutParams(
                                item.cellX, item.cellY,
                                item.spanX, item.spanY
                            )
                            
                                // Update widget with size options
                            val options = android.os.Bundle()
                            val widgetWidthDp = (widgetWidth / displayMetrics.density).toInt()
                            val widgetHeightDp = (widgetHeight / displayMetrics.density).toInt()
                            
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, widgetWidthDp)
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, widgetHeightDp)
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, widgetWidthDp)
                            options.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, widgetHeightDp)
                            
                            // Update widget options
                            updateAppWidgetOptions(options)
                            
                            // Force measure and layout for proper rendering
                            measure(
                                android.view.View.MeasureSpec.makeMeasureSpec(widgetWidth, android.view.View.MeasureSpec.EXACTLY),
                                android.view.View.MeasureSpec.makeMeasureSpec(widgetHeight, android.view.View.MeasureSpec.EXACTLY)
                            )
                            layout(0, 0, widgetWidth, widgetHeight)
                        }
                        hostView
                    } else {
                        Log.w(TAG, "AppWidgetInfo not found for widgetId: ${item.widgetId}")
                        return
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error creating widget host view for widgetId: ${item.widgetId}", e)
                    return
                }
            }
            else -> {
                Log.w(TAG, "Unsupported widget type: ${item.componentName}")
                return
            }
        }
        
        // For AppWidgetHostView, we need special handling
        if (widget is AppWidgetHostView) {
            // Create a wrapper view for handling long press on system widgets
            val wrapper = FrameLayout(context).apply {
                layoutParams = HomeScreenGridLayout.CellLayoutParams(
                    item.cellX, item.cellY,
                    item.spanX, item.spanY
                )
                
                // Add widget to wrapper
                addView(widget, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ))
                
                // For some widgets, we need to allow interaction
                // But still handle long press on wrapper
                if (item.componentName?.contains("clock", ignoreCase = true) == true ||
                    item.componentName?.contains("chrome", ignoreCase = true) == true) {
                    // Allow widget to be interactive for clicks
                    widget.isClickable = true
                    widget.isLongClickable = false // Wrapper handles long press
                } else {
                    // Other widgets - wrapper handles all events
                    widget.isClickable = false
                    widget.isLongClickable = false
                    widget.isFocusable = false
                }
                
                // Track state for drag/menu detection
                var isLongPressHandled = false
                var isDragStarted = false
                var startX = 0f
                var startY = 0f
                var longPressHandler: Runnable? = null
                
                setOnTouchListener { v, event ->
                    when (event.action) {
                        android.view.MotionEvent.ACTION_DOWN -> {
                            isLongPressHandled = false
                            isDragStarted = false
                            startX = event.rawX
                            startY = event.rawY
                            
                            // Cancel any previous long press handler
                            longPressHandler?.let { v.removeCallbacks(it) }
                            
                            // Schedule long press detection
                            longPressHandler = Runnable {
                                if (!isDragStarted) {
                                    Log.d(TAG, "Long press detected on system widget ${item.id}")
                                    isLongPressHandled = true
                                    v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                                    onItemLongClick(item, v)
                                }
                            }
                            v.postDelayed(longPressHandler, android.view.ViewConfiguration.getLongPressTimeout().toLong())
                            
                            // Pass event to widget for interaction
                            widget.dispatchTouchEvent(event)
                            true // Consume to prevent widget from handling directly
                        }
                        android.view.MotionEvent.ACTION_MOVE -> {
                            val deltaX = Math.abs(event.rawX - startX)
                            val deltaY = Math.abs(event.rawY - startY)
                            val slop = android.view.ViewConfiguration.get(context).scaledTouchSlop
                            
                            // Cancel long press if moved too much
                            if (deltaX > slop || deltaY > slop) {
                                longPressHandler?.let {
                                    v.removeCallbacks(it)
                                    longPressHandler = null
                                }
                                
                                if (isLongPressHandled && !isDragStarted) {
                                    Log.d(TAG, "Starting drag for widget ${item.id} after movement")
                                    isDragStarted = true
                                    val dragStarted = onItemStartDrag?.invoke(v, item) ?: false
                                    if (dragStarted) {
                                        return@setOnTouchListener true
                                    }
                                }
                            }
                            
                            // Pass event to widget
                            widget.dispatchTouchEvent(event)
                            true
                        }
                        android.view.MotionEvent.ACTION_UP,
                        android.view.MotionEvent.ACTION_CANCEL -> {
                            // Cancel long press handler
                            longPressHandler?.let {
                                v.removeCallbacks(it)
                                longPressHandler = null
                            }
                            
                            isLongPressHandled = false
                            isDragStarted = false
                            
                            // Pass event to widget
                            widget.dispatchTouchEvent(event)
                            true
                        }
                        else -> {
                            widget.dispatchTouchEvent(event)
                            true
                        }
                    }
                }
                
                // Set the wrapper as clickable
                isClickable = true
                isFocusableInTouchMode = false
            }
            
            // Add wrapper to grid instead of widget directly
            if (gridLayout.addItemAtPosition(wrapper, item.cellX, item.cellY, item.spanX, item.spanY)) {
                itemViews[item.id] = wrapper
                wrapper.tag = item
                Log.d(TAG, "Added system widget with wrapper at ${item.cellX}, ${item.cellY} with span ${item.spanX}x${item.spanY}")
                
                if (animate) {
                    // Smooth fade in animation with scale
                    wrapper.alpha = 0f
                    wrapper.scaleX = 0.8f
                    wrapper.scaleY = 0.8f
                    wrapper.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(350)
                        .setInterpolator(android.view.animation.DecelerateInterpolator())
                        .start()
                } else {
                    // No animation, just show normally
                    wrapper.alpha = 1f
                    wrapper.scaleX = 1f
                    wrapper.scaleY = 1f
                }
            }
            
        } else {
            // For custom widgets (like ClockWidget)
            // Track state for drag/menu detection on widgets
            var isLongPressHandled = false
            var isDragStarted = false
            var startX = 0f
            var startY = 0f
            
            widget.setOnTouchListener { v, event ->
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        isLongPressHandled = false
                        isDragStarted = false
                        startX = event.rawX
                        startY = event.rawY
                        false // Let other listeners work
                    }
                    android.view.MotionEvent.ACTION_MOVE -> {
                        if (isLongPressHandled && !isDragStarted) {
                            val deltaX = Math.abs(event.rawX - startX)
                            val deltaY = Math.abs(event.rawY - startY)
                            val slop = android.view.ViewConfiguration.get(context).scaledTouchSlop
                            
                            // If user moved finger after long press, start drag
                            if (deltaX > slop || deltaY > slop) {
                                Log.d(TAG, "Starting drag for widget ${item.id} after movement")
                                isDragStarted = true
                                // This will dismiss the menu and start drag
                                val dragStarted = onItemStartDrag?.invoke(v, item) ?: false
                                if (dragStarted) {
                                    return@setOnTouchListener true
                                }
                            }
                        }
                        false
                    }
                    android.view.MotionEvent.ACTION_UP,
                    android.view.MotionEvent.ACTION_CANCEL -> {
                        isLongPressHandled = false
                        isDragStarted = false
                        false
                    }
                    else -> false
                }
            }
            
            // Set long click listener for context menu on widgets
            widget.setOnLongClickListener { v ->
                Log.d(TAG, "Long click on widget ${item.id}")
                isLongPressHandled = true
                
                // Trigger haptic feedback
                v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                
                // Show context menu (will be dismissed if drag starts)
                onItemLongClick(item, v)
                
                true // Important: consume the event so it doesn't bubble up
            }
            
            // Make sure the widget is clickable and long-clickable
            widget.isClickable = true
            widget.isLongClickable = true
            
            // Add to grid with proper span
            // Clock widget uses its actual span, system widgets need at least 2x1
            val spanX = if (item.componentName == "clock" || item.componentName == "com.customlauncher.widget.clock") {
                item.spanX
            } else {
                item.spanX.coerceAtLeast(2)
            }
            val spanY = item.spanY.coerceAtLeast(1)
            
            if (gridLayout.addItemAtPosition(widget, item.cellX, item.cellY, spanX, spanY)) {
                itemViews[item.id] = widget
                widget.tag = item
                Log.d(TAG, "Added custom widget at ${item.cellX}, ${item.cellY} with span ${spanX}x${spanY}")
                
                if (animate) {
                    // Smooth fade in animation with scale
                    widget.alpha = 0f
                    widget.scaleX = 0.8f
                    widget.scaleY = 0.8f
                    widget.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(350)
                        .setInterpolator(android.view.animation.DecelerateInterpolator())
                        .start()
                } else {
                    // No animation, just show normally
                    widget.alpha = 1f
                    widget.scaleX = 1f
                    widget.scaleY = 1f
                }
            } else {
                Log.e(TAG, "Failed to add widget at position ${item.cellX}, ${item.cellY}")
            }
        }
    }
    
    /**
     * Load app icon
     */
    private fun loadAppIcon(item: HomeItemModel, iconView: ImageView) {
        // Use R.id.appIcon as key for storing package info
        val currentPackage = iconView.getTag(R.id.appIcon) as? String
        val newPackage = "${item.packageName}_${item.componentName}"
        
        // If the same icon is already loaded, don't reload
        if (currentPackage == newPackage && iconView.drawable != null) {
            return
        }
        
        // IMPORTANT: Clear any existing icon first to prevent showing old/system icons
        iconView.setImageDrawable(null)
        iconView.setTag(R.id.appIcon, null)
        
        // Load real icon
        adapterScope.launch {
            val icon = withContext(Dispatchers.IO) {
                try {
                    item.componentName?.let { componentName ->
                        val parts = componentName.split("/")
                        if (parts.size == 2) {
                            val componentNameObj = android.content.ComponentName(parts[0], parts[1])
                            IconCache.loadIcon(
                                context,
                                item.packageName ?: parts[0],
                                packageManager,
                                componentNameObj
                            )
                        } else {
                            loadIconByPackageName(item.packageName)
                        }
                    } ?: loadIconByPackageName(item.packageName)
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading icon for ${item.packageName}", e)
                    null
                }
            }
            
            // Only set icon if we successfully loaded it
            // If icon is null, the ImageView will remain empty (no placeholder)
            if (icon != null) {
                iconView.setImageDrawable(icon)
                iconView.setTag(R.id.appIcon, newPackage)
            } else {
                // Ensure the view stays empty if no icon was found
                Log.w(TAG, "No icon found for ${item.packageName}, keeping empty")
                iconView.setImageDrawable(null)
                iconView.setTag(R.id.appIcon, null)
            }
        }
    }
    
    /**
     * Load icon by package name
     */
    private fun loadIconByPackageName(packageName: String?): Drawable? {
        return packageName?.let {
            try {
                packageManager.getApplicationIcon(it)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }
    
    /**
     * Remove item from grid
     */
    fun removeItem(itemId: Long) {
        itemViews[itemId]?.let { view ->
            val item = view.tag as? HomeItemModel
            item?.let {
                gridLayout.removeItemAt(it.cellX, it.cellY)
            }
            itemViews.remove(itemId)
        }
        
        items.removeAll { it.id == itemId }
    }
    
    /**
     * Clear all views from grid and recycle them to pool
     */
    fun clearAllViews() {
        // Recycle views to pool for reuse
        itemViews.forEach { (itemId, view) ->
            val item = items.find { it.id == itemId }
            if (item?.type == HomeItemModel.ItemType.APP) {
                viewPool.recycleAppView(view)
            } else if (item?.type == HomeItemModel.ItemType.WIDGET && view is FrameLayout) {
                viewPool.recycleWidgetContainer(view)
            }
        }
        
        gridLayout.removeAllViews()
        itemViews.clear()
        
        // Clear lazy loaded widgets
        lazyWidgetLoader?.clearAll()
    }
    
    /**
     * Get item at specific grid position (public)
     */
    fun getItemAtPosition(x: Int, y: Int): HomeItemModel? {
        val foundItem = items.find { item ->
            item.cellX == x && item.cellY == y
        }
        Log.d(TAG, "getItemAtPosition($x, $y): found item = ${foundItem?.id} (${foundItem?.packageName})")
        return foundItem
    }
    
    /**
     * Find item at specific grid position (private)
     */
    private fun findItemAtPosition(x: Int, y: Int): HomeItemModel? {
        return getItemAtPosition(x, y)
    }
    
    /**
     * Remove item at specific grid position
     */
    private fun removeItemAtPosition(x: Int, y: Int) {
        val itemAtPosition = findItemAtPosition(x, y)
        if (itemAtPosition != null) {
            Log.d(TAG, "Removing existing item at $x,$y: ${itemAtPosition.packageName}")
            
            // Remove view from grid - use removeItemAt to properly clear occupied cells
            gridLayout.removeItemAt(x, y)
            
            // Remove from tracking
            val viewToRemove = itemViews[itemAtPosition.id]
            if (viewToRemove != null) {
                itemViews.remove(itemAtPosition.id)
                
                // Return view to pool if it's an app
                if (itemAtPosition.type == HomeItemModel.ItemType.APP) {
                    viewPool.recycleAppView(viewToRemove)
                }
            }
            
            // Remove from items list
            items.removeAll { it.id == itemAtPosition.id }
        }
    }
    
    /**
     * Move item to new position
     */
    fun moveItem(itemId: Long, newX: Int, newY: Int): Boolean {
        Log.d(TAG, "moveItem called: itemId=$itemId to position $newX,$newY")
        
        val view = itemViews[itemId]
        if (view == null) {
            Log.e(TAG, "moveItem: view not found for itemId=$itemId")
            return false
        }
        
        val item = view.tag as? HomeItemModel
        if (item == null) {
            Log.e(TAG, "moveItem: item not found in view tag for itemId=$itemId")
            return false
        }
        
        Log.d(TAG, "moveItem: current item position is ${item.cellX},${item.cellY}")
        
        // Don't move if it's the same position
        if (item.cellX == newX && item.cellY == newY) {
            Log.d(TAG, "Item already at position $newX,$newY, skipping move")
            return true
        }
        
        // First, check if there's an item at the new position and remove it
        removeItemAtPosition(newX, newY)
        
        // Remove from old position
        gridLayout.removeItemAt(item.cellX, item.cellY)
        
        // Try to add at new position
        val updatedItem = item.copy(cellX = newX, cellY = newY)
        if (gridLayout.addItemAtPosition(view, newX, newY, item.spanX, item.spanY)) {
            view.tag = updatedItem
            
            // Update item in list
            val index = items.indexOfFirst { it.id == itemId }
            if (index >= 0) {
                items[index] = updatedItem
            }
            
            // Reset view alpha in case it was changed during drag
            view.alpha = 1.0f
            
            Log.d(TAG, "Moved item ${itemId} from ${item.cellX},${item.cellY} to $newX,$newY")
            return true
        } else {
            // Failed to add at new position, restore at old position
            gridLayout.addItemAtPosition(view, item.cellX, item.cellY, item.spanX, item.spanY)
            view.alpha = 1.0f
            Log.e(TAG, "Failed to move item ${itemId} to $newX,$newY")
            return false
        }
    }
    
    /**
     * Set the focused position and update visual state
     */
    fun setFocusedPosition(position: Int) {
        val oldPosition = focusedPosition
        focusedPosition = position
        
        // Update visual state of old focused item
        if (oldPosition >= 0 && oldPosition < items.size) {
            val oldItem = items[oldPosition]
            itemViews[oldItem.id]?.let { view ->
                // Remove focus highlight
                view.isSelected = false
                if (isButtonMode) {
                    // Animate focus out
                    val fadeOut = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.focus_scale_out)
                    view.startAnimation(fadeOut)
                    // Remove focus background
                    view.setBackgroundResource(R.drawable.bg_item_normal)
                } else {
                    // Don't set background in touch mode to avoid focus highlight
                    view.background = null
                }
            }
        }
        
        // Update visual state of new focused item
        if (position >= 0 && position < items.size) {
            val newItem = items[position]
            itemViews[newItem.id]?.let { view ->
                // Add focus highlight
                view.isSelected = true
                if (isButtonMode) {
                    // Apply focus background
                    view.setBackgroundResource(R.drawable.bg_item_selector_button_mode)
                    // Animate focus in
                    val fadeIn = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.focus_scale_in)
                    view.startAnimation(fadeIn)
                    // Ensure the view maintains its scale after animation
                    view.scaleX = 1.05f
                    view.scaleY = 1.05f
                }
                // Request focus on the view
                view.requestFocus()
            }
        }
        
        Log.d(TAG, "Focus changed from $oldPosition to $position in ${if (isButtonMode) "button" else "touch"} mode")
    }
    
    /**
     * Clear focus from all items
     */
    fun clearFocus() {
        // Clear visual state from all items
        if (isButtonMode) {
            items.forEachIndexed { index, item ->
                itemViews[item.id]?.let { view ->
                    view.isSelected = false
                    view.setBackgroundResource(R.drawable.bg_item_normal)
                    view.scaleX = 1.0f
                    view.scaleY = 1.0f
                }
            }
        }
        focusedPosition = -1
    }
    
    /**
     * Set button mode state
     */
    fun setButtonMode(enabled: Boolean) {
        if (isButtonMode != enabled) {
            isButtonMode = enabled
            Log.d(TAG, "Button mode ${if (enabled) "enabled" else "disabled"}")
            
            // If disabling button mode, clear all focus highlighting
            if (!enabled) {
                clearFocus()
            }
        }
    }
    
    /**
     * Update visibility of items based on hidden mode
     */
    fun updateHiddenMode(isHidden: Boolean) {
        adapterScope.launch {
            // Get list of hidden apps from database
            val hiddenAppsDao = com.customlauncher.app.LauncherApplication.instance.database.hiddenAppDao()
            
            // Create a copy of the map to avoid ConcurrentModificationException
            val itemViewsCopy = itemViews.toMap()
            
            itemViewsCopy.forEach { (id, view) ->
                val item = items.find { it.id == id }
                if (item != null && item.type == HomeItemModel.ItemType.APP) {
                    // Check if this app is in the hidden apps list
                    val isAppHidden = item.packageName?.let { packageName ->
                        withContext(Dispatchers.IO) {
                            hiddenAppsDao.isAppHidden(packageName)
                        }
                    } ?: false
                    
                    withContext(Dispatchers.Main) {
                        // Check if view is still in the current map
                        if (itemViews.containsKey(id)) {
                            // Hide the view if in hidden mode and app is marked as hidden
                            if (isHidden && isAppHidden) {
                                view.visibility = View.INVISIBLE
                                Log.d(TAG, "Hiding app ${item.packageName} in hidden mode")
                            } else {
                                view.visibility = View.VISIBLE
                                Log.d(TAG, "Showing app ${item.packageName}, hidden=$isHidden, isAppHidden=$isAppHidden")
                            }
                        }
                    }
                } else {
                    // Widgets and other items are always visible
                    withContext(Dispatchers.Main) {
                        // Check if view is still in the current map
                        if (itemViews.containsKey(id)) {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Clean up resources
     */
    fun onDestroy() {
        clearAllViews()
        viewPool.clear()
        lazyWidgetLoader?.destroy()
        adapterScope.cancel()
        Log.d(TAG, "Adapter destroyed, performance report: ${PerformanceMonitor.getPerformanceReport()}")
    }
}

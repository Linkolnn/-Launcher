package com.customlauncher.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.R
import com.customlauncher.app.data.model.AppInfo
import com.customlauncher.app.utils.IconCache
import com.customlauncher.app.utils.AdaptiveSizeCalculator
import com.customlauncher.app.data.model.GridConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import android.util.Log
import android.widget.PopupWindow
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.graphics.Color
import android.os.Build
import com.customlauncher.app.LauncherApplication
import android.content.ClipData
import android.content.ClipDescription

class AppGridAdapter(
    private val onAppClick: (AppInfo) -> Unit,
    private val onAppLongClick: ((AppInfo, View) -> Boolean)? = null,
    private val isDragEnabled: Boolean = false,
    private val onDragStarted: (() -> Unit)? = null
) : ListAdapter<AppInfo, AppGridAdapter.ViewHolder>(AppDiffCallback()) {
    
    // Single scope for all icon loading tasks with supervisor job
    private val adapterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    // Full list of apps for filtering
    private var fullAppsList = listOf<AppInfo>()
    private var currentFilter = ""
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_grid, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            Log.e("AppGridAdapter", "Error binding item at position $position", e)
        }
    }
    
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.cancelIconLoad()
    }
    
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        // Cancel all coroutines when adapter is detached
        adapterScope.cancel()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        private val appName: TextView = itemView.findViewById(R.id.appName)
        private var iconLoadJob: Job? = null
        
        fun bind(app: AppInfo) {
            val context = itemView.context
            val preferences = LauncherApplication.instance.preferences
            
            // Get grid configuration from preferences
            // Use button grid if selected in menu settings
            val useButtonGrid = preferences.hasButtonGridSelection && preferences.buttonPhoneGridSize.isNotEmpty()
            
            val gridConfig = if (useButtonGrid) {
                // For button phones menu, use the button phone grid settings
                val gridSize = preferences.buttonPhoneGridSize
                val (cols, rows) = when (gridSize) {
                    "3x3" -> 3 to 3
                    "3x4" -> 3 to 4
                    "3x5" -> 3 to 5
                    "4x5" -> 4 to 5
                    else -> 4 to 5
                }
                GridConfiguration(cols, rows, preferences.buttonPhoneMode)
            } else {
                // For touch phones, use app grid columns
                val columns = preferences.gridColumnCount.takeIf { it > 0 } ?: 4
                
                // Calculate optimal rows for the app drawer based on columns
                val rows = when (columns) {
                    3 -> 5  // For 3 columns, use 5 rows for better icon size balance
                    4 -> 6  // For 4 columns, use 6 rows
                    5 -> 7  // For 5 columns, use 7 rows
                    else -> {
                        // For other column counts, calculate based on screen
                        val displayMetrics = context.resources.displayMetrics
                        val screenHeight = displayMetrics.heightPixels
                        val estimatedItemHeight = screenHeight / 7
                        (screenHeight / estimatedItemHeight).coerceIn(5, 8)
                    }
                }
                
                GridConfiguration(
                    columns = columns,
                    rows = rows,
                    isButtonMode = false
                )
            }
            
            // Apply adaptive icon size
            val iconSize = AdaptiveSizeCalculator.calculateIconSize(context, gridConfig)
            val layoutParams = appIcon.layoutParams
            layoutParams.width = iconSize
            layoutParams.height = iconSize
            appIcon.layoutParams = layoutParams
            
            // Apply adaptive padding
            val padding = AdaptiveSizeCalculator.calculatePadding(context, gridConfig)
            itemView.setPadding(padding, padding, padding, padding)
            
            // Apply adaptive text size and visibility
            if (preferences.showAppLabels && AdaptiveSizeCalculator.shouldShowText(gridConfig)) {
                appName.text = app.appName
                appName.visibility = View.VISIBLE
                
                // Apply adaptive text size
                val textSize = AdaptiveSizeCalculator.calculateTextSize(context, gridConfig)
                appName.textSize = textSize
                appName.maxLines = AdaptiveSizeCalculator.calculateMaxTextLines(gridConfig)
            } else {
                appName.visibility = View.GONE
            }
            
            // Set placeholder immediately
            appIcon.setImageDrawable(app.icon)
            
            // Cancel previous icon load
            cancelIconLoad()
            
            // Load real icon asynchronously using adapter scope
            iconLoadJob = adapterScope.launch {
                try {
                    val icon = withContext(Dispatchers.IO) {
                        // Create ComponentName for icon pack support
                        val componentName = itemView.context.packageManager
                            .getLaunchIntentForPackage(app.packageName)?.component
                        
                        IconCache.loadIcon(
                            itemView.context,
                            app.packageName,
                            itemView.context.packageManager,
                            componentName
                        )
                    }
                    // Check if job is still active before updating UI
                    if (iconLoadJob?.isActive == true) {
                        appIcon.setImageDrawable(icon)
                    }
                } catch (e: Exception) {
                    Log.d("AppGridAdapter", "Error loading icon for ${app.packageName}")
                    // Use placeholder on error - already set
                }
            }
            
            itemView.setOnClickListener {
                onAppClick(app)
            }
            
            // Setup touch handling for both context menu and drag
            var isLongPressed = false
            var isDragging = false
            var startX = 0f
            var startY = 0f
            var menuRunnable: Runnable? = null
            
            // Add drag detection only if drag is enabled
            if (isDragEnabled) {
                itemView.setOnTouchListener { view, event ->
                    when (event.action) {
                        android.view.MotionEvent.ACTION_DOWN -> {
                            startX = event.rawX
                            startY = event.rawY
                            isLongPressed = false
                            isDragging = false
                            false // Let the click/long click handlers work
                        }
                        android.view.MotionEvent.ACTION_MOVE -> {
                            if (!isDragging && isLongPressed) {
                                val deltaX = kotlin.math.abs(event.rawX - startX)
                                val deltaY = kotlin.math.abs(event.rawY - startY)
                                
                                // Use larger threshold to prevent accidental drags
                                val dragThreshold = 20f // Increased from 5 to 20 pixels
                                
                                // Start drag only if moved significantly
                                if (deltaX > dragThreshold || deltaY > dragThreshold) {
                                    // Cancel the menu if it was scheduled
                                    menuRunnable?.let {
                                        view.removeCallbacks(it)
                                        menuRunnable = null
                                    }
                                    
                                    // Already disabled scrolling in long click, keep it disabled
                                    view.parent?.requestDisallowInterceptTouchEvent(true)
                                    
                                    isDragging = true
                                    startDragForApp(view, app)
                                    return@setOnTouchListener true
                                }
                            }
                            
                            // Keep scrolling disabled during long press
                            if (isLongPressed) {
                                view.parent?.requestDisallowInterceptTouchEvent(true)
                                return@setOnTouchListener true
                            }
                            
                            false
                        }
                        android.view.MotionEvent.ACTION_UP,
                        android.view.MotionEvent.ACTION_CANCEL -> {
                            // Re-enable parent scrolling
                            view.parent?.requestDisallowInterceptTouchEvent(false)
                            
                            // Clean up
                            menuRunnable?.let {
                                view.removeCallbacks(it)
                                menuRunnable = null
                            }
                            isLongPressed = false
                            isDragging = false
                            false
                        }
                        else -> false
                    }
                }
            }
            
            // Handle long click for context menu
            itemView.setOnLongClickListener { view ->
                // Trigger haptic feedback immediately
                view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                
                if (isDragEnabled) {
                    // If drag is enabled, set flag and wait for movement
                    isLongPressed = true
                    
                    // Immediately prevent parent from intercepting (stops scrolling on long press)
                    view.parent?.requestDisallowInterceptTouchEvent(true)
                    
                    // Show context menu after a delay if not dragging
                    menuRunnable = Runnable {
                        if (isLongPressed && !isDragging) {
                            // Re-enable scrolling if showing menu (no drag started)
                            view.parent?.requestDisallowInterceptTouchEvent(false)
                            onAppLongClick?.invoke(app, view)
                        }
                    }
                    view.postDelayed(menuRunnable!!, 100) // Wait 100ms to see if drag starts
                } else {
                    // If drag is NOT enabled, show context menu immediately
                    onAppLongClick?.invoke(app, view)
                }
                
                true
            }
        }
        
        fun cancelIconLoad() {
            iconLoadJob?.cancel()
        }
        
        private fun startDragForApp(view: View, app: AppInfo) {
            // Create clip data with app information
            val clipData = ClipData.newPlainText(
                "app_from_drawer",
                "${app.packageName}|${app.appName}"
            )
            
            // Create drag shadow
            val shadowBuilder = View.DragShadowBuilder(view)
            
            // Make view semi-transparent during drag
            view.alpha = 0.5f
            
            // Start drag operation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(clipData, shadowBuilder, app, View.DRAG_FLAG_GLOBAL)
            } else {
                @Suppress("DEPRECATION")
                view.startDrag(clipData, shadowBuilder, app, 0)
            }
            
            // Notify that drag has started - this will close the app drawer
            onDragStarted?.invoke()
            
            // Restore opacity after a short delay
            view.postDelayed({ view.alpha = 1.0f }, 100)
        }
    }
    
    // Override submitList to store full list
    override fun submitList(list: List<AppInfo>?) {
        fullAppsList = list ?: emptyList()
        applyFilter()
    }
    
    // Get item at specific position
    fun getItemAt(position: Int): AppInfo? {
        return if (position >= 0 && position < itemCount) {
            getItem(position)
        } else {
            null
        }
    }
    
    // Filter method for search
    fun filter(query: String) {
        currentFilter = query
        applyFilter()
    }
    
    // Apply filter to the list
    fun applyFilter() {
        val filteredList = if (currentFilter.isEmpty()) {
            fullAppsList
        } else {
            fullAppsList.filter { app ->
                app.appName.contains(currentFilter, ignoreCase = true) ||
                app.packageName.contains(currentFilter, ignoreCase = true)
            }
        }
        super.submitList(filteredList)
    }
    
    private class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }
        
        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}

package com.customlauncher.app.ui.dialog

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WidgetPickerBottomSheet(
    private val onWidgetSelected: (AppWidgetProviderInfo) -> Unit
) : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "WidgetPickerBottomSheet"
    }

    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_widget_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        appWidgetManager = AppWidgetManager.getInstance(requireContext())
        
        view.findViewById<TextView>(R.id.titleText).text = "Виджеты"
        
        recyclerView = view.findViewById(R.id.widgetRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        loadWidgets()
    }

    private fun loadWidgets() {
        Log.d(TAG, "Loading widgets...")
        val widgetProviders = appWidgetManager.installedProviders
        Log.d(TAG, "Found ${widgetProviders.size} system widget providers")
        
        // Group widgets by package
        val groupedWidgets = widgetProviders.groupBy { it.provider.packageName }
        
        // Create list with our custom widgets first
        val widgetItems = mutableListOf<WidgetItem>()
        
        // Add custom clock widget
        widgetItems.add(WidgetItem(
            label = "Часы ИLauncher",
            packageName = "com.customlauncher.app",
            icon = null,
            preview = null,
            providerInfo = null,
            isCustom = true
        ))
        Log.d(TAG, "Added custom clock widget")
        
        // Add system widgets grouped by app
        groupedWidgets.forEach { (packageName, providers) ->
            providers.forEach { providerInfo ->
                try {
                    val label = providerInfo.loadLabel(requireContext().packageManager)
                    val icon = providerInfo.loadIcon(requireContext(), requireContext().resources.displayMetrics.densityDpi)
                    val previewImage = providerInfo.loadPreviewImage(requireContext(), requireContext().resources.displayMetrics.densityDpi)
                    
                    widgetItems.add(WidgetItem(
                        label = label,
                        packageName = packageName,
                        icon = icon,
                        preview = previewImage,
                        providerInfo = providerInfo,
                        isCustom = false
                    ))
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading widget info for $packageName", e)
                }
            }
        }
        
        recyclerView.adapter = WidgetAdapter(widgetItems) { widgetItem ->
            if (widgetItem.isCustom) {
                // Handle custom clock widget - show size selection dialog
                Log.d(TAG, "Clock widget selected, showing size dialog")
                val activity = activity as? com.customlauncher.app.ui.HomeScreenActivity
                if (activity != null) {
                    val sizeDialog = ClockWidgetSizeDialog.newInstance(
                        object : ClockWidgetSizeDialog.OnSizeSelectedListener {
                            override fun onSizeSelected(spanX: Int, spanY: Int) {
                                Log.d(TAG, "Clock widget size selected: ${spanX}x${spanY}")
                                activity.addClockWidget(spanX, spanY)
                            }
                        }
                    )
                    dismiss() // Dismiss bottom sheet first
                    sizeDialog.show(activity.supportFragmentManager, "ClockWidgetSizeDialog")
                } else {
                    Log.e(TAG, "Activity is not HomeScreenActivity")
                }
            } else {
                // Handle system widget - show notification that it's not available yet
                Log.d(TAG, "System widget clicked, showing unavailable message")
                android.widget.Toast.makeText(
                    activity,
                    "Пока нельзя использовать сторонние виджеты. Будет добавлено в следующих обновлениях",
                    android.widget.Toast.LENGTH_LONG
                ).show()
                // Don't dismiss the dialog so user can continue browsing
            }
        }
    }
    
    data class WidgetItem(
        val label: String,
        val packageName: String,
        val icon: Drawable?,
        val preview: Drawable?,
        val providerInfo: AppWidgetProviderInfo?,
        val isCustom: Boolean
    )
    
    inner class WidgetAdapter(
        private val items: List<WidgetItem>,
        private val onItemClick: (WidgetItem) -> Unit
    ) : RecyclerView.Adapter<WidgetAdapter.ViewHolder>() {
        
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val previewImage: ImageView = view.findViewById(R.id.widgetPreview)
            val iconImage: ImageView = view.findViewById(R.id.widgetIcon)
            val nameText: TextView = view.findViewById(R.id.widgetName)
            val packageText: TextView = view.findViewById(R.id.widgetPackage)
            val sizeText: TextView = view.findViewById(R.id.widgetSize)
            
            init {
                view.setOnClickListener {
                    items.getOrNull(adapterPosition)?.let {
                        onItemClick(it)
                    }
                }
            }
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_widget_preview, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val widget = items[position]
            
            holder.nameText.text = widget.label
            holder.packageText.text = widget.packageName
            
            // Make system widgets look disabled
            if (!widget.isCustom) {
                holder.itemView.alpha = 0.5f  // Semi-transparent to indicate unavailability
                holder.nameText.text = "${widget.label} 🔒"  // Add lock icon
            } else {
                holder.itemView.alpha = 1.0f
            }
            
            // Show preview if available, otherwise show icon
            if (widget.preview != null) {
                holder.previewImage.visibility = View.VISIBLE
                holder.iconImage.visibility = View.GONE
                holder.previewImage.setImageDrawable(widget.preview)
            } else if (widget.icon != null) {
                holder.previewImage.visibility = View.GONE
                holder.iconImage.visibility = View.VISIBLE
                holder.iconImage.setImageDrawable(widget.icon)
            } else {
                // Use default icon for custom widgets
                holder.previewImage.visibility = View.GONE
                holder.iconImage.visibility = View.VISIBLE
                holder.iconImage.setImageResource(if (widget.isCustom) R.drawable.ic_clock else R.drawable.ic_widget)
            }
            
            // Show widget size (except for clock widget which has size selection dialog)
            if (widget.isCustom && widget.label == "Часы ИLauncher") {
                // Hide size for clock widget as it's selected via dialog
                holder.sizeText.visibility = View.GONE
            } else {
                widget.providerInfo?.let { info ->
                    // Get screen dimensions
                    val displayMetrics = holder.itemView.context.resources.displayMetrics
                    val screenWidth = displayMetrics.widthPixels
                    
                    // Assume 4 columns as default (can be adjusted based on actual grid)
                    val cellSizeDp = screenWidth / 4 / displayMetrics.density
                    
                    // Calculate cells needed based on widget min size
                    val cellsX = kotlin.math.max(1, kotlin.math.ceil(info.minWidth / cellSizeDp).toInt())
                    val cellsY = kotlin.math.max(1, kotlin.math.ceil(info.minHeight / cellSizeDp).toInt())
                    
                    holder.sizeText.text = "${cellsX}x${cellsY}"
                    holder.sizeText.visibility = View.VISIBLE
                } ?: run {
                    // For other custom widgets
                    if (widget.isCustom) {
                        holder.sizeText.text = "2x1"
                        holder.sizeText.visibility = View.VISIBLE
                    } else {
                        holder.sizeText.visibility = View.GONE
                    }
                }
            }
        }
        
        override fun getItemCount() = items.size
    }
}

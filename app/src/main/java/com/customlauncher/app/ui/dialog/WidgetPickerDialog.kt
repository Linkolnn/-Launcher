package com.customlauncher.app.ui.dialog

import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.R

class WidgetPickerDialog(
    context: Context,
    private val appWidgetManager: AppWidgetManager,
    private val onWidgetSelected: (AppWidgetProviderInfo) -> Unit
) : Dialog(context) {

    companion object {
        private const val TAG = "WidgetPickerDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#212121")))
        
        setContentView(R.layout.dialog_widget_picker)
        
        setupViews()
        loadWidgets()
    }
    
    private fun setupViews() {
        findViewById<TextView>(R.id.titleText).text = "Выберите виджет"
        
        findViewById<Button>(R.id.cancelButton).apply {
            text = "ОТМЕНА"
            setOnClickListener {
                dismiss()
            }
        }
    }
    
    private fun loadWidgets() {
        try {
            val widgetList = appWidgetManager.installedProviders
            Log.d(TAG, "Found ${widgetList.size} system widgets")
            
            // Add our custom clock widget to the list
            val customWidgets = mutableListOf<WidgetItem>()
            customWidgets.add(WidgetItem(
                name = "Часы ИLauncher",
                packageName = "com.customlauncher.app",
                isCustom = true
            ))
            Log.d(TAG, "Added custom clock widget to the list")
            
            // Convert system widgets to WidgetItem
            val systemWidgets = widgetList.map { info ->
                WidgetItem(
                    name = info.loadLabel(context.packageManager),
                    packageName = info.provider.packageName,
                    providerInfo = info,
                    isCustom = false
                )
            }
            
            val allWidgets = customWidgets + systemWidgets
            Log.d(TAG, "Total widgets in list: ${allWidgets.size}")
            
            val recyclerView = findViewById<RecyclerView>(R.id.widgetList)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = WidgetAdapter(allWidgets) { widgetItem ->
                Log.d(TAG, "Widget clicked: ${widgetItem.name}, isCustom: ${widgetItem.isCustom}")
                if (widgetItem.isCustom) {
                    // Handle custom widget - don't dismiss yet
                    handleCustomWidget(widgetItem)
                } else {
                    // Handle system widget
                    widgetItem.providerInfo?.let {
                        onWidgetSelected(it)
                        dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading widgets", e)
        }
    }
    
    private fun handleCustomWidget(widgetItem: WidgetItem) {
        // For now, only handle clock widget
        if (widgetItem.name == "Часы ИLauncher") {
            // Show size selection dialog for clock widget
            val activity = context as? com.customlauncher.app.ui.HomeScreenActivity
            if (activity != null) {
                Log.d(TAG, "Showing clock widget size dialog")
                val sizeDialog = ClockWidgetSizeDialog.newInstance(
                    object : ClockWidgetSizeDialog.OnSizeSelectedListener {
                        override fun onSizeSelected(spanX: Int, spanY: Int) {
                            Log.d(TAG, "Clock widget size selected: ${spanX}x${spanY}")
                            // Add clock widget with selected size
                            activity.addClockWidget(spanX, spanY)
                        }
                    }
                )
                sizeDialog.show(activity.supportFragmentManager, "ClockWidgetSizeDialog")
                // Dismiss the widget picker dialog after showing size dialog
                dismiss()
            } else {
                Log.e(TAG, "Context is not HomeScreenActivity, cannot show size dialog")
            }
        }
    }
    
    data class WidgetItem(
        val name: String,
        val packageName: String,
        val providerInfo: AppWidgetProviderInfo? = null,
        val isCustom: Boolean = false
    )
    
    inner class WidgetAdapter(
        private val widgets: List<WidgetItem>,
        private val onItemClick: (WidgetItem) -> Unit
    ) : RecyclerView.Adapter<WidgetAdapter.ViewHolder>() {
        
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView = view.findViewById(R.id.widgetIcon)
            val name: TextView = view.findViewById(R.id.widgetName)
            val packageName: TextView = view.findViewById(R.id.widgetPackage)
            
            init {
                view.setOnClickListener {
                    widgets.getOrNull(adapterPosition)?.let {
                        onItemClick(it)
                    }
                }
            }
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_widget_picker, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val widget = widgets[position]
            holder.name.text = widget.name
            holder.packageName.text = widget.packageName
            
            // Load widget preview or icon
            try {
                if (widget.isCustom) {
                    // Use custom icon for our widgets
                    holder.icon.setImageResource(R.drawable.ic_clock)
                } else {
                    widget.providerInfo?.let { info ->
                        val drawable = context.packageManager.getDrawable(
                            info.provider.packageName,
                            info.icon,
                            null
                        )
                        holder.icon.setImageDrawable(drawable)
                    }
                }
            } catch (e: Exception) {
                // Use default icon
                holder.icon.setImageResource(R.drawable.ic_widget)
            }
        }
        
        override fun getItemCount() = widgets.size
    }
}

package com.customlauncher.app.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.R
import com.customlauncher.app.ui.widget.ClockWidget

/**
 * Dialog for selecting clock widget size
 */
class ClockWidgetSizeDialog : DialogFragment() {
    
    companion object {
        private const val TAG = "ClockWidgetSizeDialog"
        
        fun newInstance(listener: OnSizeSelectedListener): ClockWidgetSizeDialog {
            return ClockWidgetSizeDialog().apply {
                this.sizeSelectedListener = listener
            }
        }
    }
    
    interface OnSizeSelectedListener {
        fun onSizeSelected(spanX: Int, spanY: Int)
    }
    
    private var sizeSelectedListener: OnSizeSelectedListener? = null
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dialog
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_clock_widget_size_v2, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Log.d(TAG, "ClockWidgetSizeDialog onViewCreated")
        setupRecyclerView(view)
    }
    
    override fun onStart() {
        super.onStart()
        // Set dialog width and height
        dialog?.window?.apply {
            val params = attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = params
        }
    }
    
    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.sizePreviewRecyclerView)
        
        // Create list of available sizes
        val sizes = listOf(
            SizeOption(1, 2, "1x2"),  // Vertical narrow
            SizeOption(2, 1, "2x1"),  // Horizontal narrow
            SizeOption(2, 2, "2x2"),  // Square medium
            SizeOption(3, 1, "3x1"),  // Horizontal wide
            SizeOption(3, 2, "3x2"),  // Rectangle large
            SizeOption(4, 1, "4x1"),  // Maximum wide
            SizeOption(4, 2, "4x2")   // Maximum
        )
        
        // Setup horizontal layout
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        
        // Set adapter
        recyclerView.adapter = SizePreviewAdapter(sizes) { sizeOption ->
            Log.d(TAG, "Selected size: ${sizeOption.label}")
            selectSize(sizeOption.spanX, sizeOption.spanY)
        }
    }
    
    data class SizeOption(
        val spanX: Int,
        val spanY: Int,
        val label: String
    )
    
    inner class SizePreviewAdapter(
        private val sizes: List<SizeOption>,
        private val onSizeSelected: (SizeOption) -> Unit
    ) : RecyclerView.Adapter<SizePreviewAdapter.ViewHolder>() {
        
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val previewContainer: FrameLayout = view.findViewById(R.id.previewContainer)
            val sizeLabel: TextView = view.findViewById(R.id.sizeLabel)
            
            init {
                view.setOnClickListener {
                    sizes.getOrNull(adapterPosition)?.let {
                        onSizeSelected(it)
                    }
                }
            }
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_clock_size_preview, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val sizeOption = sizes[position]
            
            // Set size label with yellow background
            holder.sizeLabel.text = sizeOption.label
            
            // Create actual clock widget preview
            val clockWidget = ClockWidget(holder.itemView.context).apply {
                updateSize(sizeOption.spanX, sizeOption.spanY)
                // Force layout to ensure proper sizing
                isClickable = false
                isFocusable = false
            }
            
            // Calculate preview size - larger to show details better
            val baseSize = 80 // Increased base size for better visibility
            val density = holder.itemView.context.resources.displayMetrics.density
            
            // Scale factor to fit different sizes proportionally
            val scaleFactor = when {
                sizeOption.spanX == 4 && sizeOption.spanY == 2 -> 0.7f // Scale down largest
                sizeOption.spanX >= 3 && sizeOption.spanY >= 2 -> 0.8f // Scale down large
                sizeOption.spanX >= 3 || sizeOption.spanY >= 2 -> 0.85f // Medium scale
                else -> 1.0f // Full size for smaller widgets
            }
            
            val widthPx = (baseSize * sizeOption.spanX * scaleFactor * density).toInt()
            val heightPx = (baseSize * sizeOption.spanY * scaleFactor * density).toInt()
            
            // Clear previous preview
            holder.previewContainer.removeAllViews()
            
            // Add clock widget to container with calculated size
            val params = FrameLayout.LayoutParams(widthPx, heightPx).apply {
                gravity = android.view.Gravity.CENTER
            }
            clockWidget.layoutParams = params
            holder.previewContainer.addView(clockWidget)
            
            // Ensure the preview container has enough space
            holder.previewContainer.layoutParams = holder.previewContainer.layoutParams.apply {
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
        
        override fun getItemCount() = sizes.size
    }
    
    private fun selectSize(spanX: Int, spanY: Int) {
        sizeSelectedListener?.onSizeSelected(spanX, spanY)
        dismiss()
    }
    
    fun setOnSizeSelectedListener(listener: OnSizeSelectedListener) {
        this.sizeSelectedListener = listener
    }
}

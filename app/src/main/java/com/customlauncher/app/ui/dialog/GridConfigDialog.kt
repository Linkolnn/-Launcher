package com.customlauncher.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.customlauncher.app.R
import com.customlauncher.app.data.model.GridConfiguration
import com.google.android.material.button.MaterialButton

/**
 * Dialog for configuring home screen grid size
 */
class GridConfigDialog(
    context: Context,
    private val currentConfig: GridConfiguration,
    private val isButtonMode: Boolean,
    private val onSave: (columns: Int, rows: Int) -> Unit
) : Dialog(context) {
    
    private lateinit var gridPreview: GridPreviewView
    private lateinit var columnsText: TextView
    private lateinit var rowsText: TextView
    private lateinit var saveButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    
    private var columns = currentConfig.columns
    private var rows = currentConfig.rows
    
    companion object {
        private const val MIN_COLUMNS = 2
        private const val MAX_COLUMNS = 7
        private const val MIN_ROWS = 3
        private const val MAX_ROWS = 10
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        
        // Set transparent background
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        // Get screen dimensions for adaptive sizing
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val density = displayMetrics.density
        
        // Minimal padding for compact look
        val dialogPadding = if (screenWidth <= 640) 12 else 24
        
        // Create ScrollView for small screens
        val scrollView = android.widget.ScrollView(context).apply {
            isFillViewport = true
            isVerticalScrollBarEnabled = false // Hide scrollbar for cleaner look
        }
        
        // Create layout with custom background
        val rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            // Create background with smaller corner radius (no stroke)
            val bgDrawable = GradientDrawable().apply {
                setColor(ContextCompat.getColor(context, R.color.background_dark))
                cornerRadius = 16f * density // Smaller radius
            }
            background = bgDrawable
            setPadding(
                (dialogPadding * density).toInt(), 
                (dialogPadding * density).toInt(), 
                (dialogPadding * density).toInt(), 
                (dialogPadding * density).toInt()
            )
            gravity = Gravity.CENTER
        }
        
        // Title with custom font and white color - compact version
        val titleText = TextView(context).apply {
            text = if (isButtonMode) "Сетка для кнопочных телефонов" else "Сетка главного экрана"
            // Smaller text size for compact look
            textSize = if (screenWidth <= 640) 14f else 18f
            setTextColor(Color.WHITE) // Changed to white
            gravity = Gravity.CENTER
            // Reduced bottom padding
            setPadding(0, 0, 0, (8 * density).toInt())
            // Set custom font 5mal6Lampen
            typeface = try {
                ResourcesCompat.getFont(context, R.font.fivemal6lampen)
            } catch (e: Exception) {
                Typeface.DEFAULT_BOLD
            }
            // Межстрочный интервал для предотвращения наслаивания текста
            setLineSpacing(15f * density, 1.0f)  // Increased line spacing as requested
        }
        rootLayout.addView(titleText)
        
        // Grid preview with adaptive size
        gridPreview = GridPreviewView(context).apply {
            updateGrid(columns, rows)
        }
        
        // Calculate adaptive preview size - 65% of dialog height
        val previewWidth: Int
        val previewHeight: Int
        
        if (screenWidth <= 640) {
            // For very small screens - narrower width, 65% of available height
            previewWidth = (screenWidth * 0.5f).toInt()
            // Calculate 65% of available screen height (accounting for other elements)
            val availableHeight = screenHeight - (100 * density).toInt() // Leave space for title and controls
            previewHeight = (availableHeight * 0.65f).toInt()
        } else {
            // For normal screens
            val availableHeight = screenHeight - (200 * density).toInt()
            previewWidth = (screenWidth * 0.7f).toInt().coerceAtMost((400 * density).toInt())
            previewHeight = (availableHeight * 0.65f).toInt().coerceAtMost((600 * density).toInt())
        }
        
        val previewContainer = FrameLayout(context).apply {
            addView(gridPreview, FrameLayout.LayoutParams(previewWidth, previewHeight).apply {
                gravity = Gravity.CENTER
            })
        }
        rootLayout.addView(previewContainer)
        
        // Columns control
        val columnsLayout = createControlRow("Столбцы:", columns) { delta ->
            val newColumns = (columns + delta).coerceIn(MIN_COLUMNS, MAX_COLUMNS)
            if (newColumns != columns) {
                columns = newColumns
                updatePreview()
            }
        }
        rootLayout.addView(columnsLayout)
        
        // Rows control
        val rowsLayout = createControlRow("Строки:", rows) { delta ->
            val newRows = (rows + delta).coerceIn(MIN_ROWS, MAX_ROWS)
            if (newRows != rows) {
                rows = newRows
                updatePreview()
            }
        }
        rootLayout.addView(rowsLayout)
        
        // Buttons in vertical layout with full width
        val buttonsLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(0, (8 * density).toInt(), 0, 0)  // Reduced top padding
        }
        
        // Save button with custom font and more rounded corners (top)
        saveButton = MaterialButton(context).apply {
            text = "СОХРАНИТЬ"
            setTextColor(ContextCompat.getColor(context, R.color.background_dark))
            setBackgroundColor(ContextCompat.getColor(context, R.color.accent_yellow))
            cornerRadius = 50 // More rounded
            // Adaptive padding
            val buttonPadding = if (screenWidth <= 640) 12 else 16
            setPadding(
                (40 * density).toInt(), 
                (buttonPadding * density).toInt(), 
                (40 * density).toInt(), 
                (buttonPadding * density).toInt()
            )
            // Adaptive text size
            textSize = if (screenWidth <= 640) 14f else 16f
            // Set custom font
            typeface = try {
                ResourcesCompat.getFont(context, R.font.fivemal6lampen)
            } catch (e: Exception) {
                Typeface.DEFAULT_BOLD
            }
            setOnClickListener {
                // Check if grid is being reduced
                val isReducing = columns < currentConfig.columns || rows < currentConfig.rows
                
                if (isReducing) {
                    // Show warning dialog
                    showReductionWarning {
                        onSave(columns, rows)
                        dismiss()
                    }
                } else {
                    onSave(columns, rows)
                    dismiss()
                }
            }
        }
        buttonsLayout.addView(saveButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, (4 * density).toInt())  // Reduced margin between buttons
        })
        
        // Cancel button with custom font and more rounded corners (bottom)
        cancelButton = MaterialButton(context).apply {
            text = "ОТМЕНА"
            setTextColor(Color.WHITE)
            setBackgroundColor(ContextCompat.getColor(context, R.color.background_dark))
            cornerRadius = 50 // More rounded
            // Adaptive padding
            val buttonPadding = if (screenWidth <= 640) 12 else 16
            setPadding(
                (40 * density).toInt(), 
                (buttonPadding * density).toInt(), 
                (40 * density).toInt(), 
                (buttonPadding * density).toInt()
            )
            // Adaptive text size
            textSize = if (screenWidth <= 640) 14f else 16f
            // Set custom font
            typeface = try {
                ResourcesCompat.getFont(context, R.font.fivemal6lampen)
            } catch (e: Exception) {
                Typeface.DEFAULT_BOLD
            }
            setOnClickListener {
                dismiss()
            }
        }
        buttonsLayout.addView(cancelButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))
        
        rootLayout.addView(buttonsLayout)
        
        // Add rootLayout to scrollView
        scrollView.addView(rootLayout)
        
        // Set scrollView as content view
        setContentView(scrollView)
        
        // Set dialog window size with margins
        val marginPx = 10 // 10px margin as requested
        val dialogWidth = if (screenWidth <= 640) {
            screenWidth - (marginPx * 2) // Account for margins on both sides
        } else {
            (screenWidth * 0.9f).toInt().coerceAtMost((400 * density).toInt())
        }
        
        // Limit dialog height on small screens to ensure scrolling works  
        val maxDialogHeight = if (screenHeight <= 640) {
            screenHeight - (marginPx * 2) // Account for top and bottom margins
        } else {
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        }
        
        window?.setLayout(dialogWidth, maxDialogHeight)
    }
    
    private fun createControlRow(label: String, initialValue: Int, onChange: (Int) -> Unit): LinearLayout {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val density = displayMetrics.density
        
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            val rowPadding = if (screenWidth <= 640) 4 else 12  // Reduced padding between controls
            setPadding(0, (rowPadding * density).toInt(), 0, (rowPadding * density).toInt())
            
            // Label with adaptive text size - compact
            val labelView = TextView(context).apply {
                text = label
                setTextColor(Color.WHITE)
                textSize = if (screenWidth <= 640) 13f else 15f  // Smaller text
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            addView(labelView)
            
            // Minus button
            val minusButton = createCircularButton("-") {
                onChange(-1)
            }
            addView(minusButton)
            
            // Value text with adaptive size - compact
            val valueText = TextView(context).apply {
                text = initialValue.toString()
                setTextColor(Color.WHITE)
                textSize = if (screenWidth <= 640) 14f else 17f  // Smaller text
                gravity = Gravity.CENTER
                val valuePadding = if (screenWidth <= 640) 6 else 20  // Minimal padding
                setPadding((valuePadding * density).toInt(), 0, (valuePadding * density).toInt(), 0)
                val minWidthDp = if (screenWidth <= 640) 25 else 50  // Smaller min width
                minWidth = (minWidthDp * density).toInt()
            }
            addView(valueText)
            
            // Store reference based on label
            if (label.contains("Столбцы")) {
                columnsText = valueText
            } else {
                rowsText = valueText
            }
            
            // Plus button
            val plusButton = createCircularButton("+") {
                onChange(1)
            }
            addView(plusButton)
        }
    }
    
    private fun createCircularButton(text: String, onClick: () -> Unit): View {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val density = displayMetrics.density
        
        // Adaptive button size (in dp converted to pixels) - ultra compact
        val buttonSizeDp = if (screenWidth <= 640) 16 else 24  // Reduced by ~4.5 times from original
        val buttonSizePx = (buttonSizeDp * density).toInt()
        
        // Create a custom circular button with icon
        return FrameLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(buttonSizePx, buttonSizePx).apply {
                val buttonMargin = if (screenWidth <= 640) 1 else 4
                setMargins(
                    (buttonMargin * density).toInt(), 
                    0, 
                    (buttonMargin * density).toInt(), 
                    0
                )
            }
            
            // Background circle
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(ContextCompat.getColor(context, R.color.accent_yellow))
            }
            
            // Icon instead of text
            val iconView = ImageView(context).apply {
                // Choose icon based on text parameter
                val iconResId = if (text == "-") {
                    R.drawable.ic_minus_button
                } else {
                    R.drawable.ic_plus_button
                }
                setImageResource(iconResId)
                
                // Set tint to match the dark background color
                setColorFilter(ContextCompat.getColor(context, R.color.background_dark))
                
                // Scale the icon appropriately
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                
                // Adaptive padding for icon - ultra minimal for tiny buttons
                val iconPadding = if (screenWidth <= 640) 2 else 6  // Minimal padding for 16dp buttons
                setPadding(
                    (iconPadding * density).toInt(), 
                    (iconPadding * density).toInt(), 
                    (iconPadding * density).toInt(), 
                    (iconPadding * density).toInt()
                )
            }
            
            // Add icon with center layout params
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = Gravity.CENTER
            }
            
            addView(iconView, layoutParams)
            
            // Ripple effect on click
            isClickable = true
            isFocusable = true
            setOnClickListener { onClick() }
        }
    }
    
    private fun updatePreview() {
        gridPreview.updateGrid(columns, rows)
        columnsText.text = columns.toString()
        rowsText.text = rows.toString()
    }
    
    private fun showReductionWarning(onConfirm: () -> Unit) {
        val warningDialog = android.app.AlertDialog.Builder(context)
            .setTitle("Уменьшение сетки")
            .setMessage("При уменьшении размера сетки некоторые элементы могут быть перемещены или скрыты.\n\nПродолжить?")
            .setPositiveButton("ПРОДОЛЖИТЬ") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("ОТМЕНА", null)
            .create()
        
        warningDialog.show()
        
        // Style the buttons
        warningDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.apply {
            setTextColor(ContextCompat.getColor(context, R.color.accent_yellow))
        }
        warningDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.apply {
            setTextColor(Color.WHITE)
        }
    }
    
    /**
     * Custom view for grid preview with rounded cells
     */
    class GridPreviewView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        
        private var columns = 4
        private var rows = 6
        
        private val cellBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
            color = ContextCompat.getColor(context, R.color.accent_yellow)
            alpha = 60
            isAntiAlias = true
        }
        
        private val cellFillPaint = Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.accent_yellow)
            alpha = 20
            isAntiAlias = true
        }
        
        private val cellRect = RectF()
        
        fun updateGrid(cols: Int, rows: Int) {
            this.columns = cols
            this.rows = rows
            invalidate()
        }
        
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            
            val cellWidth = width.toFloat() / columns
            val cellHeight = height.toFloat() / rows
            val padding = 4f
            val cornerRadius = 8f
            
            // Draw grid cells with rounded corners
            for (col in 0 until columns) {
                for (row in 0 until rows) {
                    val left = col * cellWidth + padding
                    val top = row * cellHeight + padding
                    val right = left + cellWidth - padding * 2
                    val bottom = top + cellHeight - padding * 2
                    
                    cellRect.set(left, top, right, bottom)
                    
                    // Draw all cells with the same subtle fill
                    canvas.drawRoundRect(cellRect, cornerRadius, cornerRadius, cellFillPaint)
                    
                    // Draw border for all cells
                    canvas.drawRoundRect(cellRect, cornerRadius, cornerRadius, cellBorderPaint)
                }
            }
        }
    }
}

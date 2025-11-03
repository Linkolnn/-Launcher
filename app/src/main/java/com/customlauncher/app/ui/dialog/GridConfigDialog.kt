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
        
        // Create layout with custom background
        val rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            // Create background with smaller corner radius (no stroke)
            val bgDrawable = GradientDrawable().apply {
                setColor(ContextCompat.getColor(context, R.color.background_dark))
                cornerRadius = 16f * resources.displayMetrics.density // Smaller radius
            }
            background = bgDrawable
            setPadding(32, 32, 32, 32)
            gravity = Gravity.CENTER
        }
        
        // Title with custom font and white color
        val titleText = TextView(context).apply {
            text = if (isButtonMode) "Сетка для кнопочных телефонов" else "Сетка главного экрана"
            textSize = 20f
            setTextColor(Color.WHITE) // Changed to white
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
            // Set custom font 5mal6Lampen
            typeface = try {
                ResourcesCompat.getFont(context, R.font.fivemal6lampen)
            } catch (e: Exception) {
                Typeface.DEFAULT_BOLD
            }
            // Добавляем межстрочный интервал
            setLineSpacing(10f * resources.displayMetrics.density, 1.0f)
        }
        rootLayout.addView(titleText)
        
        // Grid preview
        gridPreview = GridPreviewView(context).apply {
            updateGrid(columns, rows)
        }
        val previewContainer = FrameLayout(context).apply {
            addView(gridPreview, FrameLayout.LayoutParams(400, 600).apply {
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
        
        // Buttons
        val buttonsLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }
        
        // Cancel button with custom font and more rounded corners
        cancelButton = MaterialButton(context).apply {
            text = "ОТМЕНА"
            setTextColor(Color.WHITE)
            setBackgroundColor(ContextCompat.getColor(context, R.color.background_dark))
            cornerRadius = 50 // More rounded
            setPadding(40, 16, 40, 16)
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
        buttonsLayout.addView(cancelButton)
        
        // Spacer
        val spacer = View(context).apply {
            layoutParams = LinearLayout.LayoutParams(32, 1)
        }
        buttonsLayout.addView(spacer)
        
        // Save button with custom font and more rounded corners
        saveButton = MaterialButton(context).apply {
            text = "СОХРАНИТЬ"
            setTextColor(ContextCompat.getColor(context, R.color.background_dark))
            setBackgroundColor(ContextCompat.getColor(context, R.color.accent_yellow))
            cornerRadius = 50 // More rounded
            setPadding(40, 16, 40, 16)
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
        buttonsLayout.addView(saveButton)
        
        rootLayout.addView(buttonsLayout)
        
        setContentView(rootLayout)
    }
    
    private fun createControlRow(label: String, initialValue: Int, onChange: (Int) -> Unit): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 16, 0, 16)
            
            // Label
            val labelView = TextView(context).apply {
                text = label
                setTextColor(Color.WHITE)
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            addView(labelView)
            
            // Minus button
            val minusButton = createCircularButton("-") {
                onChange(-1)
            }
            addView(minusButton)
            
            // Value text
            val valueText = TextView(context).apply {
                text = initialValue.toString()
                setTextColor(Color.WHITE)
                textSize = 18f
                gravity = Gravity.CENTER
                setPadding(24, 0, 24, 0)
                minWidth = 60
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
        // Create a custom circular button with better styling
        return FrameLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(64, 64).apply {
                setMargins(8, 0, 8, 0)
            }
            
            // Background circle
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(ContextCompat.getColor(context, R.color.accent_yellow))
            }
            
            // Text with proper centering
            val textView = TextView(context).apply {
                this.text = text
                setTextColor(ContextCompat.getColor(context, R.color.background_dark))
                textSize = 44f  // Увеличен в 2 раза (было 22f)
                gravity = Gravity.CENTER
                textAlignment = View.TEXT_ALIGNMENT_CENTER  // Дополнительное центрирование
                // Используем шрифт 5mal6Lampen для кнопок +/-
                typeface = try {
                    ResourcesCompat.getFont(context, R.font.fivemal6lampen)
                } catch (e: Exception) {
                    Typeface.DEFAULT_BOLD
                }
                // Fix vertical centering
                includeFontPadding = false
                
                // Опускаем символы вниз для визуального центрирования
                if (text == "-") {
                    // Минус опускаем еще ниже, так как он визуально выше
                    setPadding(0, 44, 0, 0)
                } else {
                    // Плюс тоже опускаем вниз
                    setPadding(0, 42, 0, 0)
                }
            }
            addView(textView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = Gravity.CENTER
            })
            
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

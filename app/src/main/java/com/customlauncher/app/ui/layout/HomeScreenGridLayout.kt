package com.customlauncher.app.ui.layout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import com.customlauncher.app.data.model.GridConfiguration
import com.customlauncher.app.R
import kotlin.math.min

/**
 * Custom layout for home screen grid that manages app icons and widgets positioning
 */
class HomeScreenGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        // Explicitly remove any background to prevent white overlay
        background = null
        setBackgroundColor(Color.TRANSPARENT)
        // Prevent focus highlight
        isClickable = false
        // Disable default focus highlight drawable
        defaultFocusHighlightEnabled = false
    }
    
    private var gridConfig: GridConfiguration = GridConfiguration.getDefault()
    private val cellWidth: Int
        get() = if (width > 0 && gridConfig.columns > 0) width / gridConfig.columns else 0
    private val cellHeight: Int
        get() = if (height > 0 && gridConfig.rows > 0) height / gridConfig.rows else 0
    
    private val debugPaint = Paint().apply {
        color = 0x20FFFFFF
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    
    private var showDebugGrid = false
    private val occupiedCells = mutableSetOf<Pair<Int, Int>>()
    
    // D-pad navigation support
    private var focusedCell: Pair<Int, Int>? = null
    private var isButtonPhoneMode = false
    
    // Enhanced focus visualization
    private val focusStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.YELLOW
        alpha = 255
    }
    
    private val focusFillPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
        alpha = 30  // Semi-transparent yellow fill for better visibility
    }
    
    private val emptyFocusPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.WHITE
        alpha = 180  // More subtle for empty cells
        pathEffect = android.graphics.DashPathEffect(floatArrayOf(10f, 5f), 0f)
    }
    
    private val focusRect = RectF()
    var onBottomReached: (() -> Unit)? = null
    
    fun setGridConfiguration(config: GridConfiguration) {
        gridConfig = config
        occupiedCells.clear()
        isButtonPhoneMode = config.isButtonMode
        if (isButtonPhoneMode) {
            // Set initial focus to first occupied cell or (0,0)
            focusedCell = occupiedCells.firstOrNull() ?: Pair(0, 0)
            // Don't set focusableInTouchMode to prevent white background
            isFocusableInTouchMode = false
        }
        requestLayout()
    }
    
    fun getGridConfiguration(): GridConfiguration {
        return gridConfig
    }
    
    fun setButtonPhoneMode(enabled: Boolean) {
        isButtonPhoneMode = enabled
        if (enabled) {
            // Remove focusableInTouchMode to prevent white background
            isFocusableInTouchMode = false
            if (focusedCell == null) {
                focusedCell = occupiedCells.firstOrNull() ?: Pair(0, 0)
            }
        } else {
            focusedCell = null
        }
        invalidate()
    }
    
    /**
     * Set focused cell from external source (GridFocusManager)
     */
    fun setFocusedCell(row: Int, col: Int) {
        if (row < 0 || col < 0) {
            focusedCell = null
        } else {
            focusedCell = Pair(col, row) // Note: Pair is (x, y) not (row, col)
        }
        invalidate()
    }
    
    /**
     * Clear grid focus
     */
    fun clearGridFocus() {
        focusedCell = null
        invalidate()
    }
    
    fun addItemAtPosition(view: View, x: Int, y: Int, spanX: Int = 1, spanY: Int = 1): Boolean {
        if (!gridConfig.canFitItem(x, y, spanX, spanY)) {
            return false
        }
        
        // Check if cells are occupied
        for (row in y until y + spanY) {
            for (col in x until x + spanX) {
                val cellPos = Pair(col, row)
                if (occupiedCells.contains(cellPos)) {
                    return false // Cell is already occupied
                }
            }
        }
        
        // Mark cells as occupied
        for (row in y until y + spanY) {
            for (col in x until x + spanX) {
                occupiedCells.add(Pair(col, row))
            }
        }
        
        // Set layout params with position and span
        val layoutParams = CellLayoutParams(x, y, spanX, spanY)
        view.layoutParams = layoutParams
        
        addView(view)
        return true
    }
    
    fun removeItemAt(x: Int, y: Int) {
        val childToRemove = children.find { child ->
            val params = child.layoutParams as? CellLayoutParams
            params?.cellX == x && params?.cellY == y
        }
        
        childToRemove?.let { child ->
            val params = child.layoutParams as CellLayoutParams
            
            // Clear occupied cells
            for (row in params.cellY until params.cellY + params.spanY) {
                for (col in params.cellX until params.cellX + params.spanX) {
                    occupiedCells.remove(Pair(col, row))
                }
            }
            
            removeView(child)
        }
    }
    
    override fun removeAllViews() {
        super.removeAllViews()
        // Clear the occupied cells tracking when all views are removed
        occupiedCells.clear()
        Log.d("HomeScreenGrid", "Cleared all views and occupied cells")
    }
    
    fun findFirstEmptyCell(): Pair<Int, Int>? {
        for (y in 0 until gridConfig.rows) {
            for (x in 0 until gridConfig.columns) {
                val cellPos = Pair(x, y)
                if (!occupiedCells.contains(cellPos)) {
                    return cellPos
                }
            }
        }
        return null
    }
    
    fun getCellRect(x: Int, y: Int): Rect {
        val left = x * cellWidth
        val top = y * cellHeight
        return Rect(left, top, left + cellWidth, top + cellHeight)
    }
    
    fun getCellFromPoint(x: Float, y: Float): Pair<Int, Int>? {
        if (cellWidth == 0 || cellHeight == 0) return null
        
        val cellX = (x / cellWidth).toInt()
        val cellY = (y / cellHeight).toInt()
        
        return if (gridConfig.isValidPosition(cellX, cellY)) {
            Pair(cellX, cellY)
        } else {
            null
        }
    }
    
    fun getViewAt(x: Int, y: Int): View? {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as? CellLayoutParams
            if (lp != null) {
                // Check if this view occupies the target cell
                if (x >= lp.cellX && x < lp.cellX + lp.spanX &&
                    y >= lp.cellY && y < lp.cellY + lp.spanY) {
                    return child
                }
            }
        }
        return null
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        
        setMeasuredDimension(width, height)
        
        // Measure children
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.layoutParams as? CellLayoutParams ?: continue
            
            val childWidth = cellWidth * params.spanX
            val childHeight = cellHeight * params.spanY
            
            child.measure(
                MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
            )
        }
    }
    
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.layoutParams as? CellLayoutParams ?: continue
            
            val cellLeft = params.cellX * cellWidth
            val cellTop = params.cellY * cellHeight
            val cellRight = cellLeft + (cellWidth * params.spanX)
            val cellBottom = cellTop + (cellHeight * params.spanY)
            
            // Add some padding
            val padding = min(cellWidth, cellHeight) / 16
            child.layout(
                cellLeft + padding,
                cellTop + padding,
                cellRight - padding,
                cellBottom - padding
            )
        }
    }
    
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        
        // Draw debug grid if enabled
        if (showDebugGrid) {
            for (i in 0..gridConfig.columns) {
                val x = i * cellWidth.toFloat()
                canvas.drawLine(x, 0f, x, height.toFloat(), debugPaint)
            }
            
            for (i in 0..gridConfig.rows) {
                val y = i * cellHeight.toFloat()
                canvas.drawLine(0f, y, width.toFloat(), y, debugPaint)
            }
        }
        
        // Draw focus indicator when using D-pad navigation (both touch and button modes)
        if (focusedCell != null) {
            val (x, y) = focusedCell!!
            val left = x * cellWidth.toFloat()
            val top = y * cellHeight.toFloat()
            val right = left + cellWidth
            val bottom = top + cellHeight
            
            focusRect.set(left + 4, top + 4, right - 4, bottom - 4)
            
            // Check if this cell is occupied
            val isOccupied = getViewAt(x, y) != null
            
            if (isOccupied) {
                // Draw filled background for occupied cells
                canvas.drawRoundRect(focusRect, 12f, 12f, focusFillPaint)
                canvas.drawRoundRect(focusRect, 12f, 12f, focusStrokePaint)
            } else {
                // Draw dashed outline for empty cells
                canvas.drawRoundRect(focusRect, 8f, 8f, emptyFocusPaint)
            }
        }
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Handle D-pad navigation in both button mode and touch mode (for physical keyboards)
        // In touch mode, we still want to support D-pad navigation for accessibility
        
        // If no focused cell yet, initialize it with first occupied cell
        if (focusedCell == null && occupiedCells.isNotEmpty()) {
            focusedCell = occupiedCells.firstOrNull() ?: Pair(0, 0)
            invalidate()
        }
        
        val currentFocus = focusedCell ?: return super.onKeyDown(keyCode, event)
        var newX = currentFocus.first
        var newY = currentFocus.second
        
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                newX = (newX - 1).coerceAtLeast(0)
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                newX = (newX + 1).coerceAtMost(gridConfig.columns - 1)
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                newY = (newY - 1).coerceAtLeast(0)
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (currentFocus.second == gridConfig.rows - 1) {
                    // Already at bottom, trigger open app drawer
                    onBottomReached?.invoke()
                    return true
                }
                newY = (newY + 1).coerceAtMost(gridConfig.rows - 1)
            }
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                // Find and click the view at focused position
                val view = getViewAt(currentFocus.first, currentFocus.second)
                view?.performClick()
                return true
            }
            else -> return super.onKeyDown(keyCode, event)
        }
        
        if (newX != currentFocus.first || newY != currentFocus.second) {
            focusedCell = Pair(newX, newY)
            invalidate()
            return true
        }
        
        return super.onKeyDown(keyCode, event)
    }
    
    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is CellLayoutParams
    }
    
    override fun generateDefaultLayoutParams(): FrameLayout.LayoutParams {
        return CellLayoutParams(0, 0)
    }
    
    override fun generateLayoutParams(attrs: AttributeSet?): FrameLayout.LayoutParams {
        return CellLayoutParams(context, attrs)
    }
    
    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): FrameLayout.LayoutParams {
        return if (p != null) {
            CellLayoutParams(p)
        } else {
            CellLayoutParams(0, 0)
        }
    }
    
    /**
     * Layout params for grid cell positioning
     */
    class CellLayoutParams : FrameLayout.LayoutParams {
        var cellX: Int = 0
        var cellY: Int = 0
        var spanX: Int = 1
        var spanY: Int = 1
        
        constructor(x: Int, y: Int, spanX: Int = 1, spanY: Int = 1) : super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) {
            this.cellX = x
            this.cellY = y
            this.spanX = spanX
            this.spanY = spanY
        }
        
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
        
        constructor(source: ViewGroup.LayoutParams) : super(source) {
            if (source is CellLayoutParams) {
                cellX = source.cellX
                cellY = source.cellY
                spanX = source.spanX
                spanY = source.spanY
            }
        }
    }
}

package com.customlauncher.app.manager

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import com.customlauncher.app.data.model.HomeItemModel
import kotlin.math.max
import kotlin.math.min

/**
 * Manager for handling D-pad focus navigation in grid layout
 */
class GridFocusManager(
    private var gridColumns: Int,
    private var gridRows: Int
) {
    
    companion object {
        private const val TAG = "GridFocusManager"
        const val POSITION_NONE = -1
        private const val FOCUS_TIMEOUT_MS = 5000L // 5 seconds
    }
    
    // Current focused position (for items)
    var focusedPosition: Int = POSITION_NONE
        private set
    
    // Current focused row and column (for grid navigation)
    var focusedRow: Int = 0
        private set
    var focusedColumn: Int = 0
        private set
    
    // List of items in the grid
    var items = mutableListOf<HomeItemModel>()
        private set
    
    // Focus timeout handler
    private val focusTimeoutHandler = Handler(Looper.getMainLooper())
    private val focusTimeoutRunnable = Runnable {
        Log.d(TAG, "Focus timeout - hiding focus after $FOCUS_TIMEOUT_MS ms of inactivity")
        clearFocus()
    }
    
    // Callback for focus changes
    interface FocusChangeListener {
        fun onFocusChanged(oldPosition: Int, newPosition: Int, item: HomeItemModel?)
        fun onGridFocusChanged(oldRow: Int, oldCol: Int, newRow: Int, newCol: Int, item: HomeItemModel?)
        fun onItemSelected(position: Int, item: HomeItemModel?)
        fun onNavigateToMenu() // Called when navigating down from bottom row
        fun shouldNavigateToMenuOnDown(): Boolean // Check if should navigate to menu on down
    }
    
    private var focusChangeListener: FocusChangeListener? = null
    
    /**
     * Set the focus change listener
     */
    fun setFocusChangeListener(listener: FocusChangeListener?) {
        focusChangeListener = listener
    }
    
    /**
     * Update grid configuration
     */
    fun updateGridConfiguration(columns: Int, rows: Int) {
        gridColumns = columns
        gridRows = rows
        Log.d(TAG, "Grid configuration updated: ${columns}x${rows}")
        
        // Validate current focus position
        if (focusedPosition != POSITION_NONE) {
            val maxPosition = items.size - 1
            if (focusedPosition > maxPosition) {
                setFocusPosition(maxPosition)
            }
        }
    }
    
    /**
     * Update the list of items
     */
    fun updateItems(newItems: List<HomeItemModel>) {
        // Only update if items actually changed
        val sortedNewItems = newItems.sortedWith(compareBy({ it.cellY }, { it.cellX }))
        
        if (items != sortedNewItems) {
            items.clear()
            items.addAll(sortedNewItems)
            
            Log.d(TAG, "Items updated, count: ${items.size}")
            
            // Reset focus if necessary
            if (focusedPosition >= items.size) {
                focusedPosition = if (items.isNotEmpty()) 0 else POSITION_NONE
            }
        } else {
            Log.d(TAG, "Items unchanged, skipping update")
        }
    }
    
    /**
     * Handle key events for navigation
     * @return true if event was handled
     */
    fun handleKeyEvent(keyCode: Int, event: KeyEvent): Boolean {
        // Only handle key down events
        if (event.action != KeyEvent.ACTION_DOWN) {
            return false
        }
        
        // Navigate by cells, not items
        // Initialize focus if needed
        if (!hasFocus()) {
            setGridFocus(0, 0)
            return true
        }
        
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> moveUpByCell()
            KeyEvent.KEYCODE_DPAD_DOWN -> moveDownByCell()
            KeyEvent.KEYCODE_DPAD_LEFT -> moveLeftByCell()
            KeyEvent.KEYCODE_DPAD_RIGHT -> moveRightByCell()
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> selectCurrentCell()
            else -> false
        }
    }
    
    /**
     * Move focus up
     */
    private fun moveUp(): Boolean {
        if (focusedPosition == POSITION_NONE || items.isEmpty()) return false
        
        val currentItem = items.getOrNull(focusedPosition) ?: return false
        val currentRow = currentItem.cellY
        val currentCol = currentItem.cellX
        
        // Check if we're already at the top row of the grid
        if (currentRow <= 0) {
            // At the top of the grid, wrap to bottom
            Log.d(TAG, "At top row of grid (row $currentRow), wrapping to bottom")
            val bottomItem = items.filter { 
                it.cellX == currentCol 
            }.maxByOrNull { it.cellY }
            
            if (bottomItem != null) {
                val newPosition = items.indexOf(bottomItem)
                setFocusPosition(newPosition)
                return true
            } else {
                // Try to find any item in bottom row
                val bottomRowItem = items.maxByOrNull { it.cellY }
                if (bottomRowItem != null) {
                    val newPosition = items.indexOf(bottomRowItem)
                    setFocusPosition(newPosition)
                    return true
                }
                return false
            }
        }
        
        // Find item above current position
        val targetItem = items.filter { 
            it.cellY < currentRow && it.cellX == currentCol 
        }.maxByOrNull { it.cellY }
        
        // If no item directly above, try to find closest item in row above
        val newItem = targetItem ?: items.filter { 
            it.cellY < currentRow 
        }.minByOrNull { 
            kotlin.math.abs(it.cellX - currentCol) 
        }
        
        return if (newItem != null) {
            val newPosition = items.indexOf(newItem)
            setFocusPosition(newPosition)
            true
        } else {
            // No item above but not at grid top - wrap to bottom
            Log.d(TAG, "No items above, wrapping to bottom")
            val bottomItem = items.filter { 
                it.cellX == currentCol 
            }.maxByOrNull { it.cellY }
            
            if (bottomItem != null) {
                val newPosition = items.indexOf(bottomItem)
                setFocusPosition(newPosition)
                true
            } else {
                false
            }
        }
    }
    
    /**
     * Move focus down
     */
    private fun moveDown(): Boolean {
        if (focusedPosition == POSITION_NONE || items.isEmpty()) return false
        
        val currentItem = items.getOrNull(focusedPosition) ?: return false
        val currentRow = currentItem.cellY
        val currentCol = currentItem.cellX
        
        // Check if we're already at the last row of the grid (not just last item row)
        if (currentRow >= gridRows - 1) {
            // At the bottom of the grid, check if we should navigate to menu
            if (focusChangeListener?.shouldNavigateToMenuOnDown() == true) {
                // Navigate to menu (either button or open directly)
                Log.d(TAG, "At bottom row of grid (row $currentRow of $gridRows), navigating to menu")
                focusChangeListener?.onNavigateToMenu()
                return true
            } else {
                // Wrap to top if menu navigation is disabled
                val topItem = items.filter { 
                    it.cellX == currentCol 
                }.minByOrNull { it.cellY }
                
                if (topItem != null) {
                    val newPosition = items.indexOf(topItem)
                    setFocusPosition(newPosition)
                    return true
                } else {
                    return false
                }
            }
        }
        
        // Find item below current position
        val targetItem = items.filter { 
            it.cellY > currentRow && it.cellX == currentCol 
        }.minByOrNull { it.cellY }
        
        // If no item directly below, try to find closest item in row below
        val newItem = targetItem ?: items.filter { 
            it.cellY > currentRow 
        }.minByOrNull { 
            kotlin.math.abs(it.cellX - currentCol) 
        }
        
        return if (newItem != null) {
            val newPosition = items.indexOf(newItem)
            setFocusPosition(newPosition)
            true
        } else {
            // No item below but not at grid bottom - check if we should still navigate to menu
            if (focusChangeListener?.shouldNavigateToMenuOnDown() == true) {
                Log.d(TAG, "No items below, navigating to menu")
                focusChangeListener?.onNavigateToMenu()
                true
            } else {
                // Just stay at current position
                false
            }
        }
    }
    
    /**
     * Move focus left
     */
    private fun moveLeft(): Boolean {
        if (focusedPosition == POSITION_NONE || items.isEmpty()) return false
        
        val currentItem = items.getOrNull(focusedPosition) ?: return false
        val currentRow = currentItem.cellY
        val currentCol = currentItem.cellX
        
        // Check if we're already at the leftmost column of the grid
        if (currentCol <= 0) {
            // At the leftmost column, wrap to rightmost
            Log.d(TAG, "At leftmost column (col $currentCol), wrapping to right")
            val rightmostItem = items.filter { 
                it.cellY == currentRow 
            }.maxByOrNull { it.cellX }
            
            if (rightmostItem != null) {
                val newPosition = items.indexOf(rightmostItem)
                setFocusPosition(newPosition)
                return true
            }
            return false
        }
        
        // Find item to the left in same row
        val targetItem = items.filter { 
            it.cellY == currentRow && it.cellX < currentCol 
        }.maxByOrNull { it.cellX }
        
        return if (targetItem != null) {
            val newPosition = items.indexOf(targetItem)
            setFocusPosition(newPosition)
            true
        } else {
            // No item to the left but not at grid edge - wrap to rightmost
            Log.d(TAG, "No items to the left, wrapping to rightmost")
            val rightmostItem = items.filter { 
                it.cellY == currentRow 
            }.maxByOrNull { it.cellX }
            
            if (rightmostItem != null && rightmostItem != currentItem) {
                val newPosition = items.indexOf(rightmostItem)
                setFocusPosition(newPosition)
                true
            } else {
                false
            }
        }
    }
    
    /**
     * Move focus right
     */
    private fun moveRight(): Boolean {
        if (focusedPosition == POSITION_NONE || items.isEmpty()) return false
        
        val currentItem = items.getOrNull(focusedPosition) ?: return false
        val currentRow = currentItem.cellY
        val currentCol = currentItem.cellX
        
        // Check if we're already at the rightmost column of the grid
        if (currentCol >= gridColumns - 1) {
            // At the rightmost column, wrap to leftmost
            Log.d(TAG, "At rightmost column (col $currentCol of $gridColumns), wrapping to left")
            val leftmostItem = items.filter { 
                it.cellY == currentRow 
            }.minByOrNull { it.cellX }
            
            if (leftmostItem != null) {
                val newPosition = items.indexOf(leftmostItem)
                setFocusPosition(newPosition)
                return true
            }
            return false
        }
        
        // Find item to the right in same row
        val targetItem = items.filter { 
            it.cellY == currentRow && it.cellX > currentCol 
        }.minByOrNull { it.cellX }
        
        return if (targetItem != null) {
            val newPosition = items.indexOf(targetItem)
            setFocusPosition(newPosition)
            true
        } else {
            // No item to the right but not at grid edge - wrap to leftmost
            Log.d(TAG, "No items to the right, wrapping to leftmost")
            val leftmostItem = items.filter { 
                it.cellY == currentRow 
            }.minByOrNull { it.cellX }
            
            if (leftmostItem != null && leftmostItem != currentItem) {
                val newPosition = items.indexOf(leftmostItem)
                setFocusPosition(newPosition)
                true
            } else {
                false
            }
        }
    }
    
    /**
     * Select the currently focused item
     */
    private fun selectCurrentItem(): Boolean {
        if (focusedPosition == POSITION_NONE || focusedPosition >= items.size) {
            return false
        }
        
        val item = items.getOrNull(focusedPosition)
        Log.d(TAG, "Selecting item at position $focusedPosition: ${item?.label}")
        focusChangeListener?.onItemSelected(focusedPosition, item)
        return true
    }
    
    /**
     * Set focus to a specific position
     */
    fun setFocusPosition(position: Int) {
        if (position < 0 || position >= items.size) {
            Log.w(TAG, "Invalid focus position: $position (items: ${items.size})")
            return
        }
        
        val oldPosition = focusedPosition
        focusedPosition = position
        
        val item = items.getOrNull(position)
        if (item != null) {
            focusedRow = item.cellY
            focusedColumn = item.cellX
        }
        
        Log.d(TAG, "Focus changed from $oldPosition to $position (row: $focusedRow, col: $focusedColumn)")
        focusChangeListener?.onFocusChanged(oldPosition, position, item)
        
        // Restart the focus timeout timer
        restartFocusTimer()
    }
    
    /**
     * Clear focus
     */
    fun clearFocus() {
        val oldRow = focusedRow
        val oldCol = focusedColumn
        val oldPosition = focusedPosition
        
        focusedPosition = POSITION_NONE
        focusedRow = -1
        focusedColumn = -1
        
        // Stop the focus timeout timer
        stopFocusTimer()
        
        Log.d(TAG, "Focus cleared")
        focusChangeListener?.onFocusChanged(oldPosition, POSITION_NONE, null)
        focusChangeListener?.onGridFocusChanged(oldRow, oldCol, -1, -1, null)
    }
    
    /**
     * Get the currently focused item
     */
    fun getFocusedItem(): HomeItemModel? {
        return if (focusedPosition != POSITION_NONE && focusedPosition < items.size) {
            items[focusedPosition]
        } else {
            null
        }
    }
    
    /**
     * Check if focus manager has focus
     */
    fun hasFocus(): Boolean {
        return focusedRow >= 0 && focusedColumn >= 0
    }
    
    /**
     * Set grid focus to a specific cell
     */
    fun setGridFocus(row: Int, col: Int) {
        // Validate bounds
        val validRow = row.coerceIn(0, gridRows - 1)
        val validCol = col.coerceIn(0, gridColumns - 1)
        
        val oldRow = focusedRow
        val oldCol = focusedColumn
        
        focusedRow = validRow
        focusedColumn = validCol
        
        // Find item at this position if any
        val item = findItemAt(validRow, validCol)
        focusedPosition = if (item != null) items.indexOf(item) else POSITION_NONE
        
        Log.d(TAG, "Grid focus changed from ($oldRow, $oldCol) to ($validRow, $validCol)")
        focusChangeListener?.onGridFocusChanged(oldRow, oldCol, validRow, validCol, item)
        
        // Restart the focus timeout timer
        restartFocusTimer()
    }
    
    /**
     * Find item at specific cell
     */
    private fun findItemAt(row: Int, col: Int): HomeItemModel? {
        return items.find { item ->
            item.cellY == row && 
            item.cellX <= col && 
            col < item.cellX + item.spanX &&
            item.cellY <= row &&
            row < item.cellY + item.spanY
        }
    }
    
    /**
     * Move focus up by cell
     */
    private fun moveUpByCell(): Boolean {
        val newRow = if (focusedRow > 0) focusedRow - 1 else gridRows - 1
        setGridFocus(newRow, focusedColumn)
        return true
    }
    
    /**
     * Move focus down by cell
     */
    private fun moveDownByCell(): Boolean {
        val newRow = if (focusedRow < gridRows - 1) focusedRow + 1 else 0
        
        // Check if we should navigate to menu
        if (focusedRow >= gridRows - 1 && focusChangeListener?.shouldNavigateToMenuOnDown() == true) {
            Log.d(TAG, "At bottom row, navigating to menu")
            focusChangeListener?.onNavigateToMenu()
            return true
        }
        
        setGridFocus(newRow, focusedColumn)
        return true
    }
    
    /**
     * Move focus left by cell
     */
    private fun moveLeftByCell(): Boolean {
        val newCol = if (focusedColumn > 0) focusedColumn - 1 else gridColumns - 1
        setGridFocus(focusedRow, newCol)
        return true
    }
    
    /**
     * Move focus right by cell
     */
    private fun moveRightByCell(): Boolean {
        val newCol = if (focusedColumn < gridColumns - 1) focusedColumn + 1 else 0
        setGridFocus(focusedRow, newCol)
        return true
    }
    
    /**
     * Select current cell (or item in it)
     */
    private fun selectCurrentCell(): Boolean {
        val item = findItemAt(focusedRow, focusedColumn)
        Log.d(TAG, "Selecting cell at ($focusedRow, $focusedColumn): ${item?.label ?: "empty"}")
        focusChangeListener?.onItemSelected(focusedPosition, item)
        return true
    }
    
    /**
     * Request initial focus
     */
    fun requestInitialFocus() {
        if (!hasFocus()) {
            // Set focus to first cell (0, 0)
            setGridFocus(0, 0)
        }
    }
    
    /**
     * Restart the focus timer - called on any user interaction
     */
    private fun restartFocusTimer() {
        stopFocusTimer()
        focusTimeoutHandler.postDelayed(focusTimeoutRunnable, FOCUS_TIMEOUT_MS)
        Log.v(TAG, "Focus timer restarted - will hide in ${FOCUS_TIMEOUT_MS}ms")
    }
    
    /**
     * Stop the focus timer
     */
    private fun stopFocusTimer() {
        focusTimeoutHandler.removeCallbacks(focusTimeoutRunnable)
        Log.v(TAG, "Focus timer stopped")
    }
    
    /**
     * Clean up resources
     */
    fun cleanup() {
        stopFocusTimer()
    }
}

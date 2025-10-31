package com.customlauncher.app.ui.layout

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaginatedGridLayoutManager(
    context: Context,
    var columns: Int,
    var rows: Int
) : RecyclerView.LayoutManager() {
    
    private val pageSize: Int
        get() = columns * rows
    
    private var currentPage = 0
    private var totalPages = 0
    private var selectedPosition = 0
    
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT
        )
    }
    
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)
        
        if (itemCount == 0) {
            return
        }
        
        totalPages = (itemCount + pageSize - 1) / pageSize
        
        val itemWidth = width / columns
        val itemHeight = height / rows
        
        val startPos = currentPage * pageSize
        val endPos = minOf(startPos + pageSize, itemCount)
        
        for (i in startPos until endPos) {
            val view = recycler.getViewForPosition(i)
            addView(view)
            
            val positionInPage = i - startPos
            val col = positionInPage % columns
            val row = positionInPage / columns
            
            val left = col * itemWidth
            val top = row * itemHeight
            val right = left + itemWidth
            val bottom = top + itemHeight
            
            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, left, top, right, bottom)
        }
    }
    
    fun navigateToPosition(position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        
        val targetPage = position / pageSize
        if (targetPage != currentPage) {
            currentPage = targetPage
            requestLayout()
        }
        
        selectedPosition = position
    }
    
    fun navigateLeft(): Boolean {
        val currentPosInPage = selectedPosition % pageSize
        val currentCol = currentPosInPage % columns
        
        if (currentCol > 0) {
            // Move left within current page
            selectedPosition--
            return true
        } else if (currentPage > 0) {
            // Move to previous page, rightmost column
            currentPage--
            val newRow = currentPosInPage / columns
            selectedPosition = (currentPage * pageSize) + (newRow * columns) + (columns - 1)
            
            // Ensure we don't go past the last item
            selectedPosition = minOf(selectedPosition, itemCount - 1)
            requestLayout()
            return true
        }
        
        return false
    }
    
    fun navigateRight(): Boolean {
        val currentPosInPage = selectedPosition % pageSize
        val currentCol = currentPosInPage % columns
        
        if (currentCol < columns - 1 && selectedPosition < itemCount - 1) {
            // Move right within current page
            selectedPosition++
            return true
        } else if (currentPage < totalPages - 1) {
            // Move to next page, leftmost column
            currentPage++
            val newRow = currentPosInPage / columns
            selectedPosition = (currentPage * pageSize) + (newRow * columns)
            
            // Ensure we don't go past the last item
            selectedPosition = minOf(selectedPosition, itemCount - 1)
            requestLayout()
            return true
        }
        
        return false
    }
    
    fun navigateUp(): Boolean {
        val currentPosInPage = selectedPosition % pageSize
        val currentRow = currentPosInPage / columns
        
        if (currentRow > 0) {
            selectedPosition -= columns
            return true
        }
        
        return false
    }
    
    fun navigateDown(): Boolean {
        val currentPosInPage = selectedPosition % pageSize
        val currentRow = currentPosInPage / columns
        
        if (currentRow < rows - 1) {
            val newPos = selectedPosition + columns
            if (newPos < itemCount && newPos < ((currentPage + 1) * pageSize)) {
                selectedPosition = newPos
                return true
            }
        }
        
        return false
    }
    
    fun getSelectedPosition(): Int = selectedPosition
    
    fun getCurrentPage(): Int = currentPage
    
    fun getTotalPages(): Int = totalPages
    
    fun goToNextPage(): Boolean {
        if (currentPage < totalPages - 1) {
            currentPage++
            selectedPosition = currentPage * pageSize
            requestLayout()
            return true
        }
        return false
    }
    
    fun goToPreviousPage(): Boolean {
        if (currentPage > 0) {
            currentPage--
            selectedPosition = currentPage * pageSize
            requestLayout()
            return true
        }
        return false
    }
    
    fun resetToFirstPage() {
        if (currentPage != 0) {
            currentPage = 0
            selectedPosition = 0
            requestLayout()
        }
    }
    
    override fun canScrollHorizontally(): Boolean = false
    override fun canScrollVertically(): Boolean = false
}

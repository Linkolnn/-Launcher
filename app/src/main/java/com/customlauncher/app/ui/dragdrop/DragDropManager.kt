package com.customlauncher.app.ui.dragdrop

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.customlauncher.app.data.model.HomeItemModel
import com.customlauncher.app.data.model.AppInfo
import com.customlauncher.app.ui.layout.HomeScreenGridLayout

/**
 * Manager for drag and drop operations on home screen
 */
class DragDropManager(
    private val gridLayout: HomeScreenGridLayout,
    private val onItemMoved: (HomeItemModel, newX: Int, newY: Int) -> Unit,
    private val onItemDropped: (HomeItemModel, x: Int, y: Int) -> Unit,
    private val onAppDroppedFromDrawer: ((AppInfo, x: Int, y: Int) -> Unit)? = null
) : View.OnDragListener {
    
    companion object {
        private const val TAG = "DragDropManager"
        const val DRAG_LABEL_HOME_ITEM = "home_item"
        const val DRAG_LABEL_APP_FROM_DRAWER = "app_from_drawer"
        const val MIME_TYPE_APPLICATION = "application/home-item"
    }
    
    private var draggedItem: HomeItemModel? = null
    private var draggedView: View? = null
    private var highlightedCell: Pair<Int, Int>? = null
    private var dropTargetCell: Pair<Int, Int>? = null
    private val highlightDrawable = ColorDrawable(0x4000FF00) // Semi-transparent green
    
    init {
        // Set this as the drag listener for the grid layout
        gridLayout.setOnDragListener(this)
    }
    
    /**
     * Start drag operation for a home screen item
     */
    fun startDrag(view: View, item: HomeItemModel): Boolean {
        // Only start drag if we have a valid item
        if (view.parent == null) {
            Log.e(TAG, "Cannot start drag - view has no parent")
            return false
        }
        
        // Get the most recent item data from view tag (in case it was moved)
        val currentItem = (view.tag as? HomeItemModel) ?: item
        
        val clipData = ClipData.newPlainText(DRAG_LABEL_HOME_ITEM, currentItem.id.toString())
        val shadowBuilder = DragShadowBuilder(view)
        
        draggedItem = currentItem
        draggedView = view
        
        // Make the original view semi-transparent during drag
        view.alpha = 0.5f
        
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(clipData, shadowBuilder, currentItem, View.DRAG_FLAG_GLOBAL)
        } else {
            @Suppress("DEPRECATION")
            view.startDrag(clipData, shadowBuilder, currentItem, 0)
        }
        
        if (result) {
            Log.d(TAG, "Drag started for item ${currentItem.id} at position ${currentItem.cellX},${currentItem.cellY}")
        } else {
            Log.e(TAG, "Failed to start drag for item ${currentItem.id}")
            view.alpha = 1.0f // Restore if drag failed
        }
        
        return result
    }
    
    override fun onDrag(v: View?, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                // Check if this drag operation is for us
                return event.clipDescription?.label == DRAG_LABEL_HOME_ITEM ||
                       event.clipDescription?.label == DRAG_LABEL_APP_FROM_DRAWER
            }
            
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d(TAG, "Drag entered grid")
                return true
            }
            
            DragEvent.ACTION_DRAG_LOCATION -> {
                // Highlight the cell under the drag
                val cell = gridLayout.getCellFromPoint(event.x, event.y)
                if (cell != highlightedCell) {
                    clearHighlight()
                    cell?.let { (x, y) ->
                        highlightCell(x, y)
                    }
                    highlightedCell = cell
                }
                return true
            }
            
            DragEvent.ACTION_DRAG_EXITED -> {
                clearHighlight()
                highlightedCell = null
                return true
            }
            
            DragEvent.ACTION_DROP -> {
                val cell = gridLayout.getCellFromPoint(event.x, event.y)
                cell?.let { (x, y) ->
                    dropTargetCell = cell  // Save where item was dropped
                    handleDrop(event, x, y)
                }
                clearHighlight()
                highlightedCell = null
                return true
            }
            
            DragEvent.ACTION_DRAG_ENDED -> {
                // Restore the dragged view's alpha
                draggedView?.alpha = 1.0f
                
                // Notify about drop completion with new coordinates
                draggedItem?.let { item ->
                    val (dropX, dropY) = dropTargetCell ?: Pair(item.cellX, item.cellY)
                    onItemDropped(item, dropX, dropY)
                }
                
                if (event.result) {
                    Log.d(TAG, "Drag completed successfully")
                } else {
                    Log.d(TAG, "Drag was cancelled or failed")
                }
                
                clearHighlight()
                highlightedCell = null
                draggedItem = null
                draggedView = null
                dropTargetCell = null
                gridLayout.invalidate() // Force redraw
                return true
            }
            
            else -> return false
        }
    }
    
    private fun handleDrop(event: DragEvent, cellX: Int, cellY: Int) {
        when (val localState = event.localState) {
            is HomeItemModel -> {
                // This is a move operation within the home screen
                if (draggedItem?.id == localState.id) {
                    Log.d(TAG, "Moving item ${localState.id} from ${draggedItem?.cellX},${draggedItem?.cellY} to $cellX, $cellY")
                    // Use the draggedItem which has the current position
                    onItemMoved(draggedItem!!, cellX, cellY)
                    // Update dragged item with new coordinates
                    draggedItem = draggedItem?.copy(cellX = cellX, cellY = cellY)
                }
            }
            is AppInfo -> {
                // This is a drop from app drawer
                Log.d(TAG, "App ${localState.appName} dropped from drawer at $cellX, $cellY")
                onAppDroppedFromDrawer?.invoke(localState, cellX, cellY)
            }
            else -> {
                // Try to parse clip data for backward compatibility
                val label = event.clipDescription?.label
                if (label == DRAG_LABEL_APP_FROM_DRAWER) {
                    val clipText = event.clipData?.getItemAt(0)?.text?.toString()
                    if (clipText != null) {
                        val parts = clipText.split("|")
                        if (parts.size >= 2) {
                            val packageName = parts[0]
                            val appName = parts[1]
                            // Create a temporary AppInfo
                            // Try to get app icon, or use empty drawable
                            val appIcon = try {
                                gridLayout.context.packageManager.getApplicationIcon(packageName)
                            } catch (e: Exception) {
                                // Create empty transparent drawable instead of Android robot
                                ColorDrawable(android.graphics.Color.TRANSPARENT)
                            }
                            val appInfo = AppInfo(
                                appName = appName,
                                packageName = packageName,
                                icon = appIcon,
                                isHidden = false
                            )
                            onAppDroppedFromDrawer?.invoke(appInfo, cellX, cellY)
                        }
                    }
                }
            }
        }
    }
    
    private fun highlightCell(x: Int, y: Int) {
        // TODO: Add visual feedback for the target cell
        // For now, just log it
        Log.d(TAG, "Highlighting cell at $x, $y")
    }
    
    private fun clearHighlight() {
        highlightedCell?.let { (x, y) ->
            Log.d(TAG, "Clearing highlight at $x, $y")
        }
    }
    
    /**
     * Custom drag shadow builder
     */
    class DragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        
        override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
            val width = view.width
            val height = view.height
            
            outShadowSize.set(width, height)
            outShadowTouchPoint.set(width / 2, height / 2)
        }
        
        override fun onDrawShadow(canvas: Canvas) {
            // Make the shadow slightly transparent
            canvas.save()
            canvas.scale(0.9f, 0.9f, view.width / 2f, view.height / 2f)
            view.draw(canvas)
            canvas.restore()
        }
    }
}

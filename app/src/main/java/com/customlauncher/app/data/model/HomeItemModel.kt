package com.customlauncher.app.data.model

/**
 * Model representing an item on the home screen (app shortcut or widget)
 */
data class HomeItemModel(
    val id: Long = 0,
    val type: ItemType,
    val packageName: String? = null,
    val componentName: String? = null,
    val label: String? = null,
    val widgetId: Int? = null,
    val cellX: Int,
    val cellY: Int,
    val spanX: Int = 1,
    val spanY: Int = 1,
    val screen: Int = 0, // For future multi-screen support
    val position: Int = -1 // Order within cell for stacked items
) {
    enum class ItemType {
        APP,        // Application shortcut
        WIDGET,     // Android widget
        FOLDER,     // Folder containing apps (future)
        SHORTCUT    // Custom shortcut (future)
    }
    
    /**
     * Check if this item overlaps with another item
     */
    fun overlaps(other: HomeItemModel): Boolean {
        if (screen != other.screen) return false
        
        val thisRight = cellX + spanX
        val thisBottom = cellY + spanY
        val otherRight = other.cellX + other.spanX
        val otherBottom = other.cellY + other.spanY
        
        return !(cellX >= otherRight || thisRight <= other.cellX ||
                cellY >= otherBottom || thisBottom <= other.cellY)
    }
    
    /**
     * Get all cells occupied by this item
     */
    fun getOccupiedCells(): List<Pair<Int, Int>> {
        val cells = mutableListOf<Pair<Int, Int>>()
        for (x in cellX until cellX + spanX) {
            for (y in cellY until cellY + spanY) {
                cells.add(Pair(x, y))
            }
        }
        return cells
    }
    
    companion object {
        fun createAppShortcut(
            packageName: String,
            componentName: String,
            label: String,
            x: Int,
            y: Int
        ): HomeItemModel {
            return HomeItemModel(
                type = ItemType.APP,
                packageName = packageName,
                componentName = componentName,
                label = label,
                cellX = x,
                cellY = y
            )
        }
        
        fun createWidget(
            widgetId: Int,
            componentName: String,
            x: Int,
            y: Int,
            spanX: Int,
            spanY: Int
        ): HomeItemModel {
            return HomeItemModel(
                type = ItemType.WIDGET,
                widgetId = widgetId,
                componentName = componentName,
                cellX = x,
                cellY = y,
                spanX = spanX,
                spanY = spanY
            )
        }
    }
}

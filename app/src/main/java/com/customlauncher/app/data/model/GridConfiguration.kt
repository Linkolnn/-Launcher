package com.customlauncher.app.data.model

/**
 * Configuration for home screen grid layout
 */
data class GridConfiguration(
    val columns: Int,
    val rows: Int,
    val isButtonMode: Boolean = false
) {
    val totalCells: Int
        get() = columns * rows
    
    fun isValidPosition(x: Int, y: Int): Boolean {
        return x in 0 until columns && y in 0 until rows
    }
    
    fun getCellIndex(x: Int, y: Int): Int {
        return y * columns + x
    }
    
    fun getCellCoordinates(index: Int): Pair<Int, Int> {
        val x = index % columns
        val y = index / columns
        return Pair(x, y)
    }
    
    fun canFitItem(x: Int, y: Int, spanX: Int, spanY: Int): Boolean {
        if (!isValidPosition(x, y)) return false
        if (x + spanX > columns) return false
        if (y + spanY > rows) return false
        return true
    }
    
    companion object {
        fun getDefault(isButtonMode: Boolean = false): GridConfiguration {
            return if (isButtonMode) {
                GridConfiguration(3, 3, true) // Button phone default
            } else {
                GridConfiguration(4, 6, false) // Touch screen default
            }
        }
        
        fun fromPreferences(preferences: com.customlauncher.app.data.preferences.LauncherPreferences): GridConfiguration {
            // Use homeScreenMode (0 = touch, 1 = button) instead of buttonPhoneMode
            val isButtonMode = preferences.homeScreenMode == 1
            
            return if (isButtonMode) {
                GridConfiguration(
                    preferences.homeScreenGridColumnsButton,
                    preferences.homeScreenGridRowsButton,
                    true
                )
            } else {
                GridConfiguration(
                    preferences.homeScreenGridColumns,
                    preferences.homeScreenGridRows,
                    false
                )
            }
        }
    }
}

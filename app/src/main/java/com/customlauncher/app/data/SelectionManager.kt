package com.customlauncher.app.data

object SelectionManager {
    // Temporary selection state that persists across activity changes
    private val selectedApps = mutableSetOf<String>()
    
    @Synchronized
    fun addSelection(packageName: String) {
        selectedApps.add(packageName)
    }
    
    @Synchronized
    fun removeSelection(packageName: String) {
        selectedApps.remove(packageName)
    }
    
    @Synchronized
    fun setSelection(packageNames: Set<String>) {
        selectedApps.clear()
        selectedApps.addAll(packageNames)
    }
    
    @Synchronized
    fun getSelection(): Set<String> {
        return selectedApps.toSet()
    }
    
    @Synchronized
    fun clearSelection() {
        selectedApps.clear()
    }
    
    @Synchronized
    fun isSelected(packageName: String): Boolean {
        return selectedApps.contains(packageName)
    }
    
    @Synchronized
    fun hasSelection(): Boolean {
        return selectedApps.isNotEmpty()
    }
}

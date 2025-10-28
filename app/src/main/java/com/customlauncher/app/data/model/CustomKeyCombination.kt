package com.customlauncher.app.data.model

data class CustomKeyCombination(
    val keys: List<Int>,  // List of key codes in order
    val timeoutMs: Long = 2000L  // Timeout between key presses
) {
    fun toStorageString(): String {
        return keys.joinToString(",")
    }
    
    companion object {
        fun fromStorageString(str: String?): CustomKeyCombination? {
            if (str.isNullOrEmpty()) return null
            
            return try {
                val keys = str.split(",").mapNotNull { it.toIntOrNull() }
                if (keys.isEmpty()) null else CustomKeyCombination(keys)
            } catch (e: Exception) {
                null
            }
        }
    }
}

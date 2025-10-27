package com.customlauncher.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.customlauncher.app.data.model.KeyCombination

class LauncherPreferences(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val sharedPreferences: SharedPreferences = prefs
    private val context: Context = context
    
    // Thread-safe cache for hidden apps
    @Volatile
    private var hiddenAppsCache: Set<String>? = null
    private val cacheLock = Any()
    
    var keyCombination: KeyCombination
        get() {
            val id = prefs.getInt(KEY_COMBINATION, KeyCombination.VOL_DOWN_LONG.id)
            android.util.Log.d("LauncherPreferences", "Getting keyCombination: id=$id")
            return KeyCombination.fromId(id)
        }
        set(value) {
            android.util.Log.d("LauncherPreferences", "Setting keyCombination: ${value.name} (id=${value.id})")
            prefs.edit().putInt(KEY_COMBINATION, value.id).commit() // Use commit() for immediate save
        }
    
    var appsHidden: Boolean
        get() = prefs.getBoolean(KEY_APPS_HIDDEN, false)
        set(value) = prefs.edit().putBoolean(KEY_APPS_HIDDEN, value).apply()
    
    var iconPackPackageName: String?
        get() = prefs.getString(KEY_ICON_PACK, null)
        set(value) = prefs.edit().putString(KEY_ICON_PACK, value).apply()
    
    fun getHiddenApps(): Set<String> {
        synchronized(cacheLock) {
            if (hiddenAppsCache == null) {
                hiddenAppsCache = sharedPreferences.getStringSet(KEY_HIDDEN_APPS, emptySet())?.toSet() ?: emptySet()
            }
            return hiddenAppsCache!!
        }
    }
    
    fun addHiddenApp(packageName: String) {
        synchronized(cacheLock) {
            val apps = getHiddenApps().toMutableSet()
            apps.add(packageName)
            saveHiddenAppsInternal(apps)
        }
    }
    
    fun removeHiddenApp(packageName: String) {
        synchronized(cacheLock) {
            val apps = getHiddenApps().toMutableSet()
            apps.remove(packageName)
            saveHiddenAppsInternal(apps)
        }
    }
    
    fun setHiddenApps(apps: Set<String>) {
        synchronized(cacheLock) {
            saveHiddenAppsInternal(apps)
        }
    }
    
    fun clearHiddenApps() {
        synchronized(cacheLock) {
            saveHiddenAppsInternal(emptySet())
        }
    }
    
    private fun saveHiddenAppsInternal(apps: Set<String>) {
        // Create completely new set to avoid SharedPreferences bug
        val newSet = HashSet(apps)
        
        // Update cache first
        hiddenAppsCache = newSet
        
        // Then save to disk with commit() for immediate persistence
        val editor = sharedPreferences.edit()
        editor.remove(KEY_HIDDEN_APPS)
        editor.putStringSet(KEY_HIDDEN_APPS, newSet)
        
        // Use commit() on background thread for Android 11 compatibility
        Thread {
            editor.commit()
        }.start()
    }
    
    fun areAppsHidden(): Boolean = appsHidden
    
    fun isSensorActive(): Boolean = touchScreenBlocked
    
    var touchScreenBlocked: Boolean
        get() = prefs.getBoolean(KEY_TOUCH_BLOCKED, false)
        set(value) = prefs.edit().putBoolean(KEY_TOUCH_BLOCKED, value).apply()
    
    var defaultHomeScreen: String
        get() = prefs.getString(KEY_DEFAULT_HOME, "standard") ?: "standard"
        set(value) = prefs.edit().putString(KEY_DEFAULT_HOME, value).apply()
    
    var selectedKeyCombination: KeyCombination
        get() {
            val ordinal = prefs.getInt(KEY_SELECTED_COMBINATION, KeyCombination.BOTH_VOLUME.ordinal)
            return KeyCombination.values().getOrNull(ordinal) ?: KeyCombination.BOTH_VOLUME
        }
        set(value) = prefs.edit().putInt(KEY_SELECTED_COMBINATION, value.ordinal).apply()
    
    var gridColumnCount: Int
        get() = prefs.getInt(KEY_GRID_COLUMNS, 4)
        set(value) {
            if (value in 3..5) {
                prefs.edit().putInt(KEY_GRID_COLUMNS, value).apply()
            }
        }
    
    companion object {
        private const val PREFS_NAME = "launcher_preferences"
        private const val KEY_COMBINATION = "key_combination"
        private const val KEY_APPS_HIDDEN = "apps_hidden"
        private const val KEY_ICON_PACK = "icon_pack"
        private const val KEY_HIDDEN_APPS = "hidden_apps"
        private const val KEY_TOUCH_BLOCKED = "touch_blocked"
        private const val KEY_DEFAULT_HOME = "default_home_screen"
        private const val KEY_SELECTED_COMBINATION = "selected_key_combination"
        private const val KEY_GRID_COLUMNS = "grid_columns"
    }
}

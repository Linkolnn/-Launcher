package com.customlauncher.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class LauncherPreferences(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val sharedPreferences: SharedPreferences = prefs
    private val context: Context = context
    
    // Thread-safe cache for hidden apps
    @Volatile
    private var hiddenAppsCache: Set<String>? = null
    private val cacheLock = Any()
    
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
    
    var gridColumnCount: Int
        get() {
            val count = prefs.getInt(KEY_GRID_COLUMNS, 0) // 0 means not set
            // Return 0 if not set, otherwise validate range
            return when {
                count == 0 -> 0  // Not set yet
                count < 3 -> 3
                count > 5 -> 5
                else -> count
            }
        }
        set(value) {
            val validValue = when {
                value < 3 -> 3
                value > 5 -> 5
                else -> value
            }
            prefs.edit().putInt(KEY_GRID_COLUMNS, validValue).apply()
        }
    
    var customKeyCombination: String?
        get() = prefs.getString(KEY_CUSTOM_KEY_COMBINATION, null)
        set(value) = prefs.edit().putString(KEY_CUSTOM_KEY_COMBINATION, value).apply()
    
    var useCustomKeys: Boolean
        get() = prefs.getBoolean(KEY_USE_CUSTOM_KEYS, false)
        set(value) = prefs.edit().putBoolean(KEY_USE_CUSTOM_KEYS, value).apply()
    
    // New feature toggles
    var closeAppsOnHiddenMode: Boolean
        get() = prefs.getBoolean(KEY_CLOSE_APPS_ON_HIDDEN, true)  // Default: enabled
        set(value) = prefs.edit().putBoolean(KEY_CLOSE_APPS_ON_HIDDEN, value).apply()
    
    var blockTouchInHiddenMode: Boolean
        get() = prefs.getBoolean(KEY_BLOCK_TOUCH_IN_HIDDEN, true)  // Default: enabled
        set(value) = prefs.edit().putBoolean(KEY_BLOCK_TOUCH_IN_HIDDEN, value).apply()
    
    var enableDndInHiddenMode: Boolean
        get() = prefs.getBoolean(KEY_ENABLE_DND_IN_HIDDEN, true)  // Default: enabled
        set(value) = prefs.edit().putBoolean(KEY_ENABLE_DND_IN_HIDDEN, value).apply()
    
    var hideAppsInHiddenMode: Boolean
        get() = prefs.getBoolean(KEY_HIDE_APPS_IN_HIDDEN, true)  // Default: enabled
        set(value) = prefs.edit().putBoolean(KEY_HIDE_APPS_IN_HIDDEN, value).apply()
    
    var blockScreenshotsInHiddenMode: Boolean
        get() = prefs.getBoolean(KEY_BLOCK_SCREENSHOTS_IN_HIDDEN, true)  // Default: enabled
        set(value) = prefs.edit().putBoolean(KEY_BLOCK_SCREENSHOTS_IN_HIDDEN, value).apply()
    
    var checkPermissionsOnStartup: Boolean
        get() = prefs.getBoolean(KEY_CHECK_PERMISSIONS, true)  // Default: enabled
        set(value) = prefs.edit().putBoolean(KEY_CHECK_PERMISSIONS, value).apply()
    
    var buttonPhoneMode: Boolean
        get() = prefs.getBoolean(KEY_BUTTON_PHONE_MODE, false)  // Default: disabled
        set(value) = prefs.edit().putBoolean(KEY_BUTTON_PHONE_MODE, value).apply()
    
    var buttonPhoneGridSize: String
        get() = prefs.getString(KEY_BUTTON_PHONE_GRID_SIZE, "") ?: ""  // Empty by default
        set(value) = prefs.edit().putString(KEY_BUTTON_PHONE_GRID_SIZE, value).apply()
    
    var hasTouchGridSelection: Boolean
        get() = prefs.getBoolean(KEY_HAS_TOUCH_GRID_SELECTION, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_TOUCH_GRID_SELECTION, value).apply()
    
    var hasButtonGridSelection: Boolean
        get() = prefs.getBoolean(KEY_HAS_BUTTON_GRID_SELECTION, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_BUTTON_GRID_SELECTION, value).apply()
    
    var showAppLabels: Boolean
        get() = prefs.getBoolean(KEY_SHOW_APP_LABELS, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_APP_LABELS, value).apply()
    
    companion object {
        private const val PREFS_NAME = "launcher_preferences"
        private const val KEY_APPS_HIDDEN = "apps_hidden"
        private const val KEY_ICON_PACK = "icon_pack"
        private const val KEY_HIDDEN_APPS = "hidden_apps"
        private const val KEY_TOUCH_BLOCKED = "touch_blocked"
        private const val KEY_DEFAULT_HOME = "default_home_screen"
        private const val KEY_GRID_COLUMNS = "grid_columns"
        private const val KEY_CUSTOM_KEY_COMBINATION = "custom_key_combination"
        private const val KEY_USE_CUSTOM_KEYS = "use_custom_keys"
        private const val KEY_CLOSE_APPS_ON_HIDDEN = "close_apps_on_hidden"
        private const val KEY_BLOCK_TOUCH_IN_HIDDEN = "block_touch_in_hidden"
        private const val KEY_ENABLE_DND_IN_HIDDEN = "enable_dnd_in_hidden"
        private const val KEY_HIDE_APPS_IN_HIDDEN = "hide_apps_in_hidden"
        private const val KEY_BLOCK_SCREENSHOTS_IN_HIDDEN = "block_screenshots_in_hidden"
        private const val KEY_CHECK_PERMISSIONS = "check_permissions_on_startup"
        private const val KEY_BUTTON_PHONE_MODE = "button_phone_mode"
        private const val KEY_BUTTON_PHONE_GRID_SIZE = "button_phone_grid_size"
        private const val KEY_HAS_TOUCH_GRID_SELECTION = "has_touch_grid_selection"
        private const val KEY_HAS_BUTTON_GRID_SELECTION = "has_button_grid_selection"
        private const val KEY_SHOW_APP_LABELS = "show_app_labels"
    }
}

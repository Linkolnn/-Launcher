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
    
    // Icon pack preferences
    var selectedIconPack: String?
        get() = prefs.getString("selected_icon_pack", null)
        set(value) = prefs.edit().putString("selected_icon_pack", value).apply()
    
    // Icon scaling preferences
    var iconScaleMode: String
        get() = prefs.getString("icon_scale_mode", "auto") ?: "auto"
        set(value) = prefs.edit().putString("icon_scale_mode", value).apply()
    
    var customIconScale: Float
        get() = prefs.getFloat("custom_icon_scale", 1.0f)
        set(value) = prefs.edit().putFloat("custom_icon_scale", value).apply()
    
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
    
    var showAppSearch: Boolean
        get() = prefs.getBoolean(KEY_SHOW_APP_SEARCH, false)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_APP_SEARCH, value).apply()
    
    var appMenuSelectedTab: String
        get() {
            // Migration: handle old int values
            return try {
                prefs.getString(KEY_APP_MENU_SELECTED_TAB, "touch") ?: "touch"
            } catch (e: ClassCastException) {
                // If it was saved as int, migrate it
                val intValue = prefs.getInt(KEY_APP_MENU_SELECTED_TAB, 0)
                val stringValue = if (intValue == 1) "button" else "touch"
                // Save as string
                prefs.edit().putString(KEY_APP_MENU_SELECTED_TAB, stringValue).apply()
                stringValue
            }
        }
        set(value) = prefs.edit().putString(KEY_APP_MENU_SELECTED_TAB, value).apply()
    
    // Home screen settings
    var showHomeScreen: Boolean
        get() = prefs.getBoolean(KEY_SHOW_HOME_SCREEN, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_HOME_SCREEN, value).apply()
    
    var menuAccessMethod: String
        get() = prefs.getString(KEY_MENU_ACCESS_METHOD, "dpad_down") ?: "dpad_down"
        set(value) = prefs.edit().putString(KEY_MENU_ACCESS_METHOD, value).apply()
    
    var homeScreenGridColumns: Int
        get() = prefs.getInt(KEY_HOME_SCREEN_GRID_COLUMNS, 4)
        set(value) = prefs.edit().putInt(KEY_HOME_SCREEN_GRID_COLUMNS, value).apply()
    
    var homeScreenGridRows: Int
        get() = prefs.getInt(KEY_HOME_SCREEN_GRID_ROWS, 6)
        set(value) = prefs.edit().putInt(KEY_HOME_SCREEN_GRID_ROWS, value).apply()
    
    var homeScreenGridColumnsButton: Int
        get() = prefs.getInt(KEY_HOME_SCREEN_GRID_COLUMNS_BUTTON, 3)
        set(value) = prefs.edit().putInt(KEY_HOME_SCREEN_GRID_COLUMNS_BUTTON, value).apply()
    
    var homeScreenGridRowsButton: Int
        get() = prefs.getInt(KEY_HOME_SCREEN_GRID_ROWS_BUTTON, 3)
        set(value) = prefs.edit().putInt(KEY_HOME_SCREEN_GRID_ROWS_BUTTON, value).apply()
    
    var homeScreenMode: Int
        get() = prefs.getInt(KEY_HOME_SCREEN_MODE, 0) // Default to TOUCH mode
        set(value) = prefs.edit().putInt(KEY_HOME_SCREEN_MODE, value).apply()
    
    var gridNeedsUpdate: Boolean
        get() = prefs.getBoolean(KEY_GRID_NEEDS_UPDATE, false)
        set(value) = prefs.edit().putBoolean(KEY_GRID_NEEDS_UPDATE, value).apply()
    
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
        private const val KEY_SHOW_APP_SEARCH = "show_app_search"
        private const val KEY_APP_MENU_SELECTED_TAB = "app_menu_selected_tab"
        private const val KEY_SHOW_HOME_SCREEN = "show_home_screen"
        private const val KEY_MENU_ACCESS_METHOD = "menu_access_method"
        private const val KEY_HOME_SCREEN_GRID_COLUMNS = "home_screen_grid_columns"
        private const val KEY_HOME_SCREEN_GRID_ROWS = "home_screen_grid_rows"
        private const val KEY_HOME_SCREEN_GRID_COLUMNS_BUTTON = "home_screen_grid_columns_button"
        private const val KEY_HOME_SCREEN_GRID_ROWS_BUTTON = "home_screen_grid_rows_button"
        private const val KEY_HOME_SCREEN_MODE = "home_screen_mode"
        private const val KEY_GRID_NEEDS_UPDATE = "grid_needs_update"
    }
}

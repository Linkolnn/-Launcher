package com.customlauncher.app.data.preferences;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0018\n\u0002\u0010\u0007\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\"\n\u0002\b\u001c\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u0002\n\u0002\b\u000b\u0018\u0000 \u0083\u00012\u00020\u0001:\u0002\u0083\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010x\u001a\u00020y2\u0006\u0010z\u001a\u00020\u0006J\u0006\u0010{\u001a\u00020\fJ\u0006\u0010|\u001a\u00020yJ\f\u0010}\u001a\b\u0012\u0004\u0012\u00020\u00060DJ\u0006\u0010~\u001a\u00020\fJ\u000e\u0010\u007f\u001a\u00020y2\u0006\u0010z\u001a\u00020\u0006J\u0018\u0010\u0080\u0001\u001a\u00020y2\r\u0010\u0081\u0001\u001a\b\u0012\u0004\u0012\u00020\u00060DH\u0002J\u0016\u0010\u0082\u0001\u001a\u00020y2\r\u0010\u0081\u0001\u001a\b\u0012\u0004\u0012\u00020\u00060DR$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR$\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R$\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0013\u0010\u000f\"\u0004\b\u0014\u0010\u0011R$\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011R$\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0019\u0010\t\"\u0004\b\u001a\u0010\u000bR$\u0010\u001b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001c\u0010\u000f\"\u0004\b\u001d\u0010\u0011R\u000e\u0010\u001e\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\u001f\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b \u0010\u000f\"\u0004\b!\u0010\u0011R$\u0010\"\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b#\u0010\u000f\"\u0004\b$\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010&\u001a\u00020%2\u0006\u0010\u0005\u001a\u00020%8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\'\u0010(\"\u0004\b)\u0010*R(\u0010+\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b,\u0010\t\"\u0004\b-\u0010\u000bR$\u0010.\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b/\u0010\t\"\u0004\b0\u0010\u000bR$\u00101\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b2\u0010\u000f\"\u0004\b3\u0010\u0011R$\u00105\u001a\u0002042\u0006\u0010\u0005\u001a\u0002048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b6\u00107\"\u0004\b8\u00109R$\u0010:\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b;\u0010\u000f\"\u0004\b<\u0010\u0011R$\u0010=\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b>\u0010\u000f\"\u0004\b?\u0010\u0011R$\u0010@\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bA\u0010\u000f\"\u0004\bB\u0010\u0011R\u0016\u0010C\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010DX\u0082\u000e\u00a2\u0006\u0002\n\u0000R$\u0010E\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bF\u0010\u000f\"\u0004\bG\u0010\u0011R$\u0010H\u001a\u0002042\u0006\u0010\u0005\u001a\u0002048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bI\u00107\"\u0004\bJ\u00109R$\u0010K\u001a\u0002042\u0006\u0010\u0005\u001a\u0002048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bL\u00107\"\u0004\bM\u00109R$\u0010N\u001a\u0002042\u0006\u0010\u0005\u001a\u0002048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bO\u00107\"\u0004\bP\u00109R$\u0010Q\u001a\u0002042\u0006\u0010\u0005\u001a\u0002048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bR\u00107\"\u0004\bS\u00109R$\u0010T\u001a\u0002042\u0006\u0010\u0005\u001a\u0002048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bU\u00107\"\u0004\bV\u00109R(\u0010W\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bX\u0010\t\"\u0004\bY\u0010\u000bR$\u0010Z\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b[\u0010\t\"\u0004\b\\\u0010\u000bR$\u0010]\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b^\u0010\t\"\u0004\b_\u0010\u000bR\u000e\u0010`\u001a\u00020aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R(\u0010b\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bc\u0010\t\"\u0004\bd\u0010\u000bR\u000e\u0010e\u001a\u00020aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010f\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bg\u0010\u000f\"\u0004\bh\u0010\u0011R$\u0010i\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bj\u0010\u000f\"\u0004\bk\u0010\u0011R$\u0010l\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bm\u0010\u000f\"\u0004\bn\u0010\u0011R$\u0010o\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bp\u0010\u000f\"\u0004\bq\u0010\u0011R$\u0010r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bs\u0010\u000f\"\u0004\bt\u0010\u0011R$\u0010u\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\bv\u0010\u000f\"\u0004\bw\u0010\u0011\u00a8\u0006\u0084\u0001"}, d2 = {"Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "appMenuSelectedTab", "getAppMenuSelectedTab", "()Ljava/lang/String;", "setAppMenuSelectedTab", "(Ljava/lang/String;)V", "", "appsHidden", "getAppsHidden", "()Z", "setAppsHidden", "(Z)V", "blockScreenshotsInHiddenMode", "getBlockScreenshotsInHiddenMode", "setBlockScreenshotsInHiddenMode", "blockTouchInHiddenMode", "getBlockTouchInHiddenMode", "setBlockTouchInHiddenMode", "buttonPhoneGridSize", "getButtonPhoneGridSize", "setButtonPhoneGridSize", "buttonPhoneMode", "getButtonPhoneMode", "setButtonPhoneMode", "cacheLock", "checkPermissionsOnStartup", "getCheckPermissionsOnStartup", "setCheckPermissionsOnStartup", "closeAppsOnHiddenMode", "getCloseAppsOnHiddenMode", "setCloseAppsOnHiddenMode", "", "customIconScale", "getCustomIconScale", "()F", "setCustomIconScale", "(F)V", "customKeyCombination", "getCustomKeyCombination", "setCustomKeyCombination", "defaultHomeScreen", "getDefaultHomeScreen", "setDefaultHomeScreen", "enableDndInHiddenMode", "getEnableDndInHiddenMode", "setEnableDndInHiddenMode", "", "gridColumnCount", "getGridColumnCount", "()I", "setGridColumnCount", "(I)V", "gridNeedsUpdate", "getGridNeedsUpdate", "setGridNeedsUpdate", "hasButtonGridSelection", "getHasButtonGridSelection", "setHasButtonGridSelection", "hasTouchGridSelection", "getHasTouchGridSelection", "setHasTouchGridSelection", "hiddenAppsCache", "", "hideAppsInHiddenMode", "getHideAppsInHiddenMode", "setHideAppsInHiddenMode", "homeScreenGridColumns", "getHomeScreenGridColumns", "setHomeScreenGridColumns", "homeScreenGridColumnsButton", "getHomeScreenGridColumnsButton", "setHomeScreenGridColumnsButton", "homeScreenGridRows", "getHomeScreenGridRows", "setHomeScreenGridRows", "homeScreenGridRowsButton", "getHomeScreenGridRowsButton", "setHomeScreenGridRowsButton", "homeScreenMode", "getHomeScreenMode", "setHomeScreenMode", "iconPackPackageName", "getIconPackPackageName", "setIconPackPackageName", "iconScaleMode", "getIconScaleMode", "setIconScaleMode", "menuAccessMethod", "getMenuAccessMethod", "setMenuAccessMethod", "prefs", "Landroid/content/SharedPreferences;", "selectedIconPack", "getSelectedIconPack", "setSelectedIconPack", "sharedPreferences", "showAppLabels", "getShowAppLabels", "setShowAppLabels", "showAppSearch", "getShowAppSearch", "setShowAppSearch", "showHomeScreen", "getShowHomeScreen", "setShowHomeScreen", "showPhoneButtons", "getShowPhoneButtons", "setShowPhoneButtons", "touchScreenBlocked", "getTouchScreenBlocked", "setTouchScreenBlocked", "useCustomKeys", "getUseCustomKeys", "setUseCustomKeys", "addHiddenApp", "", "packageName", "areAppsHidden", "clearHiddenApps", "getHiddenApps", "isSensorActive", "removeHiddenApp", "saveHiddenAppsInternal", "apps", "setHiddenApps", "Companion", "app_debug"})
public final class LauncherPreferences {
    @org.jetbrains.annotations.NotNull
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull
    private final android.content.SharedPreferences sharedPreferences = null;
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.Nullable
    private volatile java.util.Set<java.lang.String> hiddenAppsCache;
    @org.jetbrains.annotations.NotNull
    private final java.lang.Object cacheLock = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PREFS_NAME = "launcher_preferences";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_APPS_HIDDEN = "apps_hidden";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_ICON_PACK = "icon_pack";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HIDDEN_APPS = "hidden_apps";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_TOUCH_BLOCKED = "touch_blocked";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_DEFAULT_HOME = "default_home_screen";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_GRID_COLUMNS = "grid_columns";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_CUSTOM_KEY_COMBINATION = "custom_key_combination";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_USE_CUSTOM_KEYS = "use_custom_keys";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_CLOSE_APPS_ON_HIDDEN = "close_apps_on_hidden";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_BLOCK_TOUCH_IN_HIDDEN = "block_touch_in_hidden";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_ENABLE_DND_IN_HIDDEN = "enable_dnd_in_hidden";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HIDE_APPS_IN_HIDDEN = "hide_apps_in_hidden";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_BLOCK_SCREENSHOTS_IN_HIDDEN = "block_screenshots_in_hidden";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_CHECK_PERMISSIONS = "check_permissions_on_startup";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_BUTTON_PHONE_MODE = "button_phone_mode";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_BUTTON_PHONE_GRID_SIZE = "button_phone_grid_size";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HAS_TOUCH_GRID_SELECTION = "has_touch_grid_selection";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HAS_BUTTON_GRID_SELECTION = "has_button_grid_selection";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SHOW_APP_LABELS = "show_app_labels";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SHOW_APP_SEARCH = "show_app_search";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_APP_MENU_SELECTED_TAB = "app_menu_selected_tab";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SHOW_HOME_SCREEN = "show_home_screen";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_MENU_ACCESS_METHOD = "menu_access_method";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HOME_SCREEN_GRID_COLUMNS = "home_screen_grid_columns";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HOME_SCREEN_GRID_ROWS = "home_screen_grid_rows";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HOME_SCREEN_GRID_COLUMNS_BUTTON = "home_screen_grid_columns_button";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HOME_SCREEN_GRID_ROWS_BUTTON = "home_screen_grid_rows_button";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HOME_SCREEN_MODE = "home_screen_mode";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_GRID_NEEDS_UPDATE = "grid_needs_update";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SHOW_PHONE_BUTTONS = "show_phone_buttons";
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.data.preferences.LauncherPreferences.Companion Companion = null;
    
    public LauncherPreferences(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final boolean getAppsHidden() {
        return false;
    }
    
    public final void setAppsHidden(boolean value) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getSelectedIconPack() {
        return null;
    }
    
    public final void setSelectedIconPack(@org.jetbrains.annotations.Nullable
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getIconScaleMode() {
        return null;
    }
    
    public final void setIconScaleMode(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    public final float getCustomIconScale() {
        return 0.0F;
    }
    
    public final void setCustomIconScale(float value) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getIconPackPackageName() {
        return null;
    }
    
    public final void setIconPackPackageName(@org.jetbrains.annotations.Nullable
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Set<java.lang.String> getHiddenApps() {
        return null;
    }
    
    public final void addHiddenApp(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
    }
    
    public final void removeHiddenApp(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
    }
    
    public final void setHiddenApps(@org.jetbrains.annotations.NotNull
    java.util.Set<java.lang.String> apps) {
    }
    
    public final void clearHiddenApps() {
    }
    
    private final void saveHiddenAppsInternal(java.util.Set<java.lang.String> apps) {
    }
    
    public final boolean areAppsHidden() {
        return false;
    }
    
    public final boolean isSensorActive() {
        return false;
    }
    
    public final boolean getTouchScreenBlocked() {
        return false;
    }
    
    public final void setTouchScreenBlocked(boolean value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDefaultHomeScreen() {
        return null;
    }
    
    public final void setDefaultHomeScreen(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    public final int getGridColumnCount() {
        return 0;
    }
    
    public final void setGridColumnCount(int value) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getCustomKeyCombination() {
        return null;
    }
    
    public final void setCustomKeyCombination(@org.jetbrains.annotations.Nullable
    java.lang.String value) {
    }
    
    public final boolean getUseCustomKeys() {
        return false;
    }
    
    public final void setUseCustomKeys(boolean value) {
    }
    
    public final boolean getCloseAppsOnHiddenMode() {
        return false;
    }
    
    public final void setCloseAppsOnHiddenMode(boolean value) {
    }
    
    public final boolean getBlockTouchInHiddenMode() {
        return false;
    }
    
    public final void setBlockTouchInHiddenMode(boolean value) {
    }
    
    public final boolean getEnableDndInHiddenMode() {
        return false;
    }
    
    public final void setEnableDndInHiddenMode(boolean value) {
    }
    
    public final boolean getHideAppsInHiddenMode() {
        return false;
    }
    
    public final void setHideAppsInHiddenMode(boolean value) {
    }
    
    public final boolean getBlockScreenshotsInHiddenMode() {
        return false;
    }
    
    public final void setBlockScreenshotsInHiddenMode(boolean value) {
    }
    
    public final boolean getCheckPermissionsOnStartup() {
        return false;
    }
    
    public final void setCheckPermissionsOnStartup(boolean value) {
    }
    
    public final boolean getButtonPhoneMode() {
        return false;
    }
    
    public final void setButtonPhoneMode(boolean value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getButtonPhoneGridSize() {
        return null;
    }
    
    public final void setButtonPhoneGridSize(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    public final boolean getHasTouchGridSelection() {
        return false;
    }
    
    public final void setHasTouchGridSelection(boolean value) {
    }
    
    public final boolean getHasButtonGridSelection() {
        return false;
    }
    
    public final void setHasButtonGridSelection(boolean value) {
    }
    
    public final boolean getShowAppLabels() {
        return false;
    }
    
    public final void setShowAppLabels(boolean value) {
    }
    
    public final boolean getShowAppSearch() {
        return false;
    }
    
    public final void setShowAppSearch(boolean value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAppMenuSelectedTab() {
        return null;
    }
    
    public final void setAppMenuSelectedTab(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    public final boolean getShowHomeScreen() {
        return false;
    }
    
    public final void setShowHomeScreen(boolean value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMenuAccessMethod() {
        return null;
    }
    
    public final void setMenuAccessMethod(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    public final int getHomeScreenGridColumns() {
        return 0;
    }
    
    public final void setHomeScreenGridColumns(int value) {
    }
    
    public final int getHomeScreenGridRows() {
        return 0;
    }
    
    public final void setHomeScreenGridRows(int value) {
    }
    
    public final int getHomeScreenGridColumnsButton() {
        return 0;
    }
    
    public final void setHomeScreenGridColumnsButton(int value) {
    }
    
    public final int getHomeScreenGridRowsButton() {
        return 0;
    }
    
    public final void setHomeScreenGridRowsButton(int value) {
    }
    
    public final int getHomeScreenMode() {
        return 0;
    }
    
    public final void setHomeScreenMode(int value) {
    }
    
    public final boolean getGridNeedsUpdate() {
        return false;
    }
    
    public final void setGridNeedsUpdate(boolean value) {
    }
    
    public final boolean getShowPhoneButtons() {
        return false;
    }
    
    public final void setShowPhoneButtons(boolean value) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u001f\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/customlauncher/app/data/preferences/LauncherPreferences$Companion;", "", "()V", "KEY_APPS_HIDDEN", "", "KEY_APP_MENU_SELECTED_TAB", "KEY_BLOCK_SCREENSHOTS_IN_HIDDEN", "KEY_BLOCK_TOUCH_IN_HIDDEN", "KEY_BUTTON_PHONE_GRID_SIZE", "KEY_BUTTON_PHONE_MODE", "KEY_CHECK_PERMISSIONS", "KEY_CLOSE_APPS_ON_HIDDEN", "KEY_CUSTOM_KEY_COMBINATION", "KEY_DEFAULT_HOME", "KEY_ENABLE_DND_IN_HIDDEN", "KEY_GRID_COLUMNS", "KEY_GRID_NEEDS_UPDATE", "KEY_HAS_BUTTON_GRID_SELECTION", "KEY_HAS_TOUCH_GRID_SELECTION", "KEY_HIDDEN_APPS", "KEY_HIDE_APPS_IN_HIDDEN", "KEY_HOME_SCREEN_GRID_COLUMNS", "KEY_HOME_SCREEN_GRID_COLUMNS_BUTTON", "KEY_HOME_SCREEN_GRID_ROWS", "KEY_HOME_SCREEN_GRID_ROWS_BUTTON", "KEY_HOME_SCREEN_MODE", "KEY_ICON_PACK", "KEY_MENU_ACCESS_METHOD", "KEY_SHOW_APP_LABELS", "KEY_SHOW_APP_SEARCH", "KEY_SHOW_HOME_SCREEN", "KEY_SHOW_PHONE_BUTTONS", "KEY_TOUCH_BLOCKED", "KEY_USE_CUSTOM_KEYS", "PREFS_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
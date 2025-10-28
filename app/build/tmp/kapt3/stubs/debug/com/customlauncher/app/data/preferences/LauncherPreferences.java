package com.customlauncher.app.data.preferences;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u000b\u0018\u0000 A2\u00020\u0001:\u0001AB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u00106\u001a\u0002072\u0006\u00108\u001a\u00020\u0013J\u0006\u00109\u001a\u00020\u0006J\u0006\u0010:\u001a\u000207J\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00130&J\u0006\u0010<\u001a\u00020\u0006J\u000e\u0010=\u001a\u0002072\u0006\u00108\u001a\u00020\u0013J\u0016\u0010>\u001a\u0002072\f\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00130&H\u0002J\u0014\u0010@\u001a\u0002072\f\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00130&R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR$\u0010\f\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\t\"\u0004\b\u000e\u0010\u000bR\u000e\u0010\u000f\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0011\u0010\t\"\u0004\b\u0012\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R(\u0010\u0014\u001a\u0004\u0018\u00010\u00132\b\u0010\u0005\u001a\u0004\u0018\u00010\u00138F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R$\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u0005\u001a\u00020\u00138F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001a\u0010\u0016\"\u0004\b\u001b\u0010\u0018R$\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001d\u0010\t\"\u0004\b\u001e\u0010\u000bR$\u0010 \u001a\u00020\u001f2\u0006\u0010\u0005\u001a\u00020\u001f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u0016\u0010%\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R$\u0010\'\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b(\u0010\t\"\u0004\b)\u0010\u000bR(\u0010*\u001a\u0004\u0018\u00010\u00132\b\u0010\u0005\u001a\u0004\u0018\u00010\u00138F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b+\u0010\u0016\"\u0004\b,\u0010\u0018R\u000e\u0010-\u001a\u00020.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u00100\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b1\u0010\t\"\u0004\b2\u0010\u000bR$\u00103\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b4\u0010\t\"\u0004\b5\u0010\u000b\u00a8\u0006B"}, d2 = {"Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "appsHidden", "getAppsHidden", "()Z", "setAppsHidden", "(Z)V", "blockTouchInHiddenMode", "getBlockTouchInHiddenMode", "setBlockTouchInHiddenMode", "cacheLock", "closeAppsOnHiddenMode", "getCloseAppsOnHiddenMode", "setCloseAppsOnHiddenMode", "", "customKeyCombination", "getCustomKeyCombination", "()Ljava/lang/String;", "setCustomKeyCombination", "(Ljava/lang/String;)V", "defaultHomeScreen", "getDefaultHomeScreen", "setDefaultHomeScreen", "enableDndInHiddenMode", "getEnableDndInHiddenMode", "setEnableDndInHiddenMode", "", "gridColumnCount", "getGridColumnCount", "()I", "setGridColumnCount", "(I)V", "hiddenAppsCache", "", "hideAppsInHiddenMode", "getHideAppsInHiddenMode", "setHideAppsInHiddenMode", "iconPackPackageName", "getIconPackPackageName", "setIconPackPackageName", "prefs", "Landroid/content/SharedPreferences;", "sharedPreferences", "touchScreenBlocked", "getTouchScreenBlocked", "setTouchScreenBlocked", "useCustomKeys", "getUseCustomKeys", "setUseCustomKeys", "addHiddenApp", "", "packageName", "areAppsHidden", "clearHiddenApps", "getHiddenApps", "isSensorActive", "removeHiddenApp", "saveHiddenAppsInternal", "apps", "setHiddenApps", "Companion", "app_debug"})
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/customlauncher/app/data/preferences/LauncherPreferences$Companion;", "", "()V", "KEY_APPS_HIDDEN", "", "KEY_BLOCK_TOUCH_IN_HIDDEN", "KEY_CLOSE_APPS_ON_HIDDEN", "KEY_CUSTOM_KEY_COMBINATION", "KEY_DEFAULT_HOME", "KEY_ENABLE_DND_IN_HIDDEN", "KEY_GRID_COLUMNS", "KEY_HIDDEN_APPS", "KEY_HIDE_APPS_IN_HIDDEN", "KEY_ICON_PACK", "KEY_TOUCH_BLOCKED", "KEY_USE_CUSTOM_KEYS", "PREFS_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
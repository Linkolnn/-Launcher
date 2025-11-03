package com.customlauncher.app.manager;

/**
 * Manager for handling Home Screen modes (Touch/Button phone)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\u0018\u0000 \u001f2\u00020\u0001:\u0002\u001f B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0007J\u0006\u0010\r\u001a\u00020\u000bJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fJ\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fJ\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015J\u0006\u0010\u0017\u001a\u00020\u0015J\u0018\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u000fH\u0002J\u000e\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0007J\u0018\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u000fH\u0002J\u000e\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000fJ\u0006\u0010\u001e\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/customlauncher/app/manager/HomeScreenModeManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "listeners", "", "Lcom/customlauncher/app/manager/HomeScreenModeManager$ModeChangeListener;", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "addModeChangeListener", "", "listener", "clearListeners", "getCurrentMode", "", "getModeName", "", "mode", "getModeString", "isButtonMode", "", "isButtonModeSupported", "isTouchMode", "notifyModeChanged", "newMode", "previousMode", "removeModeChangeListener", "sendModeBroadcast", "setMode", "toggleMode", "Companion", "ModeChangeListener", "app_debug"})
public final class HomeScreenModeManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "HomeScreenModeManager";
    public static final int MODE_TOUCH = 0;
    public static final int MODE_BUTTON = 1;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_MODE_CHANGED = "com.customlauncher.app.HOME_SCREEN_MODE_CHANGED";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_MODE = "mode";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_PREVIOUS_MODE = "previous_mode";
    private static final boolean DEBUG = true;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.preferences.LauncherPreferences preferences = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.customlauncher.app.manager.HomeScreenModeManager.ModeChangeListener> listeners = null;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.manager.HomeScreenModeManager.Companion Companion = null;
    
    public HomeScreenModeManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    /**
     * Get current home screen mode
     */
    public final int getCurrentMode() {
        return 0;
    }
    
    /**
     * Set home screen mode
     */
    public final void setMode(int mode) {
    }
    
    /**
     * Check if current mode is Touch mode
     */
    public final boolean isTouchMode() {
        return false;
    }
    
    /**
     * Check if current mode is Button mode
     */
    public final boolean isButtonMode() {
        return false;
    }
    
    /**
     * Toggle between modes
     */
    public final void toggleMode() {
    }
    
    /**
     * Add mode change listener
     */
    public final void addModeChangeListener(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.manager.HomeScreenModeManager.ModeChangeListener listener) {
    }
    
    /**
     * Remove mode change listener
     */
    public final void removeModeChangeListener(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.manager.HomeScreenModeManager.ModeChangeListener listener) {
    }
    
    /**
     * Clear all listeners (call in onDestroy)
     */
    public final void clearListeners() {
    }
    
    /**
     * Get string representation of mode
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getModeString(int mode) {
        return null;
    }
    
    /**
     * Get user-friendly mode name for UI
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getModeName(int mode) {
        return null;
    }
    
    /**
     * Notify all listeners about mode change
     */
    private final void notifyModeChanged(int newMode, int previousMode) {
    }
    
    /**
     * Send broadcast about mode change
     */
    private final void sendModeBroadcast(int newMode, int previousMode) {
    }
    
    /**
     * Check if the device supports button mode
     * (Can be used to hide button mode option on pure touch devices)
     */
    public final boolean isButtonModeSupported() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/customlauncher/app/manager/HomeScreenModeManager$Companion;", "", "()V", "ACTION_MODE_CHANGED", "", "DEBUG", "", "EXTRA_MODE", "EXTRA_PREVIOUS_MODE", "MODE_BUTTON", "", "MODE_TOUCH", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcom/customlauncher/app/manager/HomeScreenModeManager$ModeChangeListener;", "", "onModeChanged", "", "newMode", "", "previousMode", "app_debug"})
    public static abstract interface ModeChangeListener {
        
        public abstract void onModeChanged(int newMode, int previousMode);
    }
}
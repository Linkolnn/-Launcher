package com.customlauncher.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\u0012\u0010\u001f\u001a\u00020\u001c2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u001cH\u0002J\b\u0010#\u001a\u00020\u001eH\u0002J\b\u0010$\u001a\u00020\u001eH\u0016J\u0012\u0010%\u001a\u00020\u001e2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0014J\b\u0010(\u001a\u00020\u001eH\u0014J\u001a\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\u00042\b\u0010+\u001a\u0004\u0018\u00010,H\u0016J\u001a\u0010-\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\u00042\b\u0010+\u001a\u0004\u0018\u00010,H\u0016J\b\u0010.\u001a\u00020\u001eH\u0014J\b\u0010/\u001a\u00020\u001eH\u0002J\b\u00100\u001a\u00020\u001eH\u0002J\b\u00101\u001a\u00020\u001eH\u0002J\b\u00102\u001a\u00020\u001eH\u0002J\u0010\u00103\u001a\u00020\u001e2\u0006\u00104\u001a\u00020\u001cH\u0002J\b\u00105\u001a\u00020\u001eH\u0002J\b\u00106\u001a\u00020\u001eH\u0002J\b\u00107\u001a\u00020\u001eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00068"}, d2 = {"Lcom/customlauncher/app/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "MAX_TOUCH_EVENTS_PER_SECOND", "", "TOUCH_THROTTLE_MS", "", "appAdapter", "Lcom/customlauncher/app/ui/adapter/AppGridAdapter;", "binding", "Lcom/customlauncher/app/databinding/ActivityMainBinding;", "customKeyListener", "Lcom/customlauncher/app/receiver/CustomKeyListener;", "keyCombinationReceiver", "Landroid/content/BroadcastReceiver;", "keyPressHandler", "Landroid/os/Handler;", "lastTouchEventTime", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "getPreferences", "()Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "preferences$delegate", "Lkotlin/Lazy;", "touchEventCount", "viewModel", "Lcom/customlauncher/app/ui/viewmodel/AppViewModel;", "checkAllPermissions", "", "checkOverlayPermission", "", "dispatchTouchEvent", "ev", "Landroid/view/MotionEvent;", "isAccessibilityServiceEnabled", "observeViewModel", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onKeyDown", "keyCode", "event", "Landroid/view/KeyEvent;", "onKeyUp", "onResume", "setupCustomKeyListener", "setupListeners", "setupRecyclerView", "showPermissionsDialog", "toggleDoNotDisturb", "enable", "toggleHiddenMode", "updateGridColumns", "updateVisibility", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.customlauncher.app.databinding.ActivityMainBinding binding;
    private com.customlauncher.app.ui.viewmodel.AppViewModel viewModel;
    private com.customlauncher.app.ui.adapter.AppGridAdapter appAdapter;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy preferences$delegate = null;
    @org.jetbrains.annotations.Nullable
    private com.customlauncher.app.receiver.CustomKeyListener customKeyListener;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler keyPressHandler = null;
    private long lastTouchEventTime = 0L;
    private final long TOUCH_THROTTLE_MS = 100L;
    private int touchEventCount = 0;
    private final int MAX_TOUCH_EVENTS_PER_SECOND = 30;
    @org.jetbrains.annotations.NotNull
    private final android.content.BroadcastReceiver keyCombinationReceiver = null;
    
    public MainActivity() {
        super();
    }
    
    private final com.customlauncher.app.data.preferences.LauncherPreferences getPreferences() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupCustomKeyListener() {
    }
    
    private final void checkOverlayPermission() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupListeners() {
    }
    
    private final boolean checkAllPermissions() {
        return false;
    }
    
    private final void showPermissionsDialog() {
    }
    
    private final void observeViewModel() {
    }
    
    @java.lang.Override
    public boolean onKeyDown(int keyCode, @org.jetbrains.annotations.Nullable
    android.view.KeyEvent event) {
        return false;
    }
    
    @java.lang.Override
    public boolean onKeyUp(int keyCode, @org.jetbrains.annotations.Nullable
    android.view.KeyEvent event) {
        return false;
    }
    
    private final void toggleHiddenMode() {
    }
    
    private final void toggleDoNotDisturb(boolean enable) {
    }
    
    private final void updateVisibility() {
    }
    
    private final boolean isAccessibilityServiceEnabled() {
        return false;
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    private final void updateGridColumns() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
    
    @java.lang.Override
    public boolean dispatchTouchEvent(@org.jetbrains.annotations.Nullable
    android.view.MotionEvent ev) {
        return false;
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
}
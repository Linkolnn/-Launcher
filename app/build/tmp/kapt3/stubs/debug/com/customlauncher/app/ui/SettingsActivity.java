package com.customlauncher.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u0000 ,2\u00020\u0001:\u0001,B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0002J\u0010\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\fH\u0002J\b\u0010\u0017\u001a\u00020\fH\u0002J\"\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00152\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0014J\b\u0010\u001d\u001a\u00020\u000eH\u0016J\u0012\u0010\u001e\u001a\u00020\u000e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0014J\b\u0010!\u001a\u00020\u000eH\u0014J\b\u0010\"\u001a\u00020\u000eH\u0002J\b\u0010#\u001a\u00020\u000eH\u0002J\b\u0010$\u001a\u00020\u000eH\u0002J\b\u0010%\u001a\u00020\u000eH\u0002J\b\u0010&\u001a\u00020\u000eH\u0002J\b\u0010\'\u001a\u00020\u000eH\u0002J\b\u0010(\u001a\u00020\u000eH\u0002J\b\u0010)\u001a\u00020\u000eH\u0002J\b\u0010*\u001a\u00020\u000eH\u0002J\b\u0010+\u001a\u00020\u000eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b\u00a8\u0006-"}, d2 = {"Lcom/customlauncher/app/ui/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/customlauncher/app/databinding/ActivitySettingsBinding;", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "getPreferences", "()Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "preferences$delegate", "Lkotlin/Lazy;", "areAllPermissionsGranted", "", "checkAllPermissions", "", "checkOverlayPermission", "displayCustomKeys", "keysString", "", "getKeyName", "keyCode", "", "isAccessibilityServiceEnabled", "isDefaultLauncher", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "setupAppVersion", "setupCustomKeyCombination", "setupGridSelection", "setupHomeScreenSelector", "setupPermissionsSection", "setupTabs", "showHomeScreenDialog", "showRecordKeysDialog", "updateStatusText", "updateUI", "Companion", "app_debug"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.customlauncher.app.databinding.ActivitySettingsBinding binding;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy preferences$delegate = null;
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1002;
    private static final int REQUEST_CODE_ACCESSIBILITY = 1003;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "SettingsActivity";
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.SettingsActivity.Companion Companion = null;
    
    public SettingsActivity() {
        super();
    }
    
    private final com.customlauncher.app.data.preferences.LauncherPreferences getPreferences() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupTabs() {
    }
    
    private final void setupCustomKeyCombination() {
    }
    
    private final void displayCustomKeys(java.lang.String keysString) {
    }
    
    private final java.lang.String getKeyName(int keyCode) {
        return null;
    }
    
    private final void showRecordKeysDialog() {
    }
    
    private final void setupGridSelection() {
    }
    
    private final void setupHomeScreenSelector() {
    }
    
    private final void updateUI() {
    }
    
    private final void updateStatusText() {
    }
    
    private final void showHomeScreenDialog() {
    }
    
    private final void checkOverlayPermission() {
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
    
    private final void setupPermissionsSection() {
    }
    
    private final void checkAllPermissions() {
    }
    
    private final boolean isAccessibilityServiceEnabled() {
        return false;
    }
    
    private final boolean isDefaultLauncher() {
        return false;
    }
    
    public final boolean areAllPermissionsGranted() {
        return false;
    }
    
    @java.lang.Override
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable
    android.content.Intent data) {
    }
    
    private final void setupAppVersion() {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/customlauncher/app/ui/SettingsActivity$Companion;", "", "()V", "REQUEST_CODE_ACCESSIBILITY", "", "REQUEST_CODE_WRITE_SETTINGS", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
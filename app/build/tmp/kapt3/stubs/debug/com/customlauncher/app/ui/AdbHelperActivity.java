package com.customlauncher.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u0018\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\u0012\u0010\u0014\u001a\u00020\f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\fH\u0014J\b\u0010\u0018\u001a\u00020\fH\u0014J\b\u0010\u0019\u001a\u00020\fH\u0002J\b\u0010\u001a\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/customlauncher/app/ui/AdbHelperActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/customlauncher/app/databinding/ActivityAdbHelperBinding;", "clipboardManager", "Landroid/content/ClipboardManager;", "shizukuHelper", "Lcom/customlauncher/app/utils/ShizukuHelper;", "shizukuPermissionListener", "Lrikka/shizuku/Shizuku$OnRequestPermissionResultListener;", "checkPermissionStatus", "", "checkShizukuStatus", "copyToClipboard", "label", "", "text", "isAccessibilityServiceEnabled", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onResume", "setupButtons", "setupShizukuButtons", "app_debug"})
public final class AdbHelperActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.customlauncher.app.databinding.ActivityAdbHelperBinding binding;
    private android.content.ClipboardManager clipboardManager;
    private com.customlauncher.app.utils.ShizukuHelper shizukuHelper;
    @org.jetbrains.annotations.NotNull
    private final rikka.shizuku.Shizuku.OnRequestPermissionResultListener shizukuPermissionListener = null;
    
    public AdbHelperActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
    
    private final void setupButtons() {
    }
    
    private final void copyToClipboard(java.lang.String label, java.lang.String text) {
    }
    
    private final void checkPermissionStatus() {
    }
    
    private final boolean isAccessibilityServiceEnabled() {
        return false;
    }
    
    private final void setupShizukuButtons() {
    }
    
    private final void checkShizukuStatus() {
    }
}
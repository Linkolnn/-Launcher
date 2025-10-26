package com.customlauncher.app.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u0014\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\b\u0010\u0012\u001a\u00020\fH\u0016J\b\u0010\u0013\u001a\u00020\fH\u0016J\"\u0010\u0014\u001a\u00020\u00152\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0015H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0019H\u0002J\b\u0010\u001b\u001a\u00020\u0019H\u0002J\b\u0010\u001c\u001a\u00020\u0019H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00060\nR\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/customlauncher/app/service/SensorControlService;", "Landroid/app/Service;", "()V", "componentName", "Landroid/content/ComponentName;", "devicePolicyManager", "Landroid/app/admin/DevicePolicyManager;", "powerManager", "Landroid/os/PowerManager;", "wakeLock", "Landroid/os/PowerManager$WakeLock;", "disableTouchSensor", "", "enableTouchSensor", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "tryDisableTouchViaRoot", "", "tryDisableTouchViaSettings", "tryEnableTouchViaRoot", "tryEnableTouchViaSettings", "Companion", "app_debug"})
public final class SensorControlService extends android.app.Service {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "SensorControlService";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_DISABLE_SENSOR = "com.customlauncher.app.DISABLE_SENSOR";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_ENABLE_SENSOR = "com.customlauncher.app.ENABLE_SENSOR";
    private static boolean isDisabled = false;
    private android.os.PowerManager powerManager;
    private android.os.PowerManager.WakeLock wakeLock;
    private android.app.admin.DevicePolicyManager devicePolicyManager;
    private android.content.ComponentName componentName;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.service.SensorControlService.Companion Companion = null;
    
    public SensorControlService() {
        super();
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void disableTouchSensor() {
    }
    
    private final void enableTouchSensor() {
    }
    
    private final boolean tryDisableTouchViaRoot() {
        return false;
    }
    
    private final boolean tryEnableTouchViaRoot() {
        return false;
    }
    
    private final boolean tryDisableTouchViaSettings() {
        return false;
    }
    
    private final boolean tryEnableTouchViaSettings() {
        return false;
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/customlauncher/app/service/SensorControlService$Companion;", "", "()V", "ACTION_DISABLE_SENSOR", "", "ACTION_ENABLE_SENSOR", "TAG", "isDisabled", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
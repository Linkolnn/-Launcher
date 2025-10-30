package com.customlauncher.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 #2\u00020\u0001:\u0002#$B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0019\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0019\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ%\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u000f2\u0006\u0010\u0012\u001a\u00020\u0011H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u0006H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0006\u0010\u0016\u001a\u00020\u0010J\u0006\u0010\u0017\u001a\u00020\u0010J\u0006\u0010\u0018\u001a\u00020\u0010J\u000e\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\bJ)\u0010\u001d\u001a\u00020\b2!\u0010\u001e\u001a\u001d\u0012\u0013\u0012\u00110\u0010\u00a2\u0006\f\b \u0012\b\b!\u0012\u0004\b\b(\"\u0012\u0004\u0012\u00020\b0\u001fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006%"}, d2 = {"Lcom/customlauncher/app/utils/ShizukuHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "userService", "Lcom/customlauncher/app/IUserService;", "enableAccessibilityService", "", "callback", "Lcom/customlauncher/app/utils/ShizukuHelper$ShizukuCallback;", "(Lcom/customlauncher/app/utils/ShizukuHelper$ShizukuCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "enableAllPermissions", "enableOverlayPermission", "executeCommand", "Lkotlin/Pair;", "", "", "command", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserService", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hasShizukuPermission", "isShizukuInstalled", "isShizukuRunning", "removeShizukuListener", "listener", "Lrikka/shizuku/Shizuku$OnRequestPermissionResultListener;", "requestShizukuPermission", "setupShizukuListener", "onPermissionResult", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "granted", "Companion", "ShizukuCallback", "app_debug"})
public final class ShizukuHelper {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "ShizukuHelper";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String SHIZUKU_PACKAGE = "moe.shizuku.privileged.api";
    private static final int PERMISSION_CODE = 0;
    @org.jetbrains.annotations.Nullable
    private com.customlauncher.app.IUserService userService;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.utils.ShizukuHelper.Companion Companion = null;
    
    public ShizukuHelper(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final boolean isShizukuInstalled() {
        return false;
    }
    
    public final boolean isShizukuRunning() {
        return false;
    }
    
    public final boolean hasShizukuPermission() {
        return false;
    }
    
    public final void requestShizukuPermission() {
    }
    
    private final java.lang.Object getUserService(kotlin.coroutines.Continuation<? super com.customlauncher.app.IUserService> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object executeCommand(@org.jetbrains.annotations.NotNull
    java.lang.String command, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Boolean, java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object enableAccessibilityService(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.utils.ShizukuHelper.ShizukuCallback callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object enableOverlayPermission(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.utils.ShizukuHelper.ShizukuCallback callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object enableAllPermissions(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.utils.ShizukuHelper.ShizukuCallback callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void setupShizukuListener(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onPermissionResult) {
    }
    
    public final void removeShizukuListener(@org.jetbrains.annotations.NotNull
    rikka.shizuku.Shizuku.OnRequestPermissionResultListener listener) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/customlauncher/app/utils/ShizukuHelper$Companion;", "", "()V", "PERMISSION_CODE", "", "SHIZUKU_PACKAGE", "", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H&\u00a8\u0006\b"}, d2 = {"Lcom/customlauncher/app/utils/ShizukuHelper$ShizukuCallback;", "", "onError", "", "error", "", "onSuccess", "message", "app_debug"})
    public static abstract interface ShizukuCallback {
        
        public abstract void onSuccess(@org.jetbrains.annotations.NotNull
        java.lang.String message);
        
        public abstract void onError(@org.jetbrains.annotations.NotNull
        java.lang.String error);
    }
}
package com.customlauncher.app.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\b\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\f\u001a\u00020\nH\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0002J\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\b\u0010\u0014\u001a\u00020\nH\u0016J\"\u0010\u0015\u001a\u00020\u000e2\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u000eH\u0016J\b\u0010\u0018\u001a\u00020\nH\u0002J\b\u0010\u0019\u001a\u00020\nH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/customlauncher/app/service/TouchBlockService;", "Landroid/app/Service;", "()V", "blockView", "Landroid/view/View;", "navigationBarBlockView", "statusBarBlockView", "windowManager", "Landroid/view/WindowManager;", "addNavigationBarBlock", "", "addStatusBarBlock", "blockTouch", "getNavigationBarHeight", "", "getStatusBarHeight", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onDestroy", "onStartCommand", "flags", "startId", "startForegroundService", "unblockTouch", "Companion", "app_debug"})
public final class TouchBlockService extends android.app.Service {
    @org.jetbrains.annotations.Nullable
    private android.view.WindowManager windowManager;
    @org.jetbrains.annotations.Nullable
    private android.view.View blockView;
    @org.jetbrains.annotations.Nullable
    private android.view.View statusBarBlockView;
    @org.jetbrains.annotations.Nullable
    private android.view.View navigationBarBlockView;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_BLOCK_TOUCH = "com.customlauncher.app.BLOCK_TOUCH";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_UNBLOCK_TOUCH = "com.customlauncher.app.UNBLOCK_TOUCH";
    private static final int NOTIFICATION_ID = 1001;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "TouchBlockService";
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.service.TouchBlockService.Companion Companion = null;
    
    public TouchBlockService() {
        super();
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
    
    private final void startForegroundService() {
    }
    
    private final void blockTouch() {
    }
    
    private final void addStatusBarBlock() {
    }
    
    private final void addNavigationBarBlock() {
    }
    
    private final int getStatusBarHeight() {
        return 0;
    }
    
    private final int getNavigationBarHeight() {
        return 0;
    }
    
    private final void unblockTouch() {
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/customlauncher/app/service/TouchBlockService$Companion;", "", "()V", "ACTION_BLOCK_TOUCH", "", "ACTION_UNBLOCK_TOUCH", "NOTIFICATION_ID", "", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
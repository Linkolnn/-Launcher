package com.customlauncher.app.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 /2\u00020\u0001:\u0001/B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0013H\u0002J\b\u0010\u0015\u001a\u00020\u0013H\u0002J\u0010\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\u0019\u001a\u00020\u0013H\u0002J\b\u0010\u001a\u001a\u00020\u0013H\u0002J\u0010\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0012\u0010\u001e\u001a\u00020\u00132\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0010\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u001dH\u0014J\b\u0010#\u001a\u00020\u0013H\u0016J\u0012\u0010$\u001a\u00020\u00062\b\u0010\u001f\u001a\u0004\u0018\u00010%H\u0014J\b\u0010&\u001a\u00020\u0013H\u0014J\"\u0010\'\u001a\u00020\u001d2\b\u0010(\u001a\u0004\u0018\u00010)2\u0006\u0010*\u001a\u00020\u001d2\u0006\u0010+\u001a\u00020\u001dH\u0016J\b\u0010,\u001a\u00020\u0013H\u0002J\b\u0010-\u001a\u00020\u0013H\u0002J\b\u0010.\u001a\u00020\u0013H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/customlauncher/app/service/SystemBlockAccessibilityService;", "Landroid/accessibilityservice/AccessibilityService;", "()V", "KEY_EVENT_TIMEOUT", "", "isScreenLocked", "", "isTouchExplorationEnabled", "keyPressHandler", "Landroid/os/Handler;", "keyguardManager", "Landroid/app/KeyguardManager;", "lastKeyEventTime", "longPressRunnable", "Ljava/lang/Runnable;", "powerPressed", "volumeDownPressed", "volumeUpPressed", "cancelLongPress", "", "checkKeyCombination", "checkScreenLockState", "disableNode", "node", "Landroid/view/accessibility/AccessibilityNodeInfo;", "disableTouchBlocking", "enableTouchBlocking", "handleLongPress", "keyCode", "", "onAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onGesture", "gestureId", "onInterrupt", "onKeyEvent", "Landroid/view/KeyEvent;", "onServiceConnected", "onStartCommand", "intent", "Landroid/content/Intent;", "flags", "startId", "resetKeyStates", "startLongPressTimer", "toggleHiddenMode", "Companion", "app_debug"})
public final class SystemBlockAccessibilityService extends android.accessibilityservice.AccessibilityService {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "SystemBlockAccessibility";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_BLOCK_TOUCHES = "com.customlauncher.app.BLOCK_TOUCHES";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_UNBLOCK_TOUCHES = "com.customlauncher.app.UNBLOCK_TOUCHES";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_TOGGLE_APPS = "com.customlauncher.app.TOGGLE_APPS";
    @org.jetbrains.annotations.Nullable
    private static com.customlauncher.app.service.SystemBlockAccessibilityService instance;
    private static final long KEYGUARD_CHECK_DELAY = 50L;
    private boolean volumeUpPressed = false;
    private boolean volumeDownPressed = false;
    private boolean powerPressed = false;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler keyPressHandler = null;
    @org.jetbrains.annotations.Nullable
    private java.lang.Runnable longPressRunnable;
    private long lastKeyEventTime = 0L;
    private final long KEY_EVENT_TIMEOUT = 100L;
    private boolean isScreenLocked = false;
    private android.app.KeyguardManager keyguardManager;
    private boolean isTouchExplorationEnabled = false;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.service.SystemBlockAccessibilityService.Companion Companion = null;
    
    public SystemBlockAccessibilityService() {
        super();
    }
    
    @java.lang.Override
    protected void onServiceConnected() {
    }
    
    @java.lang.Override
    public void onAccessibilityEvent(@org.jetbrains.annotations.Nullable
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    @java.lang.Override
    protected boolean onKeyEvent(@org.jetbrains.annotations.Nullable
    android.view.KeyEvent event) {
        return false;
    }
    
    private final void resetKeyStates() {
    }
    
    private final void checkKeyCombination() {
    }
    
    private final void handleLongPress(int keyCode) {
    }
    
    private final void startLongPressTimer() {
    }
    
    private final void cancelLongPress() {
    }
    
    private final void disableNode(android.view.accessibility.AccessibilityNodeInfo node) {
    }
    
    private final void checkScreenLockState() {
    }
    
    private final void toggleHiddenMode() {
    }
    
    @java.lang.Override
    public void onInterrupt() {
    }
    
    @java.lang.Override
    protected boolean onGesture(int gestureId) {
        return false;
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void enableTouchBlocking() {
    }
    
    private final void disableTouchBlocking() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/customlauncher/app/service/SystemBlockAccessibilityService$Companion;", "", "()V", "ACTION_BLOCK_TOUCHES", "", "ACTION_TOGGLE_APPS", "ACTION_UNBLOCK_TOUCHES", "KEYGUARD_CHECK_DELAY", "", "TAG", "instance", "Lcom/customlauncher/app/service/SystemBlockAccessibilityService;", "getInstance", "()Lcom/customlauncher/app/service/SystemBlockAccessibilityService;", "setInstance", "(Lcom/customlauncher/app/service/SystemBlockAccessibilityService;)V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.customlauncher.app.service.SystemBlockAccessibilityService getInstance() {
            return null;
        }
        
        public final void setInstance(@org.jetbrains.annotations.Nullable
        com.customlauncher.app.service.SystemBlockAccessibilityService p0) {
        }
    }
}
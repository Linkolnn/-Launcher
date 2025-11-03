package com.customlauncher.app.ui.gesture;

/**
 * Detector for swipe gestures on the home screen
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0005\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019BU\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\u0010\b\u0002\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\u0010\b\u0002\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\u0010\b\u0002\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J*\u0010\u0011\u001a\u00020\u000e2\b\u0010\u0012\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0016J\u000e\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0010R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/customlauncher/app/ui/gesture/SwipeGestureDetector;", "Landroid/view/GestureDetector$SimpleOnGestureListener;", "context", "Landroid/content/Context;", "onSwipeUp", "Lkotlin/Function0;", "", "onSwipeDown", "onSwipeLeft", "onSwipeRight", "(Landroid/content/Context;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "gestureDetector", "Landroid/view/GestureDetector;", "onDown", "", "e", "Landroid/view/MotionEvent;", "onFling", "e1", "e2", "velocityX", "", "velocityY", "onTouchEvent", "event", "Companion", "app_debug"})
public final class SwipeGestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeUp = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeDown = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeLeft = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeRight = null;
    @org.jetbrains.annotations.NotNull
    private final android.view.GestureDetector gestureDetector = null;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.gesture.SwipeGestureDetector.Companion Companion = null;
    
    public SwipeGestureDetector(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeUp, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeDown, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeLeft, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> onSwipeRight) {
        super();
    }
    
    public final boolean onTouchEvent(@org.jetbrains.annotations.NotNull
    android.view.MotionEvent event) {
        return false;
    }
    
    @java.lang.Override
    public boolean onFling(@org.jetbrains.annotations.Nullable
    android.view.MotionEvent e1, @org.jetbrains.annotations.NotNull
    android.view.MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    
    @java.lang.Override
    public boolean onDown(@org.jetbrains.annotations.NotNull
    android.view.MotionEvent e) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/customlauncher/app/ui/gesture/SwipeGestureDetector$Companion;", "", "()V", "SWIPE_THRESHOLD", "", "SWIPE_VELOCITY_THRESHOLD", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
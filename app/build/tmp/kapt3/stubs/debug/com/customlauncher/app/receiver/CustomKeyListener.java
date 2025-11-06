package com.customlauncher.app.receiver;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010!\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u0006\u0010\u0011\u001a\u00020\u0004J\u000e\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\fJ\b\u0010\u0014\u001a\u00020\u0004H\u0002J\u0010\u0010\u0015\u001a\u00020\u00042\b\u0010\u0016\u001a\u0004\u0018\u00010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/customlauncher/app/receiver/CustomKeyListener;", "", "onCombinationDetected", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)V", "handler", "Landroid/os/Handler;", "isProcessing", "", "keySequence", "", "", "resetRunnable", "Ljava/lang/Runnable;", "targetCombination", "Lcom/customlauncher/app/data/model/CustomKeyCombination;", "destroy", "onKeyEvent", "keyCode", "reset", "setCombination", "combination", "Companion", "app_debug"})
public final class CustomKeyListener {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function0<kotlin.Unit> onCombinationDetected = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.Integer> keySequence = null;
    @org.jetbrains.annotations.Nullable
    private com.customlauncher.app.data.model.CustomKeyCombination targetCombination;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler handler = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.Runnable resetRunnable = null;
    private boolean isProcessing = false;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "CustomKeyListener";
    private static final long TIMEOUT_MS = 2000L;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.receiver.CustomKeyListener.Companion Companion = null;
    
    public CustomKeyListener(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onCombinationDetected) {
        super();
    }
    
    public final void setCombination(@org.jetbrains.annotations.Nullable
    com.customlauncher.app.data.model.CustomKeyCombination combination) {
    }
    
    public final boolean onKeyEvent(int keyCode) {
        return false;
    }
    
    private final void reset() {
    }
    
    public final void destroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/customlauncher/app/receiver/CustomKeyListener$Companion;", "", "()V", "TAG", "", "TIMEOUT_MS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
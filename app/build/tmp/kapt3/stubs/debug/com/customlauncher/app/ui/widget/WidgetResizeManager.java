package com.customlauncher.app.ui.widget;

/**
 * Manager for handling widget resize operations
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000e\u0018\u0000 .2\u00020\u0001:\u0001.Ba\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u001e\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\t\u0012\"\b\u0002\u0010\r\u001a\u001c\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f\u0018\u00010\t\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u001e\u001a\u00020\fH\u0002J\u0018\u0010\u001f\u001a\u00020\f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001cH\u0002J\u0018\u0010#\u001a\u00020\f2\u0006\u0010 \u001a\u00020!2\u0006\u0010$\u001a\u00020\u001cH\u0002J\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010%\u001a\u00020\fH\u0002J\b\u0010&\u001a\u00020\fH\u0002J\u0010\u0010\'\u001a\u00020\f2\u0006\u0010(\u001a\u00020\u0011H\u0002J\u0016\u0010)\u001a\u00020\f2\u0006\u0010(\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\nJ\u0010\u0010+\u001a\u00020\f2\b\b\u0002\u0010,\u001a\u00020\u001cJ\b\u0010-\u001a\u00020\fH\u0002R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R&\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R(\u0010\r\u001a\u001c\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f\u0018\u00010\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/customlauncher/app/ui/widget/WidgetResizeManager;", "", "context", "Landroid/content/Context;", "gridContainer", "Landroid/view/ViewGroup;", "gridConfig", "Lcom/customlauncher/app/data/model/GridConfiguration;", "onResizeComplete", "Lkotlin/Function3;", "Lcom/customlauncher/app/data/model/HomeItemModel;", "", "", "onResizeUpdate", "(Landroid/content/Context;Landroid/view/ViewGroup;Lcom/customlauncher/app/data/model/GridConfiguration;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;)V", "activeItem", "activeWidget", "Landroid/view/View;", "cellHeight", "cellWidth", "currentSpanX", "currentSpanY", "initialSpanX", "initialSpanY", "initialTouchX", "", "initialTouchY", "isResizing", "", "resizeOverlay", "applyResize", "handleHorizontalResize", "event", "Landroid/view/MotionEvent;", "fromLeft", "handleVerticalResize", "fromTop", "scheduleWidgetUpdate", "setupResizeHandles", "showResizeOverlay", "widgetView", "startResize", "item", "stopResize", "save", "updateOverlaySize", "Companion", "app_debug"})
public final class WidgetResizeManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final android.view.ViewGroup gridContainer = null;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.model.GridConfiguration gridConfig = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function3<com.customlauncher.app.data.model.HomeItemModel, java.lang.Integer, java.lang.Integer, kotlin.Unit> onResizeComplete = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function3<com.customlauncher.app.data.model.HomeItemModel, java.lang.Integer, java.lang.Integer, kotlin.Unit> onResizeUpdate = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "WidgetResizeManager";
    private static final int MIN_SPAN = 1;
    private static final int TOUCH_SLOP = 5;
    private static final long UPDATE_DELAY_MS = 0L;
    @org.jetbrains.annotations.Nullable
    private android.view.View resizeOverlay;
    @org.jetbrains.annotations.Nullable
    private android.view.View activeWidget;
    @org.jetbrains.annotations.Nullable
    private com.customlauncher.app.data.model.HomeItemModel activeItem;
    private boolean isResizing = false;
    private float initialTouchX = 0.0F;
    private float initialTouchY = 0.0F;
    private int initialSpanX = 1;
    private int initialSpanY = 1;
    private int currentSpanX = 1;
    private int currentSpanY = 1;
    private int cellWidth = 0;
    private int cellHeight = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.widget.WidgetResizeManager.Companion Companion = null;
    
    public WidgetResizeManager(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.view.ViewGroup gridContainer, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function3<? super com.customlauncher.app.data.model.HomeItemModel, ? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onResizeComplete, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function3<? super com.customlauncher.app.data.model.HomeItemModel, ? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onResizeUpdate) {
        super();
    }
    
    /**
     * Start resize mode for a widget
     */
    public final void startResize(@org.jetbrains.annotations.NotNull
    android.view.View widgetView, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel item) {
    }
    
    /**
     * Show resize overlay for the widget
     */
    private final void showResizeOverlay(android.view.View widgetView) {
    }
    
    /**
     * Setup touch listeners for resize handles
     */
    private final void setupResizeHandles() {
    }
    
    /**
     * Handle vertical resize with snap-to-grid
     */
    private final void handleVerticalResize(android.view.MotionEvent event, boolean fromTop) {
    }
    
    /**
     * Handle horizontal resize with snap-to-grid
     */
    private final void handleHorizontalResize(android.view.MotionEvent event, boolean fromLeft) {
    }
    
    /**
     * Update widget immediately
     */
    private final void scheduleWidgetUpdate() {
    }
    
    /**
     * Update overlay size based on current spans
     */
    private final void updateOverlaySize() {
    }
    
    /**
     * Apply resize (save the new size)
     */
    private final void applyResize() {
    }
    
    /**
     * Stop resize mode
     */
    public final void stopResize(boolean save) {
    }
    
    /**
     * Check if currently resizing
     */
    public final boolean isResizing() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/customlauncher/app/ui/widget/WidgetResizeManager$Companion;", "", "()V", "MIN_SPAN", "", "TAG", "", "TOUCH_SLOP", "UPDATE_DELAY_MS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
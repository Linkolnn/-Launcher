package com.customlauncher.app.ui.dragdrop;

/**
 * Manager for drag and drop operations on home screen
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u0000 (2\u00020\u0001:\u0002()B\u00cb\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012<\u0010\u0004\u001a8\u0012\u0004\u0012\u00020\u0006\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0005\u0012<\u0010\r\u001a8\u0012\u0004\u0012\u00020\u0006\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\f0\u0005\u0012@\b\u0002\u0010\u0010\u001a:\u0012\u0004\u0012\u00020\u0011\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\f\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0012J\b\u0010\u001b\u001a\u00020\fH\u0002J \u0010\u001c\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u0007H\u0002J\u0018\u0010!\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0002J\u001a\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0016\u0010%\u001a\u00020#2\u0006\u0010&\u001a\u00020\u00152\u0006\u0010\'\u001a\u00020\u0006R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001a\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000RF\u0010\u0010\u001a:\u0012\u0004\u0012\u00020\u0011\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\f\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000RD\u0010\r\u001a8\u0012\u0004\u0012\u00020\u0006\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\f0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000RD\u0010\u0004\u001a8\u0012\u0004\u0012\u00020\u0006\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/customlauncher/app/ui/dragdrop/DragDropManager;", "Landroid/view/View$OnDragListener;", "gridLayout", "Lcom/customlauncher/app/ui/layout/HomeScreenGridLayout;", "onItemMoved", "Lkotlin/Function3;", "Lcom/customlauncher/app/data/model/HomeItemModel;", "", "Lkotlin/ParameterName;", "name", "newX", "newY", "", "onItemDropped", "x", "y", "onAppDroppedFromDrawer", "Lcom/customlauncher/app/data/model/AppInfo;", "(Lcom/customlauncher/app/ui/layout/HomeScreenGridLayout;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;)V", "draggedItem", "draggedView", "Landroid/view/View;", "dropTargetCell", "Lkotlin/Pair;", "highlightDrawable", "Landroid/graphics/drawable/ColorDrawable;", "highlightedCell", "clearHighlight", "handleDrop", "event", "Landroid/view/DragEvent;", "cellX", "cellY", "highlightCell", "onDrag", "", "v", "startDrag", "view", "item", "Companion", "DragShadowBuilder", "app_debug"})
public final class DragDropManager implements android.view.View.OnDragListener {
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.ui.layout.HomeScreenGridLayout gridLayout = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function3<com.customlauncher.app.data.model.HomeItemModel, java.lang.Integer, java.lang.Integer, kotlin.Unit> onItemMoved = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function3<com.customlauncher.app.data.model.HomeItemModel, java.lang.Integer, java.lang.Integer, kotlin.Unit> onItemDropped = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function3<com.customlauncher.app.data.model.AppInfo, java.lang.Integer, java.lang.Integer, kotlin.Unit> onAppDroppedFromDrawer = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "DragDropManager";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String DRAG_LABEL_HOME_ITEM = "home_item";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String DRAG_LABEL_APP_FROM_DRAWER = "app_from_drawer";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String MIME_TYPE_APPLICATION = "application/home-item";
    @org.jetbrains.annotations.Nullable
    private com.customlauncher.app.data.model.HomeItemModel draggedItem;
    @org.jetbrains.annotations.Nullable
    private android.view.View draggedView;
    @org.jetbrains.annotations.Nullable
    private kotlin.Pair<java.lang.Integer, java.lang.Integer> highlightedCell;
    @org.jetbrains.annotations.Nullable
    private kotlin.Pair<java.lang.Integer, java.lang.Integer> dropTargetCell;
    @org.jetbrains.annotations.NotNull
    private final android.graphics.drawable.ColorDrawable highlightDrawable = null;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.dragdrop.DragDropManager.Companion Companion = null;
    
    public DragDropManager(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.ui.layout.HomeScreenGridLayout gridLayout, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function3<? super com.customlauncher.app.data.model.HomeItemModel, ? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onItemMoved, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function3<? super com.customlauncher.app.data.model.HomeItemModel, ? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onItemDropped, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function3<? super com.customlauncher.app.data.model.AppInfo, ? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onAppDroppedFromDrawer) {
        super();
    }
    
    /**
     * Start drag operation for a home screen item
     */
    public final boolean startDrag(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel item) {
        return false;
    }
    
    @java.lang.Override
    public boolean onDrag(@org.jetbrains.annotations.Nullable
    android.view.View v, @org.jetbrains.annotations.NotNull
    android.view.DragEvent event) {
        return false;
    }
    
    private final void handleDrop(android.view.DragEvent event, int cellX, int cellY) {
    }
    
    private final void highlightCell(int x, int y) {
    }
    
    private final void clearHighlight() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/customlauncher/app/ui/dragdrop/DragDropManager$Companion;", "", "()V", "DRAG_LABEL_APP_FROM_DRAWER", "", "DRAG_LABEL_HOME_ITEM", "MIME_TYPE_APPLICATION", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    /**
     * Custom drag shadow builder
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0016\u00a8\u0006\r"}, d2 = {"Lcom/customlauncher/app/ui/dragdrop/DragDropManager$DragShadowBuilder;", "Landroid/view/View$DragShadowBuilder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "onDrawShadow", "", "canvas", "Landroid/graphics/Canvas;", "onProvideShadowMetrics", "outShadowSize", "Landroid/graphics/Point;", "outShadowTouchPoint", "app_debug"})
    public static final class DragShadowBuilder extends android.view.View.DragShadowBuilder {
        
        public DragShadowBuilder(@org.jetbrains.annotations.NotNull
        android.view.View view) {
            super(null);
        }
        
        @java.lang.Override
        public void onProvideShadowMetrics(@org.jetbrains.annotations.NotNull
        android.graphics.Point outShadowSize, @org.jetbrains.annotations.NotNull
        android.graphics.Point outShadowTouchPoint) {
        }
        
        @java.lang.Override
        public void onDrawShadow(@org.jetbrains.annotations.NotNull
        android.graphics.Canvas canvas) {
        }
    }
}
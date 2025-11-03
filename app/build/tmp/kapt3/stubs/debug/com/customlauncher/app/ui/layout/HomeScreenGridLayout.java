package com.customlauncher.app.ui.layout;

/**
 * Custom layout for home screen grid that manages app icons and widgets positioning
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u00002\u00020\u0001:\u0001SB%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ2\u0010%\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u00072\b\b\u0002\u0010*\u001a\u00020\u00072\b\b\u0002\u0010+\u001a\u00020\u0007J\u0012\u0010,\u001a\u00020\u001a2\b\u0010-\u001a\u0004\u0018\u00010.H\u0014J\u0006\u0010/\u001a\u00020\u001fJ\u0010\u00100\u001a\u00020\u001f2\u0006\u00101\u001a\u000202H\u0014J\u0014\u00103\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0016J\b\u00104\u001a\u000205H\u0014J\u0012\u00106\u001a\u0002052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u00106\u001a\u0002052\b\u0010-\u001a\u0004\u0018\u00010.H\u0014J$\u00107\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00162\u0006\u0010(\u001a\u0002082\u0006\u0010)\u001a\u000208J\u0016\u00109\u001a\u00020:2\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007J\u0006\u0010;\u001a\u00020\u0018J\u0018\u0010<\u001a\u0004\u0018\u00010\'2\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007J\u001a\u0010=\u001a\u00020\u001a2\u0006\u0010>\u001a\u00020\u00072\b\u0010?\u001a\u0004\u0018\u00010@H\u0016J0\u0010A\u001a\u00020\u001f2\u0006\u0010B\u001a\u00020\u001a2\u0006\u0010C\u001a\u00020\u00072\u0006\u0010D\u001a\u00020\u00072\u0006\u0010E\u001a\u00020\u00072\u0006\u0010F\u001a\u00020\u0007H\u0014J\u0018\u0010G\u001a\u00020\u001f2\u0006\u0010H\u001a\u00020\u00072\u0006\u0010I\u001a\u00020\u0007H\u0014J\b\u0010J\u001a\u00020\u001fH\u0016J\u0016\u0010K\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007J\u000e\u0010L\u001a\u00020\u001f2\u0006\u0010M\u001a\u00020\u001aJ\u0016\u0010N\u001a\u00020\u001f2\u0006\u0010O\u001a\u00020\u00072\u0006\u0010P\u001a\u00020\u0007J\u000e\u0010Q\u001a\u00020\u001f2\u0006\u0010R\u001a\u00020\u0018R\u0014\u0010\t\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0015\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R \u0010\u001b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u00160\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006T"}, d2 = {"Lcom/customlauncher/app/ui/layout/HomeScreenGridLayout;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "cellHeight", "getCellHeight", "()I", "cellWidth", "getCellWidth", "debugPaint", "Landroid/graphics/Paint;", "emptyFocusPaint", "focusFillPaint", "focusRect", "Landroid/graphics/RectF;", "focusStrokePaint", "focusedCell", "Lkotlin/Pair;", "gridConfig", "Lcom/customlauncher/app/data/model/GridConfiguration;", "isButtonPhoneMode", "", "occupiedCells", "", "onBottomReached", "Lkotlin/Function0;", "", "getOnBottomReached", "()Lkotlin/jvm/functions/Function0;", "setOnBottomReached", "(Lkotlin/jvm/functions/Function0;)V", "showDebugGrid", "addItemAtPosition", "view", "Landroid/view/View;", "x", "y", "spanX", "spanY", "checkLayoutParams", "p", "Landroid/view/ViewGroup$LayoutParams;", "clearGridFocus", "dispatchDraw", "canvas", "Landroid/graphics/Canvas;", "findFirstEmptyCell", "generateDefaultLayoutParams", "Landroid/widget/FrameLayout$LayoutParams;", "generateLayoutParams", "getCellFromPoint", "", "getCellRect", "Landroid/graphics/Rect;", "getGridConfiguration", "getViewAt", "onKeyDown", "keyCode", "event", "Landroid/view/KeyEvent;", "onLayout", "changed", "left", "top", "right", "bottom", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "removeAllViews", "removeItemAt", "setButtonPhoneMode", "enabled", "setFocusedCell", "row", "col", "setGridConfiguration", "config", "CellLayoutParams", "app_debug"})
public final class HomeScreenGridLayout extends android.widget.FrameLayout {
    @org.jetbrains.annotations.NotNull
    private com.customlauncher.app.data.model.GridConfiguration gridConfig;
    @org.jetbrains.annotations.NotNull
    private final android.graphics.Paint debugPaint = null;
    private boolean showDebugGrid = false;
    @org.jetbrains.annotations.NotNull
    private final java.util.Set<kotlin.Pair<java.lang.Integer, java.lang.Integer>> occupiedCells = null;
    @org.jetbrains.annotations.Nullable
    private kotlin.Pair<java.lang.Integer, java.lang.Integer> focusedCell;
    private boolean isButtonPhoneMode = false;
    @org.jetbrains.annotations.NotNull
    private final android.graphics.Paint focusStrokePaint = null;
    @org.jetbrains.annotations.NotNull
    private final android.graphics.Paint focusFillPaint = null;
    @org.jetbrains.annotations.NotNull
    private final android.graphics.Paint emptyFocusPaint = null;
    @org.jetbrains.annotations.NotNull
    private final android.graphics.RectF focusRect = null;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function0<kotlin.Unit> onBottomReached;
    
    @kotlin.jvm.JvmOverloads
    public HomeScreenGridLayout(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    private final int getCellWidth() {
        return 0;
    }
    
    private final int getCellHeight() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnBottomReached() {
        return null;
    }
    
    public final void setOnBottomReached(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    public final void setGridConfiguration(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration config) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.model.GridConfiguration getGridConfiguration() {
        return null;
    }
    
    public final void setButtonPhoneMode(boolean enabled) {
    }
    
    /**
     * Set focused cell from external source (GridFocusManager)
     */
    public final void setFocusedCell(int row, int col) {
    }
    
    /**
     * Clear grid focus
     */
    public final void clearGridFocus() {
    }
    
    public final boolean addItemAtPosition(@org.jetbrains.annotations.NotNull
    android.view.View view, int x, int y, int spanX, int spanY) {
        return false;
    }
    
    public final void removeItemAt(int x, int y) {
    }
    
    @java.lang.Override
    public void removeAllViews() {
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.Pair<java.lang.Integer, java.lang.Integer> findFirstEmptyCell() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final android.graphics.Rect getCellRect(int x, int y) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.Pair<java.lang.Integer, java.lang.Integer> getCellFromPoint(float x, float y) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.view.View getViewAt(int x, int y) {
        return null;
    }
    
    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    }
    
    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }
    
    @java.lang.Override
    protected void dispatchDraw(@org.jetbrains.annotations.NotNull
    android.graphics.Canvas canvas) {
    }
    
    @java.lang.Override
    public boolean onKeyDown(int keyCode, @org.jetbrains.annotations.Nullable
    android.view.KeyEvent event) {
        return false;
    }
    
    @java.lang.Override
    protected boolean checkLayoutParams(@org.jetbrains.annotations.Nullable
    android.view.ViewGroup.LayoutParams p) {
        return false;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    protected android.widget.FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.widget.FrameLayout.LayoutParams generateLayoutParams(@org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    protected android.widget.FrameLayout.LayoutParams generateLayoutParams(@org.jetbrains.annotations.Nullable
    android.view.ViewGroup.LayoutParams p) {
        return null;
    }
    
    @kotlin.jvm.JvmOverloads
    public HomeScreenGridLayout(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads
    public HomeScreenGridLayout(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    /**
     * Layout params for grid cell positioning
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\u0018\u00002\u00020\u0001B+\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007B\u0019\b\u0016\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\fB\u000f\b\u0016\u0012\u0006\u0010\r\u001a\u00020\u000e\u00a2\u0006\u0002\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0012\"\u0004\b\u0019\u0010\u0014R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0012\"\u0004\b\u001b\u0010\u0014\u00a8\u0006\u001c"}, d2 = {"Lcom/customlauncher/app/ui/layout/HomeScreenGridLayout$CellLayoutParams;", "Landroid/widget/FrameLayout$LayoutParams;", "x", "", "y", "spanX", "spanY", "(IIII)V", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "source", "Landroid/view/ViewGroup$LayoutParams;", "(Landroid/view/ViewGroup$LayoutParams;)V", "cellX", "getCellX", "()I", "setCellX", "(I)V", "cellY", "getCellY", "setCellY", "getSpanX", "setSpanX", "getSpanY", "setSpanY", "app_debug"})
    public static final class CellLayoutParams extends android.widget.FrameLayout.LayoutParams {
        private int cellX = 0;
        private int cellY = 0;
        private int spanX = 1;
        private int spanY = 1;
        
        public final int getCellX() {
            return 0;
        }
        
        public final void setCellX(int p0) {
        }
        
        public final int getCellY() {
            return 0;
        }
        
        public final void setCellY(int p0) {
        }
        
        public final int getSpanX() {
            return 0;
        }
        
        public final void setSpanX(int p0) {
        }
        
        public final int getSpanY() {
            return 0;
        }
        
        public final void setSpanY(int p0) {
        }
        
        public CellLayoutParams(int x, int y, int spanX, int spanY) {
            super(null, null);
        }
        
        public CellLayoutParams(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.Nullable
        android.util.AttributeSet attrs) {
            super(null, null);
        }
        
        public CellLayoutParams(@org.jetbrains.annotations.NotNull
        android.view.ViewGroup.LayoutParams source) {
            super(null, null);
        }
    }
}
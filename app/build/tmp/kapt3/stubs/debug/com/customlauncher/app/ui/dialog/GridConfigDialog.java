package com.customlauncher.app.ui.dialog;

/**
 * Dialog for configuring home screen grid size
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 +2\u00020\u0001:\u0002+,BU\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00126\u0010\b\u001a2\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u000f0\t\u00a2\u0006\u0002\u0010\u0010J\u001e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u000f0\u001eH\u0002J,\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020\n2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000f0$H\u0002J\u0012\u0010%\u001a\u00020\u000f2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0014J\u0016\u0010(\u001a\u00020\u000f2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u000f0\u001eH\u0002J\b\u0010*\u001a\u00020\u000fH\u0002R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R>\u0010\b\u001a2\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u000f0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2 = {"Lcom/customlauncher/app/ui/dialog/GridConfigDialog;", "Landroid/app/Dialog;", "context", "Landroid/content/Context;", "currentConfig", "Lcom/customlauncher/app/data/model/GridConfiguration;", "isButtonMode", "", "onSave", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "columns", "rows", "", "(Landroid/content/Context;Lcom/customlauncher/app/data/model/GridConfiguration;ZLkotlin/jvm/functions/Function2;)V", "cancelButton", "Lcom/google/android/material/button/MaterialButton;", "columnsText", "Landroid/widget/TextView;", "gridPreview", "Lcom/customlauncher/app/ui/dialog/GridConfigDialog$GridPreviewView;", "rowsText", "saveButton", "createCircularButton", "Landroid/view/View;", "text", "", "onClick", "Lkotlin/Function0;", "createControlRow", "Landroid/widget/LinearLayout;", "label", "initialValue", "onChange", "Lkotlin/Function1;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "showReductionWarning", "onConfirm", "updatePreview", "Companion", "GridPreviewView", "app_debug"})
public final class GridConfigDialog extends android.app.Dialog {
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.model.GridConfiguration currentConfig = null;
    private final boolean isButtonMode = false;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<java.lang.Integer, java.lang.Integer, kotlin.Unit> onSave = null;
    private com.customlauncher.app.ui.dialog.GridConfigDialog.GridPreviewView gridPreview;
    private android.widget.TextView columnsText;
    private android.widget.TextView rowsText;
    private com.google.android.material.button.MaterialButton saveButton;
    private com.google.android.material.button.MaterialButton cancelButton;
    private int columns;
    private int rows;
    private static final int MIN_COLUMNS = 2;
    private static final int MAX_COLUMNS = 7;
    private static final int MIN_ROWS = 3;
    private static final int MAX_ROWS = 10;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.dialog.GridConfigDialog.Companion Companion = null;
    
    public GridConfigDialog(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration currentConfig, boolean isButtonMode, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onSave) {
        super(null);
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final android.widget.LinearLayout createControlRow(java.lang.String label, int initialValue, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onChange) {
        return null;
    }
    
    private final android.view.View createCircularButton(java.lang.String text, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
        return null;
    }
    
    private final void updatePreview() {
    }
    
    private final void showReductionWarning(kotlin.jvm.functions.Function0<kotlin.Unit> onConfirm) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/customlauncher/app/ui/dialog/GridConfigDialog$Companion;", "", "()V", "MAX_COLUMNS", "", "MAX_ROWS", "MIN_COLUMNS", "MIN_ROWS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    /**
     * Custom view for grid preview with rounded cells
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0014J\u0016\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/customlauncher/app/ui/dialog/GridConfigDialog$GridPreviewView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "cellBorderPaint", "Landroid/graphics/Paint;", "cellFillPaint", "cellRect", "Landroid/graphics/RectF;", "columns", "rows", "onDraw", "", "canvas", "Landroid/graphics/Canvas;", "updateGrid", "cols", "app_debug"})
    public static final class GridPreviewView extends android.view.View {
        private int columns = 4;
        private int rows = 6;
        @org.jetbrains.annotations.NotNull
        private final android.graphics.Paint cellBorderPaint = null;
        @org.jetbrains.annotations.NotNull
        private final android.graphics.Paint cellFillPaint = null;
        @org.jetbrains.annotations.NotNull
        private final android.graphics.RectF cellRect = null;
        
        @kotlin.jvm.JvmOverloads
        public GridPreviewView(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.Nullable
        android.util.AttributeSet attrs, int defStyleAttr) {
            super(null);
        }
        
        public final void updateGrid(int cols, int rows) {
        }
        
        @java.lang.Override
        protected void onDraw(@org.jetbrains.annotations.NotNull
        android.graphics.Canvas canvas) {
        }
        
        @kotlin.jvm.JvmOverloads
        public GridPreviewView(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            super(null);
        }
        
        @kotlin.jvm.JvmOverloads
        public GridPreviewView(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.Nullable
        android.util.AttributeSet attrs) {
            super(null);
        }
    }
}
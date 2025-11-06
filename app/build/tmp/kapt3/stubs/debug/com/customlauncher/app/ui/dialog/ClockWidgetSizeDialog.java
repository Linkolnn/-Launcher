package com.customlauncher.app.ui.dialog;

/**
 * Dialog for selecting clock widget size
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\t\u0018\u0000 \u001a2\u00020\u0001:\u0004\u001a\u001b\u001c\u001dB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J&\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u001a\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\n2\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\u0018\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0002J\u000e\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0004J\u0010\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\nH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog;", "Landroidx/fragment/app/DialogFragment;", "()V", "sizeSelectedListener", "Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$OnSizeSelectedListener;", "onCreateDialog", "Landroid/app/Dialog;", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onStart", "", "onViewCreated", "view", "selectSize", "spanX", "", "spanY", "setOnSizeSelectedListener", "listener", "setupRecyclerView", "Companion", "OnSizeSelectedListener", "SizeOption", "SizePreviewAdapter", "app_debug"})
public final class ClockWidgetSizeDialog extends androidx.fragment.app.DialogFragment {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "ClockWidgetSizeDialog";
    @org.jetbrains.annotations.Nullable
    private com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.OnSizeSelectedListener sizeSelectedListener;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.Companion Companion = null;
    
    public ClockWidgetSizeDialog() {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.app.Dialog onCreateDialog(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void onStart() {
    }
    
    private final void setupRecyclerView(android.view.View view) {
    }
    
    private final void selectSize(int spanX, int spanY) {
    }
    
    public final void setOnSizeSelectedListener(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.OnSizeSelectedListener listener) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$Companion;", "", "()V", "TAG", "", "newInstance", "Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog;", "listener", "Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$OnSizeSelectedListener;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog newInstance(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.OnSizeSelectedListener listener) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$OnSizeSelectedListener;", "", "onSizeSelected", "", "spanX", "", "spanY", "app_debug"})
    public static abstract interface OnSizeSelectedListener {
        
        public abstract void onSizeSelected(int spanX, int spanY);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0006H\u00c6\u0003J\'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000b\u00a8\u0006\u0016"}, d2 = {"Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$SizeOption;", "", "spanX", "", "spanY", "label", "", "(IILjava/lang/String;)V", "getLabel", "()Ljava/lang/String;", "getSpanX", "()I", "getSpanY", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    public static final class SizeOption {
        private final int spanX = 0;
        private final int spanY = 0;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String label = null;
        
        public SizeOption(int spanX, int spanY, @org.jetbrains.annotations.NotNull
        java.lang.String label) {
            super();
        }
        
        public final int getSpanX() {
            return 0;
        }
        
        public final int getSpanY() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getLabel() {
            return null;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizeOption copy(int spanX, int spanY, @org.jetbrains.annotations.NotNull
        java.lang.String label) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u0010\u0012\f\u0012\n0\u0002R\u00060\u0000R\u00020\u00030\u0001:\u0001\u0014B\'\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J \u0010\r\u001a\u00020\t2\u000e\u0010\u000e\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u000f\u001a\u00020\fH\u0016J \u0010\u0010\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\fH\u0016R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$SizePreviewAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$SizePreviewAdapter$ViewHolder;", "Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog;", "sizes", "", "Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$SizeOption;", "onSizeSelected", "Lkotlin/Function1;", "", "(Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog;Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_debug"})
    public final class SizePreviewAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizePreviewAdapter.ViewHolder> {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizeOption> sizes = null;
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function1<com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizeOption, kotlin.Unit> onSizeSelected = null;
        
        public SizePreviewAdapter(@org.jetbrains.annotations.NotNull
        java.util.List<com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizeOption> sizes, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizeOption, kotlin.Unit> onSizeSelected) {
            super();
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizePreviewAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.ui.dialog.ClockWidgetSizeDialog.SizePreviewAdapter.ViewHolder holder, int position) {
        }
        
        @java.lang.Override
        public int getItemCount() {
            return 0;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$SizePreviewAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Lcom/customlauncher/app/ui/dialog/ClockWidgetSizeDialog$SizePreviewAdapter;Landroid/view/View;)V", "previewContainer", "Landroid/widget/FrameLayout;", "getPreviewContainer", "()Landroid/widget/FrameLayout;", "sizeLabel", "Landroid/widget/TextView;", "getSizeLabel", "()Landroid/widget/TextView;", "app_debug"})
        public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull
            private final android.widget.FrameLayout previewContainer = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView sizeLabel = null;
            
            public ViewHolder(@org.jetbrains.annotations.NotNull
            android.view.View view) {
                super(null);
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.FrameLayout getPreviewContainer() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getSizeLabel() {
                return null;
            }
        }
    }
}
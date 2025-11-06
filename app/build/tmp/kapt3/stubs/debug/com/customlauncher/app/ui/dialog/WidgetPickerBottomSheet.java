package com.customlauncher.app.ui.dialog;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u00172\u00020\u0001:\u0003\u0017\u0018\u0019B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\u0005H\u0002J\u0012\u0010\f\u001a\u00020\u00052\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J&\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u001a\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u00102\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet;", "Lcom/google/android/material/bottomsheet/BottomSheetDialogFragment;", "onWidgetSelected", "Lkotlin/Function1;", "Landroid/appwidget/AppWidgetProviderInfo;", "", "(Lkotlin/jvm/functions/Function1;)V", "appWidgetManager", "Landroid/appwidget/AppWidgetManager;", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "loadWidgets", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onViewCreated", "view", "Companion", "WidgetAdapter", "WidgetItem", "app_debug"})
public final class WidgetPickerBottomSheet extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<android.appwidget.AppWidgetProviderInfo, kotlin.Unit> onWidgetSelected = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "WidgetPickerBottomSheet";
    private android.appwidget.AppWidgetManager appWidgetManager;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.Companion Companion = null;
    
    public WidgetPickerBottomSheet(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super android.appwidget.AppWidgetProviderInfo, kotlin.Unit> onWidgetSelected) {
        super();
    }
    
    @java.lang.Override
    public void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
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
    
    private final void loadWidgets() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u0010\u0012\f\u0012\n0\u0002R\u00060\u0000R\u00020\u00030\u0001:\u0001\u0014B\'\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J \u0010\r\u001a\u00020\t2\u000e\u0010\u000e\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u000f\u001a\u00020\fH\u0016J \u0010\u0010\u001a\n0\u0002R\u00060\u0000R\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\fH\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet$WidgetAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet$WidgetAdapter$ViewHolder;", "Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet;", "items", "", "Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet$WidgetItem;", "onItemClick", "Lkotlin/Function1;", "", "(Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet;Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_debug"})
    public final class WidgetAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetAdapter.ViewHolder> {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetItem> items = null;
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function1<com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetItem, kotlin.Unit> onItemClick = null;
        
        public WidgetAdapter(@org.jetbrains.annotations.NotNull
        java.util.List<com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetItem> items, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetItem, kotlin.Unit> onItemClick) {
            super();
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetAdapter.ViewHolder holder, int position) {
        }
        
        @java.lang.Override
        public int getItemCount() {
            return 0;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\bR\u0011\u0010\u0011\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f\u00a8\u0006\u0013"}, d2 = {"Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet$WidgetAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet$WidgetAdapter;Landroid/view/View;)V", "iconImage", "Landroid/widget/ImageView;", "getIconImage", "()Landroid/widget/ImageView;", "nameText", "Landroid/widget/TextView;", "getNameText", "()Landroid/widget/TextView;", "packageText", "getPackageText", "previewImage", "getPreviewImage", "sizeText", "getSizeText", "app_debug"})
        public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull
            private final android.widget.ImageView previewImage = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.ImageView iconImage = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView nameText = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView packageText = null;
            @org.jetbrains.annotations.NotNull
            private final android.widget.TextView sizeText = null;
            
            public ViewHolder(@org.jetbrains.annotations.NotNull
            android.view.View view) {
                super(null);
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.ImageView getPreviewImage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.ImageView getIconImage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getNameText() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getPackageText() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final android.widget.TextView getSizeText() {
                return null;
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0014\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u000bH\u00c6\u0003JK\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010\u001d\u001a\u00020\u000b2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\""}, d2 = {"Lcom/customlauncher/app/ui/dialog/WidgetPickerBottomSheet$WidgetItem;", "", "label", "", "packageName", "icon", "Landroid/graphics/drawable/Drawable;", "preview", "providerInfo", "Landroid/appwidget/AppWidgetProviderInfo;", "isCustom", "", "(Ljava/lang/String;Ljava/lang/String;Landroid/graphics/drawable/Drawable;Landroid/graphics/drawable/Drawable;Landroid/appwidget/AppWidgetProviderInfo;Z)V", "getIcon", "()Landroid/graphics/drawable/Drawable;", "()Z", "getLabel", "()Ljava/lang/String;", "getPackageName", "getPreview", "getProviderInfo", "()Landroid/appwidget/AppWidgetProviderInfo;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
    public static final class WidgetItem {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String label = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String packageName = null;
        @org.jetbrains.annotations.Nullable
        private final android.graphics.drawable.Drawable icon = null;
        @org.jetbrains.annotations.Nullable
        private final android.graphics.drawable.Drawable preview = null;
        @org.jetbrains.annotations.Nullable
        private final android.appwidget.AppWidgetProviderInfo providerInfo = null;
        private final boolean isCustom = false;
        
        public WidgetItem(@org.jetbrains.annotations.NotNull
        java.lang.String label, @org.jetbrains.annotations.NotNull
        java.lang.String packageName, @org.jetbrains.annotations.Nullable
        android.graphics.drawable.Drawable icon, @org.jetbrains.annotations.Nullable
        android.graphics.drawable.Drawable preview, @org.jetbrains.annotations.Nullable
        android.appwidget.AppWidgetProviderInfo providerInfo, boolean isCustom) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getLabel() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getPackageName() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.graphics.drawable.Drawable getIcon() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.graphics.drawable.Drawable getPreview() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.appwidget.AppWidgetProviderInfo getProviderInfo() {
            return null;
        }
        
        public final boolean isCustom() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.graphics.drawable.Drawable component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.graphics.drawable.Drawable component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.appwidget.AppWidgetProviderInfo component5() {
            return null;
        }
        
        public final boolean component6() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet.WidgetItem copy(@org.jetbrains.annotations.NotNull
        java.lang.String label, @org.jetbrains.annotations.NotNull
        java.lang.String packageName, @org.jetbrains.annotations.Nullable
        android.graphics.drawable.Drawable icon, @org.jetbrains.annotations.Nullable
        android.graphics.drawable.Drawable preview, @org.jetbrains.annotations.Nullable
        android.appwidget.AppWidgetProviderInfo providerInfo, boolean isCustom) {
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
}
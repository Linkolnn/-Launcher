package com.customlauncher.app.ui.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0019B\u0019\u0012\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u00a2\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0016J\u001c\u0010\u000e\u001a\u00020\u00062\n\u0010\u000f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\rH\u0016J\u001c\u0010\u0011\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\rH\u0016J\u0014\u0010\u0015\u001a\u00020\u00062\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\tJ\u0010\u0010\u0017\u001a\u00020\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\u000bR\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/customlauncher/app/ui/adapter/IconPackAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/customlauncher/app/ui/adapter/IconPackAdapter$IconPackViewHolder;", "onIconPackSelected", "Lkotlin/Function1;", "Lcom/customlauncher/app/data/model/IconPack;", "", "(Lkotlin/jvm/functions/Function1;)V", "iconPacks", "", "selectedPackage", "", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setIconPacks", "packs", "setSelectedPack", "packageName", "IconPackViewHolder", "app_debug"})
public final class IconPackAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.customlauncher.app.ui.adapter.IconPackAdapter.IconPackViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.customlauncher.app.data.model.IconPack, kotlin.Unit> onIconPackSelected = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<com.customlauncher.app.data.model.IconPack> iconPacks;
    @org.jetbrains.annotations.Nullable
    private java.lang.String selectedPackage;
    
    public IconPackAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.customlauncher.app.data.model.IconPack, kotlin.Unit> onIconPackSelected) {
        super();
    }
    
    public final void setIconPacks(@org.jetbrains.annotations.NotNull
    java.util.List<com.customlauncher.app.data.model.IconPack> packs) {
    }
    
    public final void setSelectedPack(@org.jetbrains.annotations.Nullable
    java.lang.String packageName) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.customlauncher.app.ui.adapter.IconPackAdapter.IconPackViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.ui.adapter.IconPackAdapter.IconPackViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/customlauncher/app/ui/adapter/IconPackAdapter$IconPackViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/customlauncher/app/ui/adapter/IconPackAdapter;Landroid/view/View;)V", "checkMarkContainer", "Landroid/widget/FrameLayout;", "iconPackIcon", "Landroid/widget/ImageView;", "iconPackName", "Landroid/widget/TextView;", "selectionRing", "bind", "", "iconPack", "Lcom/customlauncher/app/data/model/IconPack;", "app_debug"})
    public final class IconPackViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.ImageView iconPackIcon = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView iconPackName = null;
        @org.jetbrains.annotations.NotNull
        private final android.view.View selectionRing = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.FrameLayout checkMarkContainer = null;
        
        public IconPackViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.IconPack iconPack) {
        }
    }
}
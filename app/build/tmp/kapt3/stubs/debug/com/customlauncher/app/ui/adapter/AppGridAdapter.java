package com.customlauncher.app.ui.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0001:\u0002\u0011\u0012B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u001c\u0010\b\u001a\u00020\u00062\n\u0010\t\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u001c\u0010\f\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0016J\u0014\u0010\u0010\u001a\u00020\u00062\n\u0010\t\u001a\u00060\u0003R\u00020\u0000H\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/customlauncher/app/ui/adapter/AppGridAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/customlauncher/app/data/model/AppInfo;", "Lcom/customlauncher/app/ui/adapter/AppGridAdapter$ViewHolder;", "onAppClick", "Lkotlin/Function1;", "", "(Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onViewRecycled", "AppDiffCallback", "ViewHolder", "app_debug"})
public final class AppGridAdapter extends androidx.recyclerview.widget.ListAdapter<com.customlauncher.app.data.model.AppInfo, com.customlauncher.app.ui.adapter.AppGridAdapter.ViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.customlauncher.app.data.model.AppInfo, kotlin.Unit> onAppClick = null;
    
    public AppGridAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.customlauncher.app.data.model.AppInfo, kotlin.Unit> onAppClick) {
        super(null);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.customlauncher.app.ui.adapter.AppGridAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.ui.adapter.AppGridAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public void onViewRecycled(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.ui.adapter.AppGridAdapter.ViewHolder holder) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/customlauncher/app/ui/adapter/AppGridAdapter$AppDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/customlauncher/app/data/model/AppInfo;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    static final class AppDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.customlauncher.app.data.model.AppInfo> {
        
        public AppDiffCallback() {
            super();
        }
        
        @java.lang.Override
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.AppInfo oldItem, @org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.AppInfo newItem) {
            return false;
        }
        
        @java.lang.Override
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.AppInfo oldItem, @org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.AppInfo newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/customlauncher/app/ui/adapter/AppGridAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/customlauncher/app/ui/adapter/AppGridAdapter;Landroid/view/View;)V", "appIcon", "Landroid/widget/ImageView;", "appName", "Landroid/widget/TextView;", "iconLoadJob", "Lkotlinx/coroutines/Job;", "bind", "", "app", "Lcom/customlauncher/app/data/model/AppInfo;", "cancelIconLoad", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.ImageView appIcon = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView appName = null;
        @org.jetbrains.annotations.Nullable
        private kotlinx.coroutines.Job iconLoadJob;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.AppInfo app) {
        }
        
        public final void cancelIconLoad() {
        }
    }
}
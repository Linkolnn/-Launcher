package com.customlauncher.app.ui.adapter;

/**
 * ViewHolder for home screen items to improve performance
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\n\u000bB\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\u0006H&\u0082\u0001\u0002\f\r\u00a8\u0006\u000e"}, d2 = {"Lcom/customlauncher/app/ui/adapter/HomeScreenViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "bind", "", "item", "Lcom/customlauncher/app/data/model/HomeItemModel;", "unbind", "AppViewHolder", "WidgetViewHolder", "Lcom/customlauncher/app/ui/adapter/HomeScreenViewHolder$AppViewHolder;", "Lcom/customlauncher/app/ui/adapter/HomeScreenViewHolder$WidgetViewHolder;", "app_debug"})
public abstract class HomeScreenViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
    
    private HomeScreenViewHolder(android.view.View itemView) {
        super(null);
    }
    
    public abstract void bind(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel item);
    
    public abstract void unbind();
    
    /**
     * ViewHolder for app shortcuts
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u000eH\u0016R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0012"}, d2 = {"Lcom/customlauncher/app/ui/adapter/HomeScreenViewHolder$AppViewHolder;", "Lcom/customlauncher/app/ui/adapter/HomeScreenViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "appIcon", "Landroid/widget/ImageView;", "getAppIcon", "()Landroid/widget/ImageView;", "appLabel", "Landroid/widget/TextView;", "getAppLabel", "()Landroid/widget/TextView;", "bind", "", "item", "Lcom/customlauncher/app/data/model/HomeItemModel;", "unbind", "app_debug"})
    public static final class AppViewHolder extends com.customlauncher.app.ui.adapter.HomeScreenViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.ImageView appIcon = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView appLabel = null;
        
        public AppViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.ImageView getAppIcon() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getAppLabel() {
            return null;
        }
        
        @java.lang.Override
        public void bind(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.HomeItemModel item) {
        }
        
        @java.lang.Override
        public void unbind() {
        }
    }
    
    /**
     * ViewHolder for widgets
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\u0006H\u0016\u00a8\u0006\n"}, d2 = {"Lcom/customlauncher/app/ui/adapter/HomeScreenViewHolder$WidgetViewHolder;", "Lcom/customlauncher/app/ui/adapter/HomeScreenViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "bind", "", "item", "Lcom/customlauncher/app/data/model/HomeItemModel;", "unbind", "app_debug"})
    public static final class WidgetViewHolder extends com.customlauncher.app.ui.adapter.HomeScreenViewHolder {
        
        public WidgetViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
        }
        
        @java.lang.Override
        public void bind(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.HomeItemModel item) {
        }
        
        @java.lang.Override
        public void unbind() {
        }
    }
}
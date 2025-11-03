package com.customlauncher.app.ui.adapter;

/**
 * Adapter for displaying items on the home screen grid
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\t\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010 \n\u0002\b\u0007\u0018\u0000 J2\u00020\u0001:\u0001JBy\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007\u0012\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u0012\u001c\b\u0002\u0010\u000e\u001a\u0016\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r\u0018\u00010\u000b\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010\'\u001a\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0002J\u0010\u0010)\u001a\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0002J\u0010\u0010*\u001a\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0002J\u0006\u0010+\u001a\u00020\tJ\b\u0010,\u001a\u00020\tH\u0002J\u0006\u0010-\u001a\u00020\tJ\u0006\u0010.\u001a\u00020\u0017J\u0018\u0010/\u001a\u00020\t2\u0006\u0010(\u001a\u00020\b2\u0006\u00100\u001a\u000201H\u0002J\u0014\u00102\u001a\u0004\u0018\u0001032\b\u00104\u001a\u0004\u0018\u000105H\u0002J\u001e\u00106\u001a\u00020\r2\u0006\u00107\u001a\u00020\u001e2\u0006\u00108\u001a\u00020\u00172\u0006\u00109\u001a\u00020\u0017J\u0006\u0010:\u001a\u00020\tJ\u000e\u0010;\u001a\u00020\t2\u0006\u00107\u001a\u00020\u001eJ\u000e\u0010<\u001a\u00020\t2\u0006\u0010(\u001a\u00020\bJ\u000e\u0010=\u001a\u00020\t2\u0006\u00104\u001a\u000205J\u000e\u0010>\u001a\u00020\t2\u0006\u0010?\u001a\u00020\rJ\u000e\u0010@\u001a\u00020\t2\u0006\u0010A\u001a\u00020\u0017J\u0014\u0010B\u001a\u00020\t2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\b0DJ\u000e\u0010E\u001a\u00020\t2\u0006\u0010F\u001a\u00020\rJ\u000e\u0010G\u001a\u00020\t2\u0006\u0010H\u001a\u00020\bJ\u0014\u0010I\u001a\u00020\t2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\b0DR\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0018\u001a\n \u001a*\u0004\u0018\u00010\u00190\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\f0\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\b0 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u000e\u001a\u0016\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\r\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010#\u001a\n \u001a*\u0004\u0018\u00010$0$X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006K"}, d2 = {"Lcom/customlauncher/app/ui/adapter/HomeScreenAdapter;", "", "context", "Landroid/content/Context;", "gridLayout", "Lcom/customlauncher/app/ui/layout/HomeScreenGridLayout;", "onItemClick", "Lkotlin/Function1;", "Lcom/customlauncher/app/data/model/HomeItemModel;", "", "onItemLongClick", "Lkotlin/Function2;", "Landroid/view/View;", "", "onItemStartDrag", "appWidgetHost", "Landroid/appwidget/AppWidgetHost;", "appWidgetManager", "Landroid/appwidget/AppWidgetManager;", "(Landroid/content/Context;Lcom/customlauncher/app/ui/layout/HomeScreenGridLayout;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Landroid/appwidget/AppWidgetHost;Landroid/appwidget/AppWidgetManager;)V", "adapterScope", "Lkotlinx/coroutines/CoroutineScope;", "focusedPosition", "", "inflater", "Landroid/view/LayoutInflater;", "kotlin.jvm.PlatformType", "isButtonMode", "itemViews", "", "", "items", "", "lazyWidgetLoader", "Lcom/customlauncher/app/ui/widget/LazyWidgetLoader;", "packageManager", "Landroid/content/pm/PackageManager;", "viewPool", "Lcom/customlauncher/app/ui/adapter/HomeScreenViewPool;", "addAppShortcut", "item", "addItemToGrid", "addWidget", "clearAllViews", "clearAllViewsAnimated", "clearFocus", "getItemCount", "loadAppIcon", "iconView", "Landroid/widget/ImageView;", "loadIconByPackageName", "Landroid/graphics/drawable/Drawable;", "packageName", "", "moveItem", "itemId", "newX", "newY", "onDestroy", "removeItem", "removeItemAnimated", "removePackageAnimated", "setButtonMode", "enabled", "setFocusedPosition", "position", "submitList", "newItems", "", "updateHiddenMode", "isHidden", "updateItem", "updatedItem", "updateItemsList", "Companion", "app_debug"})
public final class HomeScreenAdapter {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.ui.layout.HomeScreenGridLayout gridLayout = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.customlauncher.app.data.model.HomeItemModel, kotlin.Unit> onItemClick = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<com.customlauncher.app.data.model.HomeItemModel, android.view.View, java.lang.Boolean> onItemLongClick = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.jvm.functions.Function2<android.view.View, com.customlauncher.app.data.model.HomeItemModel, java.lang.Boolean> onItemStartDrag = null;
    @org.jetbrains.annotations.Nullable
    private final android.appwidget.AppWidgetHost appWidgetHost = null;
    @org.jetbrains.annotations.Nullable
    private final android.appwidget.AppWidgetManager appWidgetManager = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.customlauncher.app.data.model.HomeItemModel> items = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<java.lang.Long, android.view.View> itemViews = null;
    private final android.content.pm.PackageManager packageManager = null;
    private final android.view.LayoutInflater inflater = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineScope adapterScope = null;
    private int focusedPosition = -1;
    private boolean isButtonMode = false;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.ui.adapter.HomeScreenViewPool viewPool = null;
    @org.jetbrains.annotations.Nullable
    private final com.customlauncher.app.ui.widget.LazyWidgetLoader lazyWidgetLoader = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "HomeScreenAdapter";
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.adapter.HomeScreenAdapter.Companion Companion = null;
    
    public HomeScreenAdapter(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.ui.layout.HomeScreenGridLayout gridLayout, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.customlauncher.app.data.model.HomeItemModel, kotlin.Unit> onItemClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.customlauncher.app.data.model.HomeItemModel, ? super android.view.View, java.lang.Boolean> onItemLongClick, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function2<? super android.view.View, ? super com.customlauncher.app.data.model.HomeItemModel, java.lang.Boolean> onItemStartDrag, @org.jetbrains.annotations.Nullable
    android.appwidget.AppWidgetHost appWidgetHost, @org.jetbrains.annotations.Nullable
    android.appwidget.AppWidgetManager appWidgetManager) {
        super();
    }
    
    /**
     * Get current item count
     */
    public final int getItemCount() {
        return 0;
    }
    
    /**
     * Update displayed items
     */
    public final void submitList(@org.jetbrains.annotations.NotNull
    java.util.List<com.customlauncher.app.data.model.HomeItemModel> newItems) {
    }
    
    /**
     * Clear all views with animation
     */
    private final void clearAllViewsAnimated() {
    }
    
    /**
     * Update internal items list without rebuilding views
     * Used when deleting items to preserve positions
     */
    public final void updateItemsList(@org.jetbrains.annotations.NotNull
    java.util.List<com.customlauncher.app.data.model.HomeItemModel> newItems) {
    }
    
    /**
     * Update single item (e.g., after resize)
     */
    public final void updateItem(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel updatedItem) {
    }
    
    /**
     * Remove single item with animation
     */
    public final void removeItemAnimated(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel item) {
    }
    
    /**
     * Remove item by package name with animation
     */
    public final void removePackageAnimated(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
    }
    
    /**
     * Add single item to grid
     */
    private final void addItemToGrid(com.customlauncher.app.data.model.HomeItemModel item) {
    }
    
    /**
     * Add app shortcut to grid
     */
    private final void addAppShortcut(com.customlauncher.app.data.model.HomeItemModel item) {
    }
    
    /**
     * Add a widget to the grid
     */
    private final void addWidget(com.customlauncher.app.data.model.HomeItemModel item) {
    }
    
    /**
     * Load app icon
     */
    private final void loadAppIcon(com.customlauncher.app.data.model.HomeItemModel item, android.widget.ImageView iconView) {
    }
    
    /**
     * Load icon by package name
     */
    private final android.graphics.drawable.Drawable loadIconByPackageName(java.lang.String packageName) {
        return null;
    }
    
    /**
     * Remove item from grid
     */
    public final void removeItem(long itemId) {
    }
    
    /**
     * Clear all views from grid and recycle them to pool
     */
    public final void clearAllViews() {
    }
    
    /**
     * Move item to new position
     */
    public final boolean moveItem(long itemId, int newX, int newY) {
        return false;
    }
    
    /**
     * Set the focused position and update visual state
     */
    public final void setFocusedPosition(int position) {
    }
    
    /**
     * Clear focus from all items
     */
    public final void clearFocus() {
    }
    
    /**
     * Set button mode state
     */
    public final void setButtonMode(boolean enabled) {
    }
    
    /**
     * Update visibility of items based on hidden mode
     */
    public final void updateHiddenMode(boolean isHidden) {
    }
    
    /**
     * Clean up resources
     */
    public final void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/customlauncher/app/ui/adapter/HomeScreenAdapter$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
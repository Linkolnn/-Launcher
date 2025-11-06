package com.customlauncher.app.ui.layout;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0016H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\u0005J\u0006\u0010\u001d\u001a\u00020\u0016J\u0006\u0010\u001e\u001a\u00020\u0016J\u0006\u0010\u001f\u001a\u00020\u0016J\u0006\u0010 \u001a\u00020\u0016J\u0006\u0010!\u001a\u00020\u0016J\u000e\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0005J\u0006\u0010%\u001a\u00020\u0016J\u001c\u0010&\u001a\u00020#2\n\u0010\'\u001a\u00060(R\u00020)2\u0006\u0010*\u001a\u00020+H\u0016J\u0006\u0010,\u001a\u00020#J\u0010\u0010-\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0005H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001e\u0010\u000b\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\tR\u0011\u0010\r\u001a\u00020\u00058F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\tR\u001a\u0010\u0010\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\t\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcom/customlauncher/app/ui/layout/PaginatedGridLayoutManager;", "Landroidx/recyclerview/widget/RecyclerView$LayoutManager;", "context", "Landroid/content/Context;", "columns", "", "rows", "(Landroid/content/Context;II)V", "getColumns", "()I", "<set-?>", "currentPage", "getCurrentPage", "pageSize", "getPageSize", "getRows", "selectedPosition", "getSelectedPosition", "setSelectedPosition", "(I)V", "totalPages", "canScrollHorizontally", "", "canScrollVertically", "generateDefaultLayoutParams", "Landroidx/recyclerview/widget/RecyclerView$LayoutParams;", "getCurrentPageInfo", "", "getTotalPages", "goToNextPage", "goToPreviousPage", "navigateDown", "navigateLeft", "navigateRight", "navigateToPosition", "", "position", "navigateUp", "onLayoutChildren", "recycler", "Landroidx/recyclerview/widget/RecyclerView$Recycler;", "Landroidx/recyclerview/widget/RecyclerView;", "state", "Landroidx/recyclerview/widget/RecyclerView$State;", "resetToFirstPage", "scrollToPosition", "app_debug"})
public final class PaginatedGridLayoutManager extends androidx.recyclerview.widget.RecyclerView.LayoutManager {
    private final int columns = 0;
    private final int rows = 0;
    private int currentPage = 0;
    private int totalPages = 0;
    private int selectedPosition = 0;
    
    public PaginatedGridLayoutManager(@org.jetbrains.annotations.NotNull
    android.content.Context context, int columns, int rows) {
        super();
    }
    
    public final int getColumns() {
        return 0;
    }
    
    public final int getRows() {
        return 0;
    }
    
    public final int getPageSize() {
        return 0;
    }
    
    public final int getCurrentPage() {
        return 0;
    }
    
    public final int getSelectedPosition() {
        return 0;
    }
    
    public final void setSelectedPosition(int p0) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public androidx.recyclerview.widget.RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }
    
    @java.lang.Override
    public void onLayoutChildren(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.Recycler recycler, @org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.State state) {
    }
    
    public final void navigateToPosition(int position) {
    }
    
    @java.lang.Override
    public void scrollToPosition(int position) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrentPageInfo() {
        return null;
    }
    
    public final boolean navigateLeft() {
        return false;
    }
    
    public final boolean navigateRight() {
        return false;
    }
    
    public final boolean navigateUp() {
        return false;
    }
    
    public final boolean navigateDown() {
        return false;
    }
    
    public final int getTotalPages() {
        return 0;
    }
    
    public final boolean goToNextPage() {
        return false;
    }
    
    public final boolean goToPreviousPage() {
        return false;
    }
    
    public final void resetToFirstPage() {
    }
    
    @java.lang.Override
    public boolean canScrollHorizontally() {
        return false;
    }
    
    @java.lang.Override
    public boolean canScrollVertically() {
        return false;
    }
}
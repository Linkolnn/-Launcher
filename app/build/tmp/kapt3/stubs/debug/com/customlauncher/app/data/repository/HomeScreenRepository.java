package com.customlauncher.app.data.repository;

/**
 * Repository for managing home screen items
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0014\u0018\u0000 @2\u00020\u0001:\u0001@B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0019\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0019\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J,\u0010\u0016\u001a\u0016\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0018H\u0002J1\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u001e\u0018\u00010\u001d2\u0006\u0010\u001f\u001a\u00020 2\b\b\u0002\u0010!\u001a\u00020\u001eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"J\u001f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u000f0$2\u0006\u0010\u001f\u001a\u00020 H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000f0$H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\'J\u0012\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0$0)J\u001c\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0$0)2\b\b\u0002\u0010!\u001a\u00020\u001eJ\u0019\u0010+\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020 H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J+\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u00020\u001e2\b\b\u0002\u0010!\u001a\u00020\u001eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100J)\u00101\u001a\u00020\u00122\u0006\u00102\u001a\u00020\r2\u0006\u00103\u001a\u00020\u001e2\u0006\u00104\u001a\u00020\u001eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105J!\u00106\u001a\u00020\u001e2\u0006\u00107\u001a\u00020\u001e2\u0006\u00108\u001a\u00020\u001eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00109J\u0011\u0010:\u001a\u00020\u0012H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\'J\u0019\u0010;\u001a\u00020\u00122\u0006\u0010<\u001a\u00020\u0018H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010=J\u0019\u0010>\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020 H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J\u0019\u0010?\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006A"}, d2 = {"Lcom/customlauncher/app/data/repository/HomeScreenRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "database", "Lcom/customlauncher/app/data/database/HomeScreenDatabase;", "homeItemDao", "Lcom/customlauncher/app/data/database/HomeItemDao;", "packageManager", "Landroid/content/pm/PackageManager;", "kotlin.jvm.PlatformType", "addItem", "", "model", "Lcom/customlauncher/app/data/model/HomeItemModel;", "(Lcom/customlauncher/app/data/model/HomeItemModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteItem", "", "deleteItemById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findDefaultApp", "Lkotlin/Triple;", "", "intent", "Landroid/content/Intent;", "fallbackLabel", "findEmptyPosition", "Lkotlin/Pair;", "", "gridConfig", "Lcom/customlauncher/app/data/model/GridConfiguration;", "screen", "(Lcom/customlauncher/app/data/model/GridConfiguration;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fixOutOfBoundsItems", "", "(Lcom/customlauncher/app/data/model/GridConfiguration;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllItems", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllItemsFlow", "Lkotlinx/coroutines/flow/Flow;", "getItemsByScreenFlow", "initializeDefaultItems", "isPositionAvailable", "", "x", "y", "(IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "moveItem", "itemId", "newX", "newY", "(JIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recalculatePositionsForNewGrid", "newColumns", "newRows", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeDuplicates", "removeItemsForPackage", "packageName", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetToDefaults", "updateItem", "Companion", "app_debug"})
public final class HomeScreenRepository {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.database.HomeScreenDatabase database = null;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.database.HomeItemDao homeItemDao = null;
    private final android.content.pm.PackageManager packageManager = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "HomeScreenRepository";
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.data.repository.HomeScreenRepository.Companion Companion = null;
    
    public HomeScreenRepository(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    /**
     * Get all items as Flow
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.customlauncher.app.data.model.HomeItemModel>> getAllItemsFlow() {
        return null;
    }
    
    /**
     * Get all items directly (not as Flow)
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getAllItems(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.model.HomeItemModel>> $completion) {
        return null;
    }
    
    /**
     * Get items for specific screen
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.customlauncher.app.data.model.HomeItemModel>> getItemsByScreenFlow(int screen) {
        return null;
    }
    
    /**
     * Add new item to home screen
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object addItem(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel model, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * Update existing item
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateItem(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel model, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Delete item
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object deleteItem(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel model, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Delete item by id
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object deleteItemById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Move item to new position
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object moveItem(long itemId, int newX, int newY, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Check if position is available
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object isPositionAvailable(int x, int y, int screen, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Find first empty position on screen
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object findEmptyPosition(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig, int screen, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Integer, java.lang.Integer>> $completion) {
        return null;
    }
    
    /**
     * Reset home screen to default state (for testing)
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object resetToDefaults(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Remove duplicate items from database
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object removeDuplicates(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Initialize default home screen items
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object initializeDefaultItems(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Find default app for an intent
     */
    private final kotlin.Triple<java.lang.String, java.lang.String, java.lang.String> findDefaultApp(android.content.Intent intent, java.lang.String fallbackLabel) {
        return null;
    }
    
    /**
     * Remove items for uninstalled package
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object removeItemsForPackage(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Recalculate item positions after grid resize
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object recalculatePositionsForNewGrid(int newColumns, int newRows, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    /**
     * Check and fix out-of-bounds items after grid resize
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object fixOutOfBoundsItems(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.model.HomeItemModel>> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/customlauncher/app/data/repository/HomeScreenRepository$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
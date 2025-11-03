package com.customlauncher.app.data.database;

/**
 * DAO for home screen items
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\f\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\u0007\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0019\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u0019\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J#\u0010\u0011\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0014\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00180\u0017H\'J\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u0018H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u001b\u0010\u001a\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ+\u0010\u001b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eJ\u0011\u0010\u001f\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0019\u0010 \u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010!J1\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00050\u00182\u0006\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00132\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u001f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u00182\u0006\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u001c\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00180\u00172\u0006\u0010\u0012\u001a\u00020\u0013H\'J\u0019\u0010&\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001f\u0010\'\u001a\u00020\u00032\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\u0018H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J+\u0010*\u001a\u00020+2\u0006\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00132\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J)\u0010,\u001a\u00020\u00032\u0006\u0010-\u001a\u00020\u000b2\u0006\u0010.\u001a\u00020\u00132\u0006\u0010/\u001a\u00020\u0013H\u0097@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100J!\u00101\u001a\u00020\u00032\u0006\u00102\u001a\u00020\u000b2\u0006\u00103\u001a\u00020\u000bH\u0097@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00104J\u0019\u00105\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001f\u00106\u001a\u00020\u00032\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\u0018H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00067"}, d2 = {"Lcom/customlauncher/app/data/database/HomeItemDao;", "", "delete", "", "item", "Lcom/customlauncher/app/data/database/HomeItem;", "(Lcom/customlauncher/app/data/database/HomeItem;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteByPackage", "packageName", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findFirstEmptyPosition", "screen", "", "columns", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllItems", "Lkotlinx/coroutines/flow/Flow;", "", "getAllItemsOnce", "getItemById", "getItemByPackageAndPosition", "x", "y", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getItemCount", "getItemCountByScreen", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getItemsAtPosition", "(IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getItemsByPackage", "getItemsByScreen", "insert", "insertAll", "items", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isPositionOccupied", "", "moveItem", "itemId", "newX", "newY", "(JIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "swapItems", "itemId1", "itemId2", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "updateAll", "app_debug"})
@androidx.room.Dao
public abstract interface HomeItemDao {
    
    @androidx.room.Query(value = "SELECT * FROM home_items ORDER BY screen, cellY, cellX")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.customlauncher.app.data.database.HomeItem>> getAllItems();
    
    @androidx.room.Query(value = "SELECT * FROM home_items ORDER BY screen, cellY, cellX")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getAllItemsOnce(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.database.HomeItem>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM home_items WHERE screen = :screen ORDER BY cellY, cellX")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.customlauncher.app.data.database.HomeItem>> getItemsByScreen(int screen);
    
    @androidx.room.Query(value = "SELECT * FROM home_items WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getItemById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.customlauncher.app.data.database.HomeItem> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM home_items WHERE packageName = :packageName")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getItemsByPackage(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.database.HomeItem>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM home_items WHERE cellX = :x AND cellY = :y AND screen = :screen")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getItemsAtPosition(int x, int y, int screen, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.database.HomeItem>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM home_items WHERE packageName = :packageName AND cellX = :x AND cellY = :y")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getItemByPackageAndPosition(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, int x, int y, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.customlauncher.app.data.database.HomeItem> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.database.HomeItem item, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull
    java.util.List<com.customlauncher.app.data.database.HomeItem> items, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.database.HomeItem item, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateAll(@org.jetbrains.annotations.NotNull
    java.util.List<com.customlauncher.app.data.database.HomeItem> items, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.database.HomeItem item, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM home_items WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM home_items WHERE packageName = :packageName")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteByPackage(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM home_items")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM home_items")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getItemCount(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM home_items WHERE screen = :screen")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getItemCountByScreen(int screen, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    /**
     * Check if a position is occupied
     */
    @androidx.room.Query(value = "\n        SELECT EXISTS(\n            SELECT 1 FROM home_items \n            WHERE screen = :screen \n            AND :x >= cellX AND :x < cellX + spanX\n            AND :y >= cellY AND :y < cellY + spanY\n        )\n    ")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object isPositionOccupied(int x, int y, int screen, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    /**
     * Find first empty cell on screen
     */
    @androidx.room.Query(value = "\n        SELECT * FROM home_items \n        WHERE screen = :screen \n        ORDER BY cellY * :columns + cellX \n        LIMIT 1\n    ")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object findFirstEmptyPosition(int screen, int columns, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.customlauncher.app.data.database.HomeItem> $completion);
    
    /**
     * Move item to new position
     */
    @androidx.room.Transaction
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object moveItem(long itemId, int newX, int newY, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Swap positions of two items
     */
    @androidx.room.Transaction
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object swapItems(long itemId1, long itemId2, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * DAO for home screen items
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        /**
         * Move item to new position
         */
        @androidx.room.Transaction
        @org.jetbrains.annotations.Nullable
        public static java.lang.Object moveItem(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.database.HomeItemDao $this, long itemId, int newX, int newY, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
        
        /**
         * Swap positions of two items
         */
        @androidx.room.Transaction
        @org.jetbrains.annotations.Nullable
        public static java.lang.Object swapItems(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.database.HomeItemDao $this, long itemId1, long itemId2, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}
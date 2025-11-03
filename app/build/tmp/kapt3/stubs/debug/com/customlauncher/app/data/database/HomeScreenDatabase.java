package com.customlauncher.app.data.database;

/**
 * Room database for home screen items
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcom/customlauncher/app/data/database/HomeScreenDatabase;", "Landroidx/room/RoomDatabase;", "()V", "homeItemDao", "Lcom/customlauncher/app/data/database/HomeItemDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.customlauncher.app.data.database.HomeItem.class}, version = 1, exportSchema = true)
@androidx.room.TypeConverters(value = {com.customlauncher.app.data.database.ItemTypeConverter.class})
public abstract class HomeScreenDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String DATABASE_NAME = "home_screen_database";
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.Nullable
    private static volatile com.customlauncher.app.data.database.HomeScreenDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.data.database.HomeScreenDatabase.Companion Companion = null;
    
    public HomeScreenDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract com.customlauncher.app.data.database.HomeItemDao homeItemDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0002J\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2 = {"Lcom/customlauncher/app/data/database/HomeScreenDatabase$Companion;", "", "()V", "DATABASE_NAME", "", "INSTANCE", "Lcom/customlauncher/app/data/database/HomeScreenDatabase;", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "getMIGRATION_1_2", "()Landroidx/room/migration/Migration;", "buildDatabase", "context", "Landroid/content/Context;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.data.database.HomeScreenDatabase getInstance(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
        
        private final com.customlauncher.app.data.database.HomeScreenDatabase buildDatabase(android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final androidx.room.migration.Migration getMIGRATION_1_2() {
            return null;
        }
    }
}
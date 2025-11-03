package com.customlauncher.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room database for home screen items
 */
@Database(
    entities = [HomeItem::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(ItemTypeConverter::class)
abstract class HomeScreenDatabase : RoomDatabase() {
    
    abstract fun homeItemDao(): HomeItemDao
    
    companion object {
        private const val DATABASE_NAME = "home_screen_database"
        
        @Volatile
        private var INSTANCE: HomeScreenDatabase? = null
        
        fun getInstance(context: Context): HomeScreenDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        
        private fun buildDatabase(context: Context): HomeScreenDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                HomeScreenDatabase::class.java,
                DATABASE_NAME
            )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Database created for the first time
                    // Default items will be added by repository
                }
            })
            .build()
        }
        
        // Migration from version 1 to 2 (example for future use)
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration code here when needed
                // Example: database.execSQL("ALTER TABLE home_items ADD COLUMN new_field TEXT")
            }
        }
    }
}

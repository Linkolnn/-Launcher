package com.customlauncher.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HiddenApp::class], version = 1, exportSchema = false)
abstract class LauncherDatabase : RoomDatabase() {
    abstract fun hiddenAppDao(): HiddenAppDao
    
    companion object {
        @Volatile
        private var INSTANCE: LauncherDatabase? = null
        
        fun getDatabase(context: Context): LauncherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LauncherDatabase::class.java,
                    "launcher_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

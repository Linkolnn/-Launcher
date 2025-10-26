package com.customlauncher.app

import android.app.Application
import com.customlauncher.app.data.database.LauncherDatabase
import com.customlauncher.app.data.preferences.LauncherPreferences
import com.customlauncher.app.data.repository.AppRepository

class LauncherApplication : Application() {
    
    val database by lazy { LauncherDatabase.getDatabase(this) }
    val repository by lazy { AppRepository(this, database.hiddenAppDao()) }
    val preferences by lazy { LauncherPreferences(this) }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Don't reset state - preserve user's choice
        // The hidden mode should persist until user explicitly changes it
    }
    
    companion object {
        lateinit var instance: LauncherApplication
            private set
    }
}

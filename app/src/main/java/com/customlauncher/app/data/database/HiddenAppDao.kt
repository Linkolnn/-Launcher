package com.customlauncher.app.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HiddenAppDao {
    @Query("SELECT * FROM hidden_apps")
    fun getAllHiddenApps(): Flow<List<HiddenApp>>
    
    @Query("SELECT * FROM hidden_apps")
    suspend fun getAllHiddenAppsList(): List<HiddenApp>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hiddenApp: HiddenApp)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(hiddenApps: List<HiddenApp>)
    
    @Delete
    suspend fun delete(hiddenApp: HiddenApp)
    
    @Query("DELETE FROM hidden_apps WHERE packageName = :packageName")
    suspend fun deleteByPackageName(packageName: String)
    
    @Query("DELETE FROM hidden_apps")
    suspend fun deleteAll()
    
    @Query("SELECT EXISTS(SELECT 1 FROM hidden_apps WHERE packageName = :packageName)")
    suspend fun isAppHidden(packageName: String): Boolean
}

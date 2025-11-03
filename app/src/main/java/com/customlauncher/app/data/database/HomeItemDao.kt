package com.customlauncher.app.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for home screen items
 */
@Dao
interface HomeItemDao {
    
    @Query("SELECT * FROM home_items ORDER BY screen, cellY, cellX")
    fun getAllItems(): Flow<List<HomeItem>>
    
    @Query("SELECT * FROM home_items ORDER BY screen, cellY, cellX")
    suspend fun getAllItemsOnce(): List<HomeItem>
    
    @Query("SELECT * FROM home_items WHERE screen = :screen ORDER BY cellY, cellX")
    fun getItemsByScreen(screen: Int): Flow<List<HomeItem>>
    
    @Query("SELECT * FROM home_items WHERE id = :id")
    suspend fun getItemById(id: Long): HomeItem?
    
    @Query("SELECT * FROM home_items WHERE packageName = :packageName")
    suspend fun getItemsByPackage(packageName: String): List<HomeItem>
    
    @Query("SELECT * FROM home_items WHERE cellX = :x AND cellY = :y AND screen = :screen")
    suspend fun getItemsAtPosition(x: Int, y: Int, screen: Int = 0): List<HomeItem>
    
    @Query("SELECT * FROM home_items WHERE packageName = :packageName AND cellX = :x AND cellY = :y")
    suspend fun getItemByPackageAndPosition(packageName: String, x: Int, y: Int): HomeItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HomeItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HomeItem>)
    
    @Update
    suspend fun update(item: HomeItem)
    
    @Update
    suspend fun updateAll(items: List<HomeItem>)
    
    @Delete
    suspend fun delete(item: HomeItem)
    
    @Query("DELETE FROM home_items WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM home_items WHERE packageName = :packageName")
    suspend fun deleteByPackage(packageName: String)
    
    @Query("DELETE FROM home_items")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM home_items")
    suspend fun getItemCount(): Int
    
    @Query("SELECT COUNT(*) FROM home_items WHERE screen = :screen")
    suspend fun getItemCountByScreen(screen: Int): Int
    
    /**
     * Check if a position is occupied
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM home_items 
            WHERE screen = :screen 
            AND :x >= cellX AND :x < cellX + spanX
            AND :y >= cellY AND :y < cellY + spanY
        )
    """)
    suspend fun isPositionOccupied(x: Int, y: Int, screen: Int = 0): Boolean
    
    /**
     * Find first empty cell on screen
     */
    @Query("""
        SELECT * FROM home_items 
        WHERE screen = :screen 
        ORDER BY cellY * :columns + cellX 
        LIMIT 1
    """)
    suspend fun findFirstEmptyPosition(screen: Int, columns: Int): HomeItem?
    
    /**
     * Move item to new position
     */
    @Transaction
    suspend fun moveItem(itemId: Long, newX: Int, newY: Int) {
        val item = getItemById(itemId)
        item?.let {
            update(it.copy(cellX = newX, cellY = newY))
        }
    }
    
    /**
     * Swap positions of two items
     */
    @Transaction
    suspend fun swapItems(itemId1: Long, itemId2: Long) {
        val item1 = getItemById(itemId1)
        val item2 = getItemById(itemId2)
        
        if (item1 != null && item2 != null) {
            val tempX = item1.cellX
            val tempY = item1.cellY
            
            update(item1.copy(cellX = item2.cellX, cellY = item2.cellY))
            update(item2.copy(cellX = tempX, cellY = tempY))
        }
    }
}

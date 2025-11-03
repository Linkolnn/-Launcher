package com.customlauncher.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.customlauncher.app.data.model.HomeItemModel

/**
 * Room entity for home screen items
 */
@Entity(tableName = "home_items")
@TypeConverters(ItemTypeConverter::class)
data class HomeItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: HomeItemModel.ItemType,
    val packageName: String? = null,
    val componentName: String? = null,
    val label: String? = null,
    val widgetId: Int? = null,
    val cellX: Int,
    val cellY: Int,
    val spanX: Int = 1,
    val spanY: Int = 1,
    val screen: Int = 0,
    val position: Int = -1
) {
    /**
     * Convert to model for use in UI
     */
    fun toModel(): HomeItemModel {
        return HomeItemModel(
            id = id,
            type = type,
            packageName = packageName,
            componentName = componentName,
            label = label,
            widgetId = widgetId,
            cellX = cellX,
            cellY = cellY,
            spanX = spanX,
            spanY = spanY,
            screen = screen,
            position = position
        )
    }
    
    companion object {
        /**
         * Create entity from model
         */
        fun fromModel(model: HomeItemModel): HomeItem {
            return HomeItem(
                id = model.id,
                type = model.type,
                packageName = model.packageName,
                componentName = model.componentName,
                label = model.label,
                widgetId = model.widgetId,
                cellX = model.cellX,
                cellY = model.cellY,
                spanX = model.spanX,
                spanY = model.spanY,
                screen = model.screen,
                position = model.position
            )
        }
    }
}

/**
 * Type converter for ItemType enum
 */
class ItemTypeConverter {
    @TypeConverter
    fun fromItemType(type: HomeItemModel.ItemType): String {
        return type.name
    }
    
    @TypeConverter
    fun toItemType(type: String): HomeItemModel.ItemType {
        return HomeItemModel.ItemType.valueOf(type)
    }
}

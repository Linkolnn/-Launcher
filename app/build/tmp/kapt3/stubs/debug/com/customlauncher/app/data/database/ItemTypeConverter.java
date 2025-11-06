package com.customlauncher.app.data.database;

/**
 * Type converter for ItemType enum
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0007\u00a8\u0006\b"}, d2 = {"Lcom/customlauncher/app/data/database/ItemTypeConverter;", "", "()V", "fromItemType", "", "type", "Lcom/customlauncher/app/data/model/HomeItemModel$ItemType;", "toItemType", "app_debug"})
public final class ItemTypeConverter {
    
    public ItemTypeConverter() {
        super();
    }
    
    @androidx.room.TypeConverter
    @org.jetbrains.annotations.NotNull
    public final java.lang.String fromItemType(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel.ItemType type) {
        return null;
    }
    
    @androidx.room.TypeConverter
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.model.HomeItemModel.ItemType toItemType(@org.jetbrains.annotations.NotNull
    java.lang.String type) {
        return null;
    }
}
package com.customlauncher.app.data.database;

/**
 * Room entity for home screen items
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b(\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\b\u0018\u0000 :2\u00020\u0001:\u0001:B\u007f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\u0006\u0010\f\u001a\u00020\u000b\u0012\u0006\u0010\r\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\u0012J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\'\u001a\u00020\u000bH\u00c6\u0003J\t\u0010(\u001a\u00020\u000bH\u00c6\u0003J\t\u0010)\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u0010\u0010-\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010#J\t\u0010.\u001a\u00020\u000bH\u00c6\u0003J\t\u0010/\u001a\u00020\u000bH\u00c6\u0003J\t\u00100\u001a\u00020\u000bH\u00c6\u0003J\u008e\u0001\u00101\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000b2\b\b\u0002\u0010\u0011\u001a\u00020\u000bH\u00c6\u0001\u00a2\u0006\u0002\u00102J\u0013\u00103\u001a\u0002042\b\u00105\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00106\u001a\u00020\u000bH\u00d6\u0001J\u0006\u00107\u001a\u000208J\t\u00109\u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\r\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0017R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0017R\u0011\u0010\u0011\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u0010\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0014R\u0011\u0010\u000f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0015\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\n\n\u0002\u0010$\u001a\u0004\b\"\u0010#\u00a8\u0006;"}, d2 = {"Lcom/customlauncher/app/data/database/HomeItem;", "", "id", "", "type", "Lcom/customlauncher/app/data/model/HomeItemModel$ItemType;", "packageName", "", "componentName", "label", "widgetId", "", "cellX", "cellY", "spanX", "spanY", "screen", "position", "(JLcom/customlauncher/app/data/model/HomeItemModel$ItemType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;IIIIII)V", "getCellX", "()I", "getCellY", "getComponentName", "()Ljava/lang/String;", "getId", "()J", "getLabel", "getPackageName", "getPosition", "getScreen", "getSpanX", "getSpanY", "getType", "()Lcom/customlauncher/app/data/model/HomeItemModel$ItemType;", "getWidgetId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLcom/customlauncher/app/data/model/HomeItemModel$ItemType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;IIIIII)Lcom/customlauncher/app/data/database/HomeItem;", "equals", "", "other", "hashCode", "toModel", "Lcom/customlauncher/app/data/model/HomeItemModel;", "toString", "Companion", "app_debug"})
@androidx.room.Entity(tableName = "home_items")
@androidx.room.TypeConverters(value = {com.customlauncher.app.data.database.ItemTypeConverter.class})
public final class HomeItem {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.model.HomeItemModel.ItemType type = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String packageName = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String componentName = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String label = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer widgetId = null;
    private final int cellX = 0;
    private final int cellY = 0;
    private final int spanX = 0;
    private final int spanY = 0;
    private final int screen = 0;
    private final int position = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.data.database.HomeItem.Companion Companion = null;
    
    public HomeItem(long id, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel.ItemType type, @org.jetbrains.annotations.Nullable
    java.lang.String packageName, @org.jetbrains.annotations.Nullable
    java.lang.String componentName, @org.jetbrains.annotations.Nullable
    java.lang.String label, @org.jetbrains.annotations.Nullable
    java.lang.Integer widgetId, int cellX, int cellY, int spanX, int spanY, int screen, int position) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.model.HomeItemModel.ItemType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPackageName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getComponentName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getWidgetId() {
        return null;
    }
    
    public final int getCellX() {
        return 0;
    }
    
    public final int getCellY() {
        return 0;
    }
    
    public final int getSpanX() {
        return 0;
    }
    
    public final int getSpanY() {
        return 0;
    }
    
    public final int getScreen() {
        return 0;
    }
    
    public final int getPosition() {
        return 0;
    }
    
    /**
     * Convert to model for use in UI
     */
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.model.HomeItemModel toModel() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.model.HomeItemModel.ItemType component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component6() {
        return null;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.database.HomeItem copy(long id, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.HomeItemModel.ItemType type, @org.jetbrains.annotations.Nullable
    java.lang.String packageName, @org.jetbrains.annotations.Nullable
    java.lang.String componentName, @org.jetbrains.annotations.Nullable
    java.lang.String label, @org.jetbrains.annotations.Nullable
    java.lang.Integer widgetId, int cellX, int cellY, int spanX, int spanY, int screen, int position) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/customlauncher/app/data/database/HomeItem$Companion;", "", "()V", "fromModel", "Lcom/customlauncher/app/data/database/HomeItem;", "model", "Lcom/customlauncher/app/data/model/HomeItemModel;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Create entity from model
         */
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.data.database.HomeItem fromModel(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.model.HomeItemModel model) {
            return null;
        }
    }
}
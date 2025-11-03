package com.customlauncher.app.data.model;

/**
 * Configuration for home screen grid layout
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 !2\u00020\u0001:\u0001!B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J&\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0006H\u00c6\u0003J\'\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u001a\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u001a2\u0006\u0010\u001b\u001a\u00020\u0003J\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001J\u0016\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\f\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\t\u00a8\u0006\""}, d2 = {"Lcom/customlauncher/app/data/model/GridConfiguration;", "", "columns", "", "rows", "isButtonMode", "", "(IIZ)V", "getColumns", "()I", "()Z", "getRows", "totalCells", "getTotalCells", "canFitItem", "x", "y", "spanX", "spanY", "component1", "component2", "component3", "copy", "equals", "other", "getCellCoordinates", "Lkotlin/Pair;", "index", "getCellIndex", "hashCode", "isValidPosition", "toString", "", "Companion", "app_debug"})
public final class GridConfiguration {
    private final int columns = 0;
    private final int rows = 0;
    private final boolean isButtonMode = false;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.data.model.GridConfiguration.Companion Companion = null;
    
    public GridConfiguration(int columns, int rows, boolean isButtonMode) {
        super();
    }
    
    public final int getColumns() {
        return 0;
    }
    
    public final int getRows() {
        return 0;
    }
    
    public final boolean isButtonMode() {
        return false;
    }
    
    public final int getTotalCells() {
        return 0;
    }
    
    public final boolean isValidPosition(int x, int y) {
        return false;
    }
    
    public final int getCellIndex(int x, int y) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlin.Pair<java.lang.Integer, java.lang.Integer> getCellCoordinates(int index) {
        return null;
    }
    
    public final boolean canFitItem(int x, int y, int spanX, int spanY) {
        return false;
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.model.GridConfiguration copy(int columns, int rows, boolean isButtonMode) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0007\u001a\u00020\u00042\b\b\u0002\u0010\b\u001a\u00020\t\u00a8\u0006\n"}, d2 = {"Lcom/customlauncher/app/data/model/GridConfiguration$Companion;", "", "()V", "fromPreferences", "Lcom/customlauncher/app/data/model/GridConfiguration;", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "getDefault", "isButtonMode", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.data.model.GridConfiguration getDefault(boolean isButtonMode) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.data.model.GridConfiguration fromPreferences(@org.jetbrains.annotations.NotNull
        com.customlauncher.app.data.preferences.LauncherPreferences preferences) {
            return null;
        }
    }
}
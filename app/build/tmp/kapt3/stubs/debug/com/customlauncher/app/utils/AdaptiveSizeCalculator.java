package com.customlauncher.app.utils;

/**
 * Calculator for adaptive icon and text sizes based on grid configuration
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/customlauncher/app/utils/AdaptiveSizeCalculator;", "", "()V", "BASE_ICON_SIZE_DP", "", "BASE_PADDING_DP", "BASE_TEXT_SIZE_SP", "MAX_ICON_SIZE_DP", "MAX_PADDING_DP", "MAX_TEXT_SIZE_SP", "MIN_ICON_SIZE_DP", "MIN_PADDING_DP", "MIN_TEXT_SIZE_SP", "REFERENCE_COLUMNS", "REFERENCE_ROWS", "calculateIconSize", "context", "Landroid/content/Context;", "gridConfig", "Lcom/customlauncher/app/data/model/GridConfiguration;", "calculateMaxTextLines", "calculatePadding", "calculateTextSize", "", "shouldShowText", "", "app_debug"})
public final class AdaptiveSizeCalculator {
    private static final int BASE_ICON_SIZE_DP = 48;
    private static final int BASE_TEXT_SIZE_SP = 12;
    private static final int BASE_PADDING_DP = 8;
    private static final int MIN_ICON_SIZE_DP = 32;
    private static final int MAX_ICON_SIZE_DP = 72;
    private static final int MIN_TEXT_SIZE_SP = 10;
    private static final int MAX_TEXT_SIZE_SP = 14;
    private static final int MIN_PADDING_DP = 4;
    private static final int MAX_PADDING_DP = 12;
    private static final int REFERENCE_COLUMNS = 4;
    private static final int REFERENCE_ROWS = 5;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.utils.AdaptiveSizeCalculator INSTANCE = null;
    
    private AdaptiveSizeCalculator() {
        super();
    }
    
    /**
     * Calculate adaptive icon size based on grid configuration
     */
    public final int calculateIconSize(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig) {
        return 0;
    }
    
    /**
     * Calculate adaptive text size based on grid configuration
     */
    public final float calculateTextSize(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig) {
        return 0.0F;
    }
    
    /**
     * Calculate adaptive padding based on grid configuration
     */
    public final int calculatePadding(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig) {
        return 0;
    }
    
    /**
     * Calculate if text should be shown based on grid density
     */
    public final boolean shouldShowText(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig) {
        return false;
    }
    
    /**
     * Calculate max lines for text based on grid configuration
     */
    public final int calculateMaxTextLines(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.GridConfiguration gridConfig) {
        return 0;
    }
}
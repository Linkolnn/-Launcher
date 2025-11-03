package com.customlauncher.app.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.data.model.GridConfiguration
import kotlin.math.min

/**
 * Calculator for adaptive icon and text sizes based on grid configuration
 */
object AdaptiveSizeCalculator {
    
    // Base sizes in dp for a 4x5 grid (default)
    private const val BASE_ICON_SIZE_DP = 48
    private const val BASE_TEXT_SIZE_SP = 12
    private const val BASE_PADDING_DP = 8
    
    // Min/Max constraints
    private const val MIN_ICON_SIZE_DP = 32
    private const val MAX_ICON_SIZE_DP = 72
    private const val MIN_TEXT_SIZE_SP = 10
    private const val MAX_TEXT_SIZE_SP = 14
    private const val MIN_PADDING_DP = 4
    private const val MAX_PADDING_DP = 12
    
    // Reference grid for calculations
    private const val REFERENCE_COLUMNS = 4
    private const val REFERENCE_ROWS = 5
    
    /**
     * Calculate adaptive icon size based on grid configuration
     */
    fun calculateIconSize(context: Context, gridConfig: GridConfiguration): Int {
        val preferences = LauncherApplication.instance.preferences
        val displayMetrics = context.resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val screenHeightPx = displayMetrics.heightPixels
        
        // Calculate actual cell size in pixels
        val cellWidthPx = screenWidthPx / gridConfig.columns
        val cellHeightPx = (screenHeightPx * 0.75f / gridConfig.rows).toInt() // Account for status bar and navigation
        val cellSizePx = min(cellWidthPx, cellHeightPx)
        
        // Icon should be 50-70% of cell size depending on grid density
        val iconPercentage = when {
            gridConfig.columns >= 6 -> 0.7f  // More columns = larger percentage
            gridConfig.columns >= 5 -> 0.65f
            gridConfig.columns >= 4 -> 0.6f
            gridConfig.columns == 3 -> 0.65f  // 3 columns should have larger icons like home screen
            else -> 0.55f  // 2 or less columns
        }
        
        // Calculate icon size in pixels
        var iconSizePx = (cellSizePx * iconPercentage).toInt()
        
        // Apply custom scale if set
        if (preferences.iconScaleMode == "custom") {
            iconSizePx = (iconSizePx * preferences.customIconScale).toInt()
        }
        
        // Convert to dp for constraints check
        val iconSizeDp = iconSizePx / displayMetrics.density
        
        // Apply constraints in dp, then convert back to pixels
        val constrainedDp = iconSizeDp.coerceIn(MIN_ICON_SIZE_DP.toFloat(), MAX_ICON_SIZE_DP.toFloat())
        val finalIconSizePx = (constrainedDp * displayMetrics.density).toInt()
        
        android.util.Log.d("AdaptiveSize", "Grid: ${gridConfig.columns}x${gridConfig.rows}, " +
                "Cell: ${cellWidthPx}x${cellHeightPx}px, Icon: ${finalIconSizePx}px (${constrainedDp}dp)")
        
        return finalIconSizePx
    }
    
    /**
     * Calculate adaptive text size based on grid configuration
     */
    fun calculateTextSize(context: Context, gridConfig: GridConfiguration): Float {
        val displayMetrics = context.resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val screenHeightPx = displayMetrics.heightPixels
        
        // Calculate cell size
        val cellWidthPx = screenWidthPx / gridConfig.columns
        val cellHeightPx = (screenHeightPx * 0.75f / gridConfig.rows).toInt()
        val cellSizePx = min(cellWidthPx, cellHeightPx)
        
        // Base text size on cell size - roughly 10-15% of cell size
        val baseTextSizePx = cellSizePx * 0.12f
        
        // Convert to SP
        var textSizeSp = baseTextSizePx / displayMetrics.scaledDensity
        
        // Apply additional scaling for very dense grids
        if (gridConfig.columns >= 6) {
            textSizeSp *= 0.9f
        }
        
        // Apply constraints
        textSizeSp = textSizeSp.coerceIn(MIN_TEXT_SIZE_SP.toFloat(), MAX_TEXT_SIZE_SP.toFloat())
        
        android.util.Log.d("AdaptiveSize", "Text size: ${textSizeSp}sp for grid ${gridConfig.columns}x${gridConfig.rows}")
        
        return textSizeSp
    }
    
    /**
     * Calculate adaptive padding based on grid configuration
     */
    fun calculatePadding(context: Context, gridConfig: GridConfiguration): Int {
        val displayMetrics = context.resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val screenHeightPx = displayMetrics.heightPixels
        
        // Calculate cell size
        val cellWidthPx = screenWidthPx / gridConfig.columns
        val cellHeightPx = (screenHeightPx * 0.75f / gridConfig.rows).toInt()
        val cellSizePx = min(cellWidthPx, cellHeightPx)
        
        // Padding should be 5-10% of cell size
        val paddingPercentage = when {
            gridConfig.columns >= 6 -> 0.05f // Less padding for dense grids
            gridConfig.columns >= 5 -> 0.06f
            else -> 0.08f
        }
        
        var paddingPx = (cellSizePx * paddingPercentage).toInt()
        
        // Convert to dp for constraints check
        val paddingDp = paddingPx / displayMetrics.density
        
        // Apply constraints in dp, then convert back to pixels
        val constrainedDp = paddingDp.coerceIn(MIN_PADDING_DP.toFloat(), MAX_PADDING_DP.toFloat())
        val finalPaddingPx = (constrainedDp * displayMetrics.density).toInt()
        
        android.util.Log.d("AdaptiveSize", "Padding: ${finalPaddingPx}px (${constrainedDp}dp)")
        
        return finalPaddingPx
    }
    
    /**
     * Calculate if text should be shown based on grid density
     */
    fun shouldShowText(gridConfig: GridConfiguration): Boolean {
        // Hide text for very dense grids
        return gridConfig.columns <= 5 && gridConfig.rows <= 8
    }
    
    /**
     * Calculate max lines for text based on grid configuration
     */
    fun calculateMaxTextLines(gridConfig: GridConfiguration): Int {
        return when {
            gridConfig.columns >= 6 || gridConfig.rows >= 8 -> 1
            gridConfig.columns >= 5 || gridConfig.rows >= 7 -> 1
            else -> 2
        }
    }
}

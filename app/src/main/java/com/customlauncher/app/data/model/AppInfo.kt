package com.customlauncher.app.data.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable?,
    val isHidden: Boolean = false,
    val isSelected: Boolean = false
)

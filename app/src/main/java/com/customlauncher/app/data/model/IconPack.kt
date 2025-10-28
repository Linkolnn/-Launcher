package com.customlauncher.app.data.model

import android.graphics.drawable.Drawable

data class IconPack(
    val packageName: String,
    val name: String,
    val icon: Drawable,
    val isSelected: Boolean = false,
    val isSystemDefault: Boolean = false
)

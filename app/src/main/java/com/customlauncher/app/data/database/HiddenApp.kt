package com.customlauncher.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hidden_apps")
data class HiddenApp(
    @PrimaryKey
    val packageName: String
)

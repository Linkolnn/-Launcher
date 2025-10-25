package com.customlauncher.app.data.model

enum class KeyCombination(val id: Int, val nameResId: Int) {
    BOTH_VOLUME(0, com.customlauncher.app.R.string.combo_both_volume),
    POWER_HOLD(1, com.customlauncher.app.R.string.combo_power_hold),
    POWER_VOL_UP(2, com.customlauncher.app.R.string.combo_power_vol_up),
    POWER_VOL_DOWN(3, com.customlauncher.app.R.string.combo_power_vol_down),
    VOL_UP_LONG(4, com.customlauncher.app.R.string.combo_vol_up_long),
    VOL_DOWN_LONG(5, com.customlauncher.app.R.string.combo_vol_down_long);
    
    companion object {
        fun fromId(id: Int): KeyCombination {
            return values().find { it.id == id } ?: VOL_DOWN_LONG
        }
    }
}

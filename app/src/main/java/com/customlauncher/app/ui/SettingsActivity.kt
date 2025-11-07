package com.customlauncher.app.ui

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.R
import com.customlauncher.app.databinding.ActivitySettingsBinding
import com.customlauncher.app.service.SystemBlockAccessibilityService
import com.customlauncher.app.data.model.GridConfiguration
import com.customlauncher.app.ui.dialog.GridConfigDialog
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.ui.adapter.IconPackAdapter
import com.customlauncher.app.utils.IconPackManager
import com.customlauncher.app.data.model.IconPack
import com.customlauncher.app.manager.HomeScreenModeManager

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val preferences by lazy { LauncherApplication.instance.preferences }
    private lateinit var iconPackAdapter: IconPackAdapter
    private lateinit var iconPackManager: IconPackManager
    private lateinit var homeScreenModeManager: HomeScreenModeManager
    
    companion object {
        private const val REQUEST_CODE_WRITE_SETTINGS = 1002
        private const val REQUEST_CODE_ACCESSIBILITY = 1003
        private const val TAG = "SettingsActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize mode manager
        homeScreenModeManager = HomeScreenModeManager(this)
        
        setupTabs()
        setupCustomKeyCombination()
        setupHomeScreenSettings()
        setupGridSelection()
        setupGridTypeTabs()  // Setup grid type tabs
        setupButtonPhoneMode()  // Setup button phone mode
        setupFeatureSwitches()  // New method for feature toggles
        setupIconPacks()  // Setup icon pack selector
        setupHomeScreenSelector()
        setupPermissionsSection()
        setupDonateSection()  // Setup donate button
        setupAppVersion()
        
        // Check all permissions and auto-scroll if needed
        val allPermissionsGranted = checkAllPermissions()
        
        // Auto-scroll to permissions section if:
        // 1. Explicitly requested via intent
        // 2. Or if not all permissions are granted
        if (intent.getBooleanExtra("scroll_to_permissions", false) || !allPermissionsGranted) {
            binding.root.post {
                scrollToPermissions()
            }
        }
    }
    
    
    private fun setupTabs() {
        // Tab navigation
        binding.tabApplications.setOnClickListener {
            startActivity(Intent(this, AppListActivity::class.java))
            finish()
        }
        
        binding.tabSettings.setOnClickListener {
            // Already on settings tab
        }
    }
    
    private fun setupCustomKeyCombination() {
        val preferences = LauncherApplication.instance.preferences
        
        // Always use custom keys
        preferences.useCustomKeys = true
        
        // Load saved combination
        val customKeys = preferences.customKeyCombination
        if (customKeys != null) {
            displayCustomKeys(customKeys)
        } else {
            binding.customKeysText.text = "Комбинация не задана"
        }
        
        // Record button listener
        binding.recordKeysButton.setOnClickListener {
            showRecordKeysDialog()
        }
    }
    
    
    private fun displayCustomKeys(keysString: String) {
        val keys = keysString.split(",").mapNotNull { it.toIntOrNull() }
        if (keys.isNotEmpty()) {
            val keyNames = keys.map { getKeyName(it) }.joinToString(" → ")
            binding.customKeysText.text = "Комбинация: $keyNames"
        }
    }
    
    private fun getKeyName(keyCode: Int): String {
        return when (keyCode) {
            android.view.KeyEvent.KEYCODE_0 -> "0"
            android.view.KeyEvent.KEYCODE_1 -> "1"
            android.view.KeyEvent.KEYCODE_2 -> "2"
            android.view.KeyEvent.KEYCODE_3 -> "3"
            android.view.KeyEvent.KEYCODE_4 -> "4"
            android.view.KeyEvent.KEYCODE_5 -> "5"
            android.view.KeyEvent.KEYCODE_6 -> "6"
            android.view.KeyEvent.KEYCODE_7 -> "7"
            android.view.KeyEvent.KEYCODE_8 -> "8"
            android.view.KeyEvent.KEYCODE_9 -> "9"
            android.view.KeyEvent.KEYCODE_STAR -> "*"
            android.view.KeyEvent.KEYCODE_POUND -> "#"
            android.view.KeyEvent.KEYCODE_VOLUME_UP -> "Vol+"
            android.view.KeyEvent.KEYCODE_VOLUME_DOWN -> "Vol-"
            android.view.KeyEvent.KEYCODE_POWER -> "Power"
            android.view.KeyEvent.KEYCODE_MENU -> "Menu"
            android.view.KeyEvent.KEYCODE_BACK -> "Back"
            android.view.KeyEvent.KEYCODE_HOME -> "Home"
            android.view.KeyEvent.KEYCODE_CALL -> "Call"
            android.view.KeyEvent.KEYCODE_ENDCALL -> "EndCall"
            else -> "Key$keyCode"
        }
    }
    
    private fun showRecordKeysDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_record_keys, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        
        // Make dialog background transparent to show rounded corners
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        
        val recordedKeys = mutableListOf<Int>()
        val recordedKeysText = dialogView.findViewById<TextView>(R.id.recordedKeysText)
        val keyCountText = dialogView.findViewById<TextView>(R.id.keyCountText)
        val saveButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.saveButton)
        val clearButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.clearButton)
        val cancelButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.cancelButton)
        
        fun updateDisplay() {
            val keyNames = recordedKeys.map { getKeyName(it) }.joinToString(" → ")
            recordedKeysText.text = keyNames
            keyCountText.text = "${recordedKeys.size} клавиш"
            saveButton.isEnabled = recordedKeys.isNotEmpty()
        }
        
        // Intercept key events in dialog
        dialog.setOnKeyListener { _, keyCode, event ->
            if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                // Skip system keys
                if (keyCode != android.view.KeyEvent.KEYCODE_BACK && 
                    keyCode != android.view.KeyEvent.KEYCODE_HOME &&
                    keyCode != android.view.KeyEvent.KEYCODE_APP_SWITCH &&
                    keyCode != android.view.KeyEvent.KEYCODE_RECENT_APPS) {
                    
                    recordedKeys.add(keyCode)
                    updateDisplay()
                    return@setOnKeyListener true
                }
            }
            false
        }
        
        clearButton.setOnClickListener {
            recordedKeys.clear()
            updateDisplay()
        }
        
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        
        saveButton.setOnClickListener {
            if (recordedKeys.isNotEmpty()) {
                val keysString = recordedKeys.joinToString(",")
                LauncherApplication.instance.preferences.customKeyCombination = keysString
                displayCustomKeys(keysString)
                Toast.makeText(this, "Комбинация сохранена", Toast.LENGTH_SHORT).show()
                
                // Notify AccessibilityService to refresh its key listener
                val refreshIntent = Intent(this, SystemBlockAccessibilityService::class.java)
                refreshIntent.action = SystemBlockAccessibilityService.ACTION_REFRESH_KEYS
                startService(refreshIntent)
                
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Сначала запишите комбинацию", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialog.show()
    }
    
    private fun setupGridSelection() {
        val preferences = LauncherApplication.instance.preferences
        
        // Update radio button state
        updateTouchGridState()
        
        // Handle grid selection changes - set listener only once
        if (binding.gridColumnsGroup.tag != "listener_set") {
            binding.gridColumnsGroup.tag = "listener_set"
            binding.gridColumnsGroup.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == -1) return@setOnCheckedChangeListener // No selection
                
                val columns = when (checkedId) {
                    R.id.grid3Columns -> 3
                    R.id.grid4Columns -> 4
                    R.id.grid5Columns -> 5
                    else -> return@setOnCheckedChangeListener
                }
                
                preferences.gridColumnCount = columns
                preferences.hasTouchGridSelection = true
                preferences.hasButtonGridSelection = false  // Clear button selection flag
                
                // Clear button grid selection when selecting touch grid
                // Use post to avoid conflicts with current event processing
                binding.buttonPhoneGridGroup.post {
                    binding.buttonPhoneGridGroup.setOnCheckedChangeListener(null)
                    binding.buttonPhoneGridGroup.clearCheck()
                    // Re-setup the button phone mode to restore listener
                    setupButtonPhoneMode()
                }
                
                // Automatically disable button phone mode when selecting touch grid
                if (preferences.buttonPhoneMode) {
                    preferences.buttonPhoneMode = false
                    sendBroadcast(Intent("com.customlauncher.BUTTON_PHONE_MODE_CHANGED"))
                    Toast.makeText(this, "Переключено на сенсорный режим: $columns столбца", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Сетка изменена на $columns столбца", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun updateTouchGridState() {
        val preferences = LauncherApplication.instance.preferences
        Log.d(TAG, "updateTouchGridState - hasTouchGridSelection: ${preferences.hasTouchGridSelection}")
        Log.d(TAG, "updateTouchGridState - hasButtonGridSelection: ${preferences.hasButtonGridSelection}")
        Log.d(TAG, "updateTouchGridState - gridColumnCount: ${preferences.gridColumnCount}")
        
        // Only show selection if touch grid is actually being used
        if (preferences.hasButtonGridSelection && preferences.buttonPhoneGridSize.isNotEmpty()) {
            // Button grid is being used, clear touch grid selection
            binding.gridColumnsGroup.clearCheck()
            Log.d(TAG, "Clearing touch grid selection - button grid is active")
            return
        }
        
        // Get the effective column count (with default fallback)
        val effectiveColumns = if (preferences.hasTouchGridSelection) {
            preferences.gridColumnCount
        } else {
            // Default is 4 columns if no selection was made
            4
        }
        
        // Set the appropriate radio button as checked
        when (effectiveColumns) {
            3 -> {
                binding.grid3Columns.isChecked = true
                Log.d(TAG, "Setting 3 columns as checked")
            }
            4 -> {
                binding.grid4Columns.isChecked = true
                Log.d(TAG, "Setting 4 columns as checked (default)")
            }
            5 -> {
                binding.grid5Columns.isChecked = true
                Log.d(TAG, "Setting 5 columns as checked")
            }
            0 -> {
                // If value is 0, use default of 4 columns
                binding.grid4Columns.isChecked = true
                Log.d(TAG, "Setting 4 columns as checked (fallback from 0)")
            }
            else -> {
                // Unknown value, use default of 4 columns
                binding.grid4Columns.isChecked = true
                Log.d(TAG, "Setting 4 columns as checked (fallback from unknown)")
            }
        }
    }
    
    private fun setupHomeScreenSettings() {
        // Setup home screen grid tabs
        binding.homeTabTouchPhone.setOnClickListener {
            selectHomeScreenTouchTab()
        }
        
        binding.homeTabButtonPhone.setOnClickListener {
            Log.d(TAG, "homeTabButtonPhone onClick triggered")
            selectHomeScreenButtonTab()
        }
        
        // Select appropriate tab based on current mode
        when (homeScreenModeManager.getCurrentMode()) {
            HomeScreenModeManager.MODE_TOUCH -> selectHomeScreenTouchTab()
            HomeScreenModeManager.MODE_BUTTON -> {
                Log.d(TAG, "Initial mode is Button, selecting button tab")
                selectHomeScreenButtonTab()
            }
        }
        
        // Update grid size display
        updateHomeScreenGridDisplay()
        
        // Setup grid size click handlers
        binding.homeGridSizeRow.setOnClickListener {
            showGridConfigDialog(false)
        }
        
        binding.homeButtonGridSizeRow.setOnClickListener {
            showGridConfigDialog(true)
        }
        
        binding.menuAccessMethodRow.setOnClickListener {
            showMenuAccessMethodDialog()
        }
        
        // Update menu access method display
        updateMenuAccessMethodDisplay()
    }
    
    private fun selectHomeScreenTouchTab() {
        // Update UI
        binding.homeTabTouchPhone.setBackgroundResource(R.drawable.bg_tab_item_selected)
        binding.homeTabButtonPhone.setBackgroundResource(R.drawable.bg_tab_item)
        binding.homeTouchPhoneContent.visibility = View.VISIBLE
        binding.homeButtonPhoneContent.visibility = View.GONE
        
        // Set mode to Touch
        homeScreenModeManager.setMode(HomeScreenModeManager.MODE_TOUCH)
        
        // Also update buttonPhoneMode for compatibility
        preferences.buttonPhoneMode = false
        
        // Send broadcast about mode change
        sendBroadcast(Intent("com.customlauncher.BUTTON_PHONE_MODE_CHANGED"))
        
        Log.d(TAG, "Switched to Touch mode")
    }
    
    private fun selectHomeScreenButtonTab() {
        Log.d(TAG, "selectHomeScreenButtonTab clicked")
        try {
            // Update UI
            binding.homeTabTouchPhone.setBackgroundResource(R.drawable.bg_tab_item)
            binding.homeTabButtonPhone.setBackgroundResource(R.drawable.bg_tab_item_selected)
            binding.homeTouchPhoneContent.visibility = View.GONE
            binding.homeButtonPhoneContent.visibility = View.VISIBLE
            
            // Force layout update
            binding.homeButtonPhoneContent.post {
                binding.homeButtonPhoneContent.requestLayout()
                binding.homeButtonPhoneContent.invalidate()
                Log.d(TAG, "Button content visibility: ${binding.homeButtonPhoneContent.visibility}")
                Log.d(TAG, "Button content height: ${binding.homeButtonPhoneContent.height}")
            }
            
            // Set mode to Button
            homeScreenModeManager.setMode(HomeScreenModeManager.MODE_BUTTON)
            
            // Also update buttonPhoneMode for compatibility
            preferences.buttonPhoneMode = true
            
            // Send broadcast about mode change
            sendBroadcast(Intent("com.customlauncher.BUTTON_PHONE_MODE_CHANGED"))
            
            Log.d(TAG, "Switched to Button mode successfully")
            
            // Update grid display
            updateHomeScreenGridDisplay()
        } catch (e: Exception) {
            Log.e(TAG, "Error switching to Button mode", e)
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateHomeScreenGridDisplay() {
        // Update touch screen grid display
        val touchColumns = preferences.homeScreenGridColumns
        val touchRows = preferences.homeScreenGridRows
        binding.homeGridSizeValue.text = "${touchColumns}×${touchRows}"
        
        // Update button phone grid display
        val buttonColumns = preferences.homeScreenGridColumnsButton
        val buttonRows = preferences.homeScreenGridRowsButton
        binding.homeButtonGridSizeValue.text = "${buttonColumns}×${buttonRows}"
    }
    
    private fun updateMenuAccessMethodDisplay() {
        val method = preferences.menuAccessMethod
        val displayText = when (method) {
            "dpad_down" -> "Навигация вниз"
            "button" -> "Кнопка меню"
            "gesture" -> "Жест смахивания"
            else -> "Навигация вниз"
        }
        binding.menuAccessMethodValue.text = displayText
    }
    
    private fun showMenuAccessMethodDialog() {
        val methods = arrayOf(
            "Навигация вниз",
            "Кнопка меню",
            "Жест смахивания"
        )
        val values = arrayOf("dpad_down", "button", "gesture")
        val currentMethod = preferences.menuAccessMethod
        var selectedIndex = values.indexOf(currentMethod).coerceAtLeast(0)
        
        AlertDialog.Builder(this)
            .setTitle("Способ открытия меню")
            .setSingleChoiceItems(methods, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton("ОК") { _, _ ->
                preferences.menuAccessMethod = values[selectedIndex]
                updateMenuAccessMethodDisplay()
                
                // Send broadcast for menu method change
                val intent = Intent("com.customlauncher.MENU_METHOD_CHANGED")
                intent.putExtra("method", values[selectedIndex])
                sendBroadcast(intent)
                
                Toast.makeText(this, "Способ доступа к меню изменен", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    
    private fun showGridConfigDialog(isButtonMode: Boolean) {
        val currentConfig = if (isButtonMode) {
            GridConfiguration(
                columns = preferences.homeScreenGridColumnsButton,
                rows = preferences.homeScreenGridRowsButton,
                isButtonMode = true
            )
        } else {
            GridConfiguration(
                columns = preferences.homeScreenGridColumns,
                rows = preferences.homeScreenGridRows,
                isButtonMode = false
            )
        }
        
        val dialog = GridConfigDialog(
            context = this,
            currentConfig = currentConfig,
            isButtonMode = isButtonMode,
            onSave = { columns, rows ->
                // Save new grid configuration
                if (isButtonMode) {
                    preferences.homeScreenGridColumnsButton = columns
                    preferences.homeScreenGridRowsButton = rows
                } else {
                    preferences.homeScreenGridColumns = columns
                    preferences.homeScreenGridRows = rows
                }
                
                // Update display
                updateHomeScreenGridDisplay()
                
                Toast.makeText(this, "Настройки сетки сохранены: ${columns}×${rows}", Toast.LENGTH_SHORT).show()
                
                // Notify home screen about the change immediately
                // The broadcast will be received by HomeScreenActivity if it's running
                // Otherwise changes will be applied on next onResume
                sendBroadcast(Intent("com.customlauncher.GRID_CONFIG_CHANGED").apply {
                    putExtra("immediate_update", true)
                    putExtra("columns", columns)
                    putExtra("rows", rows)
                })
                
                // Mark that grid needs update for next HomeScreen visit
                preferences.gridNeedsUpdate = true
            }
        )
        
        dialog.show()
    }
    
    private fun setupGridTypeTabs() {
        val preferences = LauncherApplication.instance.preferences
        
        // Set initial tab selection based on which grid is actually being used
        if (preferences.hasButtonGridSelection && preferences.buttonPhoneGridSize.isNotEmpty()) {
            // Button grid is being used
            selectButtonPhoneTab()
        } else {
            // Touch grid is being used (or nothing selected, default to touch)
            selectTouchPhoneTab()
        }
        
        // Handle touch phone tab click
        binding.tabTouchPhone.setOnClickListener {
            selectTouchPhoneTab()
            preferences.appMenuSelectedTab = "touch"
        }
        
        // Handle button phone tab click  
        binding.tabButtonPhone.setOnClickListener {
            selectButtonPhoneTab()
            preferences.appMenuSelectedTab = "button"
        }
    }
    
    private fun selectTouchPhoneTab() {
        // Update tab backgrounds
        binding.tabTouchPhone.setBackgroundResource(R.drawable.bg_tab_item_selected)
        binding.tabButtonPhone.setBackgroundResource(R.drawable.bg_tab_item)
        
        // Update content visibility
        binding.touchPhoneGridContent.visibility = View.VISIBLE
        binding.buttonPhoneGridContent.visibility = View.GONE
        
        // Update radio button state without resetting listener
        updateTouchGridState()
    }
    
    private fun selectButtonPhoneTab() {
        // Update tab backgrounds
        binding.tabTouchPhone.setBackgroundResource(R.drawable.bg_tab_item)
        binding.tabButtonPhone.setBackgroundResource(R.drawable.bg_tab_item_selected)
        
        // Update content visibility
        binding.touchPhoneGridContent.visibility = View.GONE
        binding.buttonPhoneGridContent.visibility = View.VISIBLE
        
        // Update radio button state without resetting listener
        updateButtonGridState()
    }
    
    private fun setupButtonPhoneMode() {
        val preferences = LauncherApplication.instance.preferences
        
        // Update radio button state
        updateButtonGridState()
        
        // Handle button phone grid size selection - set listener only once
        if (binding.buttonPhoneGridGroup.tag != "listener_set") {
            binding.buttonPhoneGridGroup.tag = "listener_set"
            binding.buttonPhoneGridGroup.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == -1) return@setOnCheckedChangeListener // No selection
                
                val gridSize = when (checkedId) {
                    R.id.gridButton3x3 -> "3x3"
                    R.id.gridButton3x4 -> "3x4"
                    R.id.gridButton3x5 -> "3x5"
                    R.id.gridButton4x5 -> "4x5"
                    else -> return@setOnCheckedChangeListener
                }
                
                Log.d(TAG, "Button phone grid selected: $gridSize")
                
                preferences.buttonPhoneGridSize = gridSize
                preferences.hasButtonGridSelection = true
                preferences.hasTouchGridSelection = false  // Clear touch selection flag
                
                Log.d(TAG, "Saved button phone grid size: ${preferences.buttonPhoneGridSize}")
                Log.d(TAG, "hasButtonGridSelection: ${preferences.hasButtonGridSelection}")
                
                // Clear touch grid selection when selecting button grid
                // Use post to avoid conflicts with current event processing
                binding.gridColumnsGroup.post {
                    binding.gridColumnsGroup.setOnCheckedChangeListener(null)
                    binding.gridColumnsGroup.clearCheck()
                    // Re-setup the grid selection to restore listener
                    setupGridSelection()
                }
                
                // DON'T automatically enable button phone mode for HOME SCREEN
                // This is just for app menu grid configuration
                Toast.makeText(this, "Сетка меню для кнопочного режима: $gridSize", Toast.LENGTH_SHORT).show()
                
                // Send broadcast to MainActivity to update only the app menu grid
                Log.d(TAG, "Sending broadcast APP_MENU_BUTTON_GRID_CHANGED")
                sendBroadcast(Intent("com.customlauncher.APP_MENU_BUTTON_GRID_CHANGED"))
            }
        }
    }
    
    private fun updateButtonGridState() {
        val preferences = LauncherApplication.instance.preferences
        Log.d(TAG, "updateButtonGridState - hasButtonGridSelection: ${preferences.hasButtonGridSelection}")
        Log.d(TAG, "updateButtonGridState - buttonPhoneGridSize: ${preferences.buttonPhoneGridSize}")
        
        // Only set initial selection if user has made a selection
        if (preferences.hasButtonGridSelection) {
            when (preferences.buttonPhoneGridSize) {
                "3x3" -> {
                    binding.gridButton3x3.isChecked = true
                    Log.d(TAG, "Setting 3x3 as checked")
                }
                "3x4" -> {
                    binding.gridButton3x4.isChecked = true
                    Log.d(TAG, "Setting 3x4 as checked")
                }
                "3x5" -> {
                    binding.gridButton3x5.isChecked = true
                    Log.d(TAG, "Setting 3x5 as checked")
                }
                "4x5" -> {
                    binding.gridButton4x5.isChecked = true
                    Log.d(TAG, "Setting 4x5 as checked")
                }
                else -> {
                    binding.buttonPhoneGridGroup.clearCheck()
                    Log.d(TAG, "Clearing selection")
                }
            }
        } else {
            // Clear selection if no prior selection
            binding.buttonPhoneGridGroup.clearCheck()
        }
    }
    
    private fun setupFeatureSwitches() {
        // Load saved states
        binding.homeScreenSwitch.isChecked = preferences.showHomeScreen
        binding.closeAppsSwitch.isChecked = preferences.closeAppsOnHiddenMode
        binding.blockTouchSwitch.isChecked = preferences.blockTouchInHiddenMode
        binding.dndSwitch.isChecked = preferences.enableDndInHiddenMode
        binding.hideAppsSwitch.isChecked = preferences.hideAppsInHiddenMode
        binding.blockScreenshotsSwitch.isChecked = preferences.blockScreenshotsInHiddenMode
        binding.showAppLabelsSwitch.isChecked = preferences.showAppLabels
        binding.showAppSearchSwitch.isChecked = preferences.showAppSearch
        
        // Setup home screen switch
        binding.homeScreenSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.showHomeScreen = isChecked
            val message = if (isChecked) {
                "Главный экран включен"
            } else {
                "Главный экран выключен, будет показано только меню приложений"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Show home screen: $isChecked")
            
            // Send broadcast to notify about home screen visibility change
            sendBroadcast(Intent("com.customlauncher.HOME_SCREEN_VISIBILITY_CHANGED"))
        }
        
        // Setup close apps switch
        binding.closeAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.closeAppsOnHiddenMode = isChecked
            val message = if (isChecked) {
                "Закрытие приложений в скрытом режиме включено"
            } else {
                "Закрытие приложений в скрытом режиме выключено"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Close apps on hidden mode: $isChecked")
        }
        
        // Setup block touch switch
        binding.blockTouchSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.blockTouchInHiddenMode = isChecked
            val message = if (isChecked) {
                "Блокировка сенсора в скрытом режиме включена"
            } else {
                "Блокировка сенсора в скрытом режиме выключена"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Block touch in hidden mode: $isChecked")
        }
        
        // Setup DND switch
        binding.dndSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.enableDndInHiddenMode = isChecked
            val message = if (isChecked) {
                "Режим «Не беспокоить» в скрытом режиме включен"
            } else {
                "Режим «Не беспокоить» в скрытом режиме выключен"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "DND in hidden mode: $isChecked")
        }
        
        // Setup hide apps switch
        binding.hideAppsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.hideAppsInHiddenMode = isChecked
            val message = if (isChecked) {
                "Скрытие приложений в скрытом режиме включено"
            } else {
                "Скрытие приложений в скрытом режиме выключено"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Hide apps in hidden mode: $isChecked")
            
            // Send broadcast to MainActivity to update the app list
            sendBroadcast(Intent("com.customlauncher.HIDE_APPS_SETTING_CHANGED"))
        }
        
        // Setup block screenshots switch
        binding.blockScreenshotsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.blockScreenshotsInHiddenMode = isChecked
            val message = if (isChecked) {
                "Блокировка скриншотов в скрытом режиме включена"
            } else {
                "Блокировка скриншотов в скрытом режиме выключена"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Block screenshots in hidden mode: $isChecked")
        }
        
        // Setup show app labels switch
        binding.showAppLabelsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.showAppLabels = isChecked
            val message = if (isChecked) {
                "Подписи иконок включены"
            } else {
                "Подписи иконок выключены"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Show app labels: $isChecked")
            
            // Send broadcast to MainActivity to update the grid
            sendBroadcast(Intent("com.customlauncher.APP_LABELS_CHANGED"))
        }
        
        // Additional listener for home screen switch has been moved to above  
        // to avoid duplicate handling
        
        // Setup show app search switch
        binding.showAppSearchSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.showAppSearch = isChecked
            val message = if (isChecked) {
                "Поиск в меню приложений включен"
            } else {
                "Поиск в меню приложений выключен"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        
        // Setup check permissions switch
        binding.checkPermissionsSwitch.isChecked = preferences.checkPermissionsOnStartup
        binding.checkPermissionsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.checkPermissionsOnStartup = isChecked
            val message = if (isChecked) {
                "Проверка разрешений включена"
            } else {
                "Проверка разрешений отключена. Приложение будет работать с ограниченным функционалом"
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            Log.d(TAG, "Check permissions on startup: $isChecked")
            
            if (!isChecked) {
                // Show warning dialog when disabling permissions check
                android.app.AlertDialog.Builder(this)
                    .setTitle("Внимание")
                    .setMessage("Отключение проверки разрешений может привести к неполной работе функций:\n\n• Блокировка экрана может не работать\n• Скриншоты могут не блокироваться\n• Специальные возможности будут недоступны\n\nПродолжить?")
                    .setPositiveButton("Да") { _, _ ->
                        // User confirmed
                    }
                    .setNegativeButton("Отмена") { _, _ ->
                        // Revert the change
                        binding.checkPermissionsSwitch.isChecked = true
                        preferences.checkPermissionsOnStartup = true
                    }
                    .show()
            }
        }
    }
    
    private fun setupIconPacks() {
        iconPackManager = IconPackManager(this)
        
        // Setup RecyclerView
        binding.iconPacksRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        
        // Setup adapter
        iconPackAdapter = IconPackAdapter { iconPack ->
            val packageName = if (iconPack.isSystemDefault) null else iconPack.packageName
            preferences.iconPackPackageName = packageName
            
            val message = if (iconPack.isSystemDefault) {
                "Выбраны системные иконки"
            } else {
                "Выбран пак иконок: ${iconPack.name}"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Icon pack selected: $packageName")
            
            // Notify MainActivity to refresh icons
            sendBroadcast(Intent("com.customlauncher.ICON_PACK_CHANGED"))
        }
        
        binding.iconPacksRecyclerView.adapter = iconPackAdapter
        
        // Load available icon packs
        loadIconPacks()
    }
    
    private fun loadIconPacks() {
        Thread {
            val iconPacks = iconPackManager.getAvailableIconPacks()
            runOnUiThread {
                iconPackAdapter.setIconPacks(iconPacks)
                iconPackAdapter.setSelectedPack(preferences.iconPackPackageName)
            }
        }.start()
    }
    
    private fun setupHomeScreenSelector() {
        // Home screen selector
        binding.selectHomeScreenButton.setOnClickListener {
            showHomeScreenDialog()
        }
    }
    
    private fun showHomeScreenDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выбор рабочего стола")
            .setMessage("Для выбора рабочего стола по умолчанию нажмите кнопку Home и выберите нужный launcher")
            .setPositiveButton("Открыть настройки") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Не удалось открыть настройки", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    
    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                AlertDialog.Builder(this)
                    .setTitle("Требуется разрешение")
                    .setMessage("Для блокировки экрана необходимо разрешение на отображение поверх других приложений")
                    .setPositiveButton("Предоставить") { _, _ ->
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName")
                        )
                        startActivity(intent)
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            }
        }
    }
    
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    
    private fun setupPermissionsSection() {
        // Accessibility Service - always open settings
        binding.accessibilitySettingsButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
            // Schedule scroll to permissions section when we return
            scheduleScrollToPermissions()
        }
        
        // Overlay Permission - always open settings
        binding.overlayPermissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
                scheduleScrollToPermissions()
            } else {
                Toast.makeText(this, "Разрешение уже предоставлено", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Do Not Disturb Permission
        binding.dndPermissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
                scheduleScrollToPermissions()
            }
        }
        
        // Write Settings
        binding.writeSettingsButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
                scheduleScrollToPermissions()
            }
        }
        
        // Home Screen Default - always open settings
        binding.selectHomeScreenButton.setOnClickListener {
            try {
                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                startActivity(intent)
                scheduleScrollToPermissions()
            } catch (e: Exception) {
                // Fallback to app info if home settings not available
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
        
        // ADB Helper Button
        binding.adbHelperButton.setOnClickListener {
            val intent = Intent(this, AdbHelperActivity::class.java)
            startActivity(intent)
        }
        
        // Shizuku Guide Button
        binding.shizukuGuideButton.setOnClickListener {
            try {
                // Можно заменить на реальную ссылку на ваш репозиторий с гайдом
                val guideUrl = "https://github.com/Linkolnn/-Launcher/blob/main/SHIZUKU_GUIDE.md"
                // Альтернатива на русском:
                // val guideUrl = "https://telegra.ph/Shizuku-WiFi-Debug-Guide-RU"
                
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(guideUrl))
                startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "Не удалось открыть браузер", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun checkAllPermissions(): Boolean {
        var allPermissionsGranted = true
        
        // Check Accessibility Service
        val accessibilityEnabled = isAccessibilityServiceEnabled()
        if (accessibilityEnabled) {
            binding.accessibilityStatusIcon.setImageResource(R.drawable.ic_check)
            binding.accessibilityStatusText.text = "Включено"
            binding.accessibilityStatusText.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
        } else {
            binding.accessibilityStatusIcon.setImageResource(R.drawable.ic_close)
            binding.accessibilityStatusText.text = "Не включено"
            binding.accessibilityStatusText.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
            allPermissionsGranted = false
        }
        
        // Check Overlay Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                binding.overlayStatusIcon.setImageResource(R.drawable.ic_check)
                binding.overlayStatusText.text = "Разрешено"
                binding.overlayStatusText.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
            } else {
                binding.overlayStatusIcon.setImageResource(R.drawable.ic_close)
                binding.overlayStatusText.text = "Не разрешено"
                binding.overlayStatusText.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
                allPermissionsGranted = false
            }
        }
        
        // Check Do Not Disturb Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.isNotificationPolicyAccessGranted) {
                binding.dndStatusIcon.setImageResource(R.drawable.ic_check)
                binding.dndStatusText.text = "Разрешено"
                binding.dndStatusText.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
            } else {
                binding.dndStatusIcon.setImageResource(R.drawable.ic_close)
                binding.dndStatusText.text = "Не разрешено"
                binding.dndStatusText.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
                allPermissionsGranted = false
            }
        }
        
        // Check Write Settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                binding.writeSettingsStatusIcon.setImageResource(R.drawable.ic_check)
                binding.writeSettingsStatusText.text = "Разрешено"
                binding.writeSettingsStatusText.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
            } else {
                binding.writeSettingsStatusIcon.setImageResource(R.drawable.ic_close)
                binding.writeSettingsStatusText.text = "Не разрешено"
                binding.writeSettingsStatusText.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
                allPermissionsGranted = false
            }
        }
        
        // Check Home Screen Default
        if (isDefaultLauncher()) {
            binding.homeScreenStatusIcon.setImageResource(R.drawable.ic_check)
            binding.homeScreenStatusText.text = "Установлен"
            binding.homeScreenStatusText.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
        } else {
            binding.homeScreenStatusIcon.setImageResource(R.drawable.ic_close)
            binding.homeScreenStatusText.text = "Не установлен"
            binding.homeScreenStatusText.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
            allPermissionsGranted = false
        }
        
        return allPermissionsGranted
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val serviceName = "${packageName}/${com.customlauncher.app.service.SystemBlockAccessibilityService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return enabledServices?.contains(serviceName) == true
    }
    
    private fun isDefaultLauncher(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName == packageName
    }
    
    fun areAllPermissionsGranted(): Boolean {
        val accessibilityEnabled = isAccessibilityServiceEnabled()
        val overlayGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val dndGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || notificationManager.isNotificationPolicyAccessGranted
        val writeSettingsGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(this)
        val isDefaultHome = isDefaultLauncher()
        
        return accessibilityEnabled && overlayGranted && dndGranted && writeSettingsGranted && isDefaultHome
    }
    
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            REQUEST_CODE_WRITE_SETTINGS,
            REQUEST_CODE_ACCESSIBILITY -> {
                // Recheck permissions after returning from settings
                checkAllPermissions()
            }
        }
    }
    
    private fun setupDonateSection() {
        // Setup donate button click
        binding.donateButton.setOnClickListener {
            try {
                // SBP payment link
                val sbpUrl = "https://finance.ozon.ru/apps/sbp/ozonbankpay/019a5a84-36a3-7a07-aa7c-a26f247a82f7"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sbpUrl))
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to open donate link", e)
                Toast.makeText(this, "Не удалось открыть ссылку для доната", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupAppVersion() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName
            binding.appVersionText.text = "Версия $versionName"
        } catch (e: Exception) {
            binding.appVersionText.text = "Версия 3.0"
        }
    }
    
    override fun onResume() {
        super.onResume()
        val allPermissionsGranted = checkAllPermissions()
        // Reload icon packs in case new ones were installed
        loadIconPacks()
        
        // Check if we need to scroll to permissions section
        // This happens after returning from system settings
        if (shouldScrollToPermissions) {
            scrollToPermissions()
            shouldScrollToPermissions = false
        } else if (!allPermissionsGranted) {
            // If permissions are still missing after returning, keep focus on permissions section
            binding.settingsScrollView.post {
                scrollToPermissions()
            }
        }
    }
    
    private var shouldScrollToPermissions = false
    
    private fun scheduleScrollToPermissions() {
        shouldScrollToPermissions = true
    }
    
    private fun scrollToPermissions() {
        binding.settingsScrollView.post {
            // Get the position of the permissions section
            val permissionsTop = binding.permissionsSection.top
            // Smooth scroll to the permissions section
            binding.settingsScrollView.smoothScrollTo(0, permissionsTop)
        }
    }
}

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
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val preferences by lazy { LauncherApplication.instance.preferences }
    
    companion object {
        private const val REQUEST_CODE_WRITE_SETTINGS = 1002
        private const val REQUEST_CODE_ACCESSIBILITY = 1003
        private const val TAG = "SettingsActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupTabs()
        setupCustomKeyCombination()
        setupGridSelection()
        setupHomeScreenSelector()
        setupPermissionsSection()
        checkAllPermissions()
        setupAppVersion()
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
        
        // Set initial selection based on saved preference
        val currentColumns = preferences.gridColumnCount
        when (currentColumns) {
            3 -> binding.grid3Columns.isChecked = true
            4 -> binding.grid4Columns.isChecked = true
            5 -> binding.grid5Columns.isChecked = true
            else -> binding.grid4Columns.isChecked = true // Default to 4 columns
        }
        
        // Handle grid selection changes
        binding.gridColumnsGroup.setOnCheckedChangeListener { _, checkedId ->
            val columns = when (checkedId) {
                R.id.grid3Columns -> 3
                R.id.grid4Columns -> 4
                R.id.grid5Columns -> 5
                else -> 4 // Default to 4
            }
            
            preferences.gridColumnCount = columns
            Toast.makeText(this, "Сетка изменена на $columns столбца", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupHomeScreenSelector() {
        // Home screen selector
        binding.selectHomeScreenButton.setOnClickListener {
            showHomeScreenDialog()
        }
    }
    
    
    private fun updateUI() {
        updateStatusText()
    }
    
    private fun updateStatusText() {
        val isHidden = preferences.areAppsHidden()
        val isSensorActive = preferences.isSensorActive()
        
        val status = when {
            isHidden && isSensorActive -> "Приложения скрыты, сенсор активен"
            isHidden && !isSensorActive -> "Приложения скрыты, сенсор не активен"
            !isHidden && isSensorActive -> "Приложения видимы, сенсор активен"
            else -> "Приложения видимы, сенсор не активен"
        }
        
        binding.statusText.text = status
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
        }
        
        // Overlay Permission - always open settings
        binding.overlayPermissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            } else {
                Toast.makeText(this, "Разрешение уже предоставлено", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Do Not Disturb Permission
        binding.dndPermissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }
        
        // Write Settings
        binding.writeSettingsButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
        
        // Home Screen Default - always open settings
        binding.selectHomeScreenButton.setOnClickListener {
            try {
                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                startActivity(intent)
            } catch (e: Exception) {
                // Fallback to app info if home settings not available
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }
    
    private fun checkAllPermissions() {
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
        }
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
        updateUI()
        checkAllPermissions()
    }
}

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.R
import com.customlauncher.app.data.model.KeyCombination
import com.customlauncher.app.databinding.ActivitySettingsBinding
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
        setupHomeScreenSelector()
        setupPermissionsSection()
        checkAllPermissions()
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
    
    private fun setupKeyCombinationSelection() {
        // Key combination radio buttons
        binding.keyCombinationGroup.setOnCheckedChangeListener { _, checkedId ->
            // Ignore if no button is checked (happens during initialization)
            if (checkedId == -1) return@setOnCheckedChangeListener
            
            val combination = when (checkedId) {
                R.id.comboBothVolume -> KeyCombination.BOTH_VOLUME
                R.id.comboPowerHold -> KeyCombination.POWER_HOLD
                R.id.comboPowerVolUp -> KeyCombination.POWER_VOL_UP
                R.id.comboPowerVolDown -> KeyCombination.POWER_VOL_DOWN
                R.id.comboVolUpLong -> KeyCombination.VOL_UP_LONG
                R.id.comboVolDownLong -> KeyCombination.VOL_DOWN_LONG
                else -> return@setOnCheckedChangeListener // Don't save if unknown
            }
            
            // Only save if actually changed
            if (preferences.keyCombination != combination) {
                preferences.keyCombination = combination
                Toast.makeText(this, "Комбинация клавиш сохранена", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupHomeScreenSelector() {
        // Home screen selector
        binding.selectHomeScreenButton.setOnClickListener {
            showHomeScreenDialog()
        }
    }
    
    private fun updateUI() {
        // Temporarily remove listener to avoid triggering save
        binding.keyCombinationGroup.setOnCheckedChangeListener(null)
        
        // Set selected radio button based on saved combination
        val radioButtonId = when (preferences.keyCombination) {
            KeyCombination.BOTH_VOLUME -> R.id.comboBothVolume
            KeyCombination.POWER_HOLD -> R.id.comboPowerHold
            KeyCombination.POWER_VOL_UP -> R.id.comboPowerVolUp
            KeyCombination.POWER_VOL_DOWN -> R.id.comboPowerVolDown
            KeyCombination.VOL_UP_LONG -> R.id.comboVolUpLong
            KeyCombination.VOL_DOWN_LONG -> R.id.comboVolDownLong
        }
        binding.keyCombinationGroup.check(radioButtonId)
        
        // Re-attach listener after setting value
        setupKeyCombinationSelection()
        
        // Update status
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
    
    override fun onResume() {
        super.onResume()
        updateUI()
        checkAllPermissions()
    }
}

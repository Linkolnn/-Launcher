package com.customlauncher.app.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val preferences by lazy { LauncherApplication.instance.preferences }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupTabs()
        setupKeyCombinationSelection()
        setupHomeScreenSelector()
        setupOverlayPermission()
        setupDndPermission()
        updateStatusText()
        updateOverlayStatus()
        updateDndStatus()
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
            val combination = when (checkedId) {
                R.id.comboBothVolume -> KeyCombination.BOTH_VOLUME
                R.id.comboPowerHold -> KeyCombination.POWER_HOLD
                R.id.comboPowerVolUp -> KeyCombination.POWER_VOL_UP
                R.id.comboPowerVolDown -> KeyCombination.POWER_VOL_DOWN
                R.id.comboVolUpLong -> KeyCombination.VOL_UP_LONG
                R.id.comboVolDownLong -> KeyCombination.VOL_DOWN_LONG
                else -> KeyCombination.VOL_DOWN_LONG
            }
            preferences.keyCombination = combination
            Toast.makeText(this, "Комбинация клавиш сохранена", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupHomeScreenSelector() {
        // Home screen selector
        binding.selectHomeScreenButton.setOnClickListener {
            showHomeScreenDialog()
        }
    }
    
    private fun updateUI() {
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
    
    private fun setupOverlayPermission() {
        binding.overlayPermissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = android.net.Uri.parse("package:$packageName")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Разрешение не требуется на этой версии Android", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateOverlayStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission = Settings.canDrawOverlays(this)
            binding.overlayStatusText.text = if (hasPermission) {
                "Статус: Разрешено"
            } else {
                "Статус: Не настроено"
            }
            binding.overlayStatusText.setTextColor(
                if (hasPermission) getColor(R.color.accent_yellow) else getColor(R.color.text_gray)
            )
        } else {
            binding.overlayStatusText.text = "Не требуется"
            binding.overlayStatusText.setTextColor(getColor(R.color.text_gray))
        }
    }
    
    private fun setupDndPermission() {
        binding.dndPermissionButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Режим не беспокоить не поддерживается на этой версии Android", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateDndStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            val isEnabled = notificationManager.isNotificationPolicyAccessGranted
            binding.dndStatusText.text = if (isEnabled) {
                "Статус: Разрешено"
            } else {
                "Статус: Не настроено"
            }
            binding.dndStatusText.setTextColor(
                if (isEnabled) getColor(R.color.accent_yellow) else getColor(R.color.text_gray)
            )
        } else {
            binding.dndStatusText.text = "Не поддерживается"
            binding.dndStatusText.setTextColor(getColor(R.color.text_gray))
        }
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
    
    override fun onResume() {
        super.onResume()
        updateUI()
        updateOverlayStatus()
        updateDndStatus()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

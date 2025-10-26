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
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import com.customlauncher.app.receiver.LauncherDeviceAdminReceiver
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.DataOutputStream

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val preferences by lazy { LauncherApplication.instance.preferences }
    
    companion object {
        private const val REQUEST_CODE_DEVICE_ADMIN = 1001
        private const val REQUEST_CODE_WRITE_SETTINGS = 1002
        private const val REQUEST_CODE_ACCESSIBILITY = 1003
        private const val TAG = "SettingsActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupTabs()
        setupKeyCombinationSelection()
        setupHomeScreenSelector()
        setupOverlayPermission()
        setupDndPermission()
        setupPermissionsSection()
        updateStatusText()
        updateOverlayStatus()
        updateDndStatus()
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
    
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    
    private fun setupPermissionsSection() {
        // Accessibility Service
        binding.accessibilitySettingsButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivityForResult(intent, REQUEST_CODE_ACCESSIBILITY)
        }
        
        // Device Admin
        binding.deviceAdminSettingsButton.setOnClickListener {
            val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val componentName = ComponentName(this, LauncherDeviceAdminReceiver::class.java)
            
            if (!devicePolicyManager.isAdminActive(componentName)) {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, 
                    "Требуется для полной блокировки сенсора в скрытом режиме")
                startActivityForResult(intent, REQUEST_CODE_DEVICE_ADMIN)
            } else {
                Toast.makeText(this, "Администратор устройства уже активирован", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Write Settings
        binding.writeSettingsButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS)
                } else {
                    Toast.makeText(this, "Разрешение уже предоставлено", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        // Root Check
        binding.rootCheckButton.setOnClickListener {
            checkRootAccess()
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
        
        // Check Device Admin
        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(this, LauncherDeviceAdminReceiver::class.java)
        if (devicePolicyManager.isAdminActive(componentName)) {
            binding.deviceAdminStatusIcon.setImageResource(R.drawable.ic_check)
            binding.deviceAdminStatusText.text = "Активирован"
            binding.deviceAdminStatusText.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
        } else {
            binding.deviceAdminStatusIcon.setImageResource(R.drawable.ic_close)
            binding.deviceAdminStatusText.text = "Не активирован"
            binding.deviceAdminStatusText.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
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
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val serviceName = "${packageName}/${com.customlauncher.app.service.SystemBlockAccessibilityService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return enabledServices?.contains(serviceName) == true
    }
    
    private fun checkRootAccess() {
        binding.rootCheckButton.isEnabled = false
        binding.rootStatusText.text = "Проверка..."
        
        Thread {
            val hasRoot = checkRoot()
            runOnUiThread {
                binding.rootCheckButton.isEnabled = true
                if (hasRoot) {
                    binding.rootStatusIcon.setImageResource(R.drawable.ic_check)
                    binding.rootStatusText.text = "Доступен"
                    binding.rootStatusText.setTextColor(ContextCompat.getColor(this, R.color.accent_green))
                    requestRootPermission()
                } else {
                    binding.rootStatusIcon.setImageResource(R.drawable.ic_close)
                    binding.rootStatusText.text = "Не доступен"
                    binding.rootStatusText.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
                    Toast.makeText(this, "Root доступ не обнаружен на устройстве", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
    
    private fun checkRoot(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su -c echo test")
            val exitCode = process.waitFor()
            exitCode == 0
        } catch (e: Exception) {
            Log.e(TAG, "Root check failed", e)
            false
        }
    }
    
    private fun requestRootPermission() {
        Thread {
            try {
                // Request root permission for the app
                val process = Runtime.getRuntime().exec("su")
                val os = DataOutputStream(process.outputStream)
                os.writeBytes("echo Root permission granted\n")
                os.writeBytes("exit\n")
                os.flush()
                os.close()
                
                val exitCode = process.waitFor()
                runOnUiThread {
                    if (exitCode == 0) {
                        Toast.makeText(this, "Root доступ предоставлен", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to request root", e)
            }
        }.start()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            REQUEST_CODE_DEVICE_ADMIN,
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
        updateOverlayStatus()
        updateDndStatus()
    }
}

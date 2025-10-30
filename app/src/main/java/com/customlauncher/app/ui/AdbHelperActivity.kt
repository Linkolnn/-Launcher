package com.customlauncher.app.ui

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.customlauncher.app.databinding.ActivityAdbHelperBinding
import com.customlauncher.app.service.SystemBlockAccessibilityService
import com.customlauncher.app.utils.ShizukuHelper
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class AdbHelperActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdbHelperBinding
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var shizukuHelper: ShizukuHelper
    private val shizukuPermissionListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode == 0) {
            val granted = grantResult == PackageManager.PERMISSION_GRANTED
            if (granted) {
                checkShizukuStatus()
                Toast.makeText(this, "Доступ к Shizuku разрешен!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Доступ к Shizuku отклонен", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdbHelperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        shizukuHelper = ShizukuHelper(this)
        
        setupButtons()
        setupShizukuButtons()
        checkPermissionStatus()
        checkShizukuStatus()
        
        // Setup Shizuku listener
        Shizuku.addRequestPermissionResultListener(shizukuPermissionListener)
    }
    
    override fun onResume() {
        super.onResume()
        checkPermissionStatus()
        checkShizukuStatus()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(shizukuPermissionListener)
    }
    
    private fun setupButtons() {
        // Back button
        binding.backButton.setOnClickListener {
            finish()
        }
        
        // Open Shizuku guide on GitHub
        binding.openShizukuGuideButton.setOnClickListener {
            try {
                // Можно заменить на реальную ссылку на ваш репозиторий
                val guideUrl = "https://github.com/RikkaApps/Shizuku/blob/master/README.md"
                // Альтернативная ссылка на русскоязычный гайд:
                // val guideUrl = "https://telegra.ph/Shizuku-Guide-RU-12-15"
                
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(guideUrl))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Не удалось открыть браузер", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Open developer settings
        binding.openDeveloperSettingsButton.setOnClickListener {
            try {
                startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
            } catch (e: Exception) {
                Toast.makeText(this, "Сначала включите режим разработчика", Toast.LENGTH_LONG).show()
            }
        }
        
        // Copy ADB download link
        binding.copyAdbLinkButton.setOnClickListener {
            copyToClipboard(
                "ADB Download",
                "https://developer.android.com/studio/releases/platform-tools"
            )
        }
        
        // Copy accessibility commands
        binding.copyAccessibilityButton.setOnClickListener {
            val commands = """
                adb shell settings put secure enabled_accessibility_services com.customlauncher.app/com.customlauncher.app.service.SystemBlockAccessibilityService
                adb shell settings put secure accessibility_enabled 1
            """.trimIndent()
            copyToClipboard("Accessibility Commands", commands)
        }
        
        // Copy overlay command
        binding.copyOverlayButton.setOnClickListener {
            val command = "adb shell appops set com.customlauncher.app SYSTEM_ALERT_WINDOW allow"
            copyToClipboard("Overlay Command", command)
        }
        
        // Copy all commands
        binding.copyAllCommandsButton.setOnClickListener {
            val allCommands = """
                # Активация Специальных возможностей
                adb shell settings put secure enabled_accessibility_services com.customlauncher.app/com.customlauncher.app.service.SystemBlockAccessibilityService
                adb shell settings put secure accessibility_enabled 1
                
                # Разрешение Поверх других окон
                adb shell appops set com.customlauncher.app SYSTEM_ALERT_WINDOW allow
            """.trimIndent()
            copyToClipboard("All ADB Commands", allCommands)
        }
        
        // Check status button
        binding.checkStatusButton.setOnClickListener {
            checkPermissionStatus()
            Toast.makeText(this, "Статус обновлен", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun copyToClipboard(label: String, text: String) {
        val clip = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(this, "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show()
    }
    
    private fun checkPermissionStatus() {
        // Check accessibility service
        val isAccessibilityEnabled = isAccessibilityServiceEnabled()
        binding.accessibilityStatus.text = if (isAccessibilityEnabled) {
            "✅ Активно"
        } else {
            "❌ Не активно"
        }
        binding.accessibilityStatus.setTextColor(
            if (isAccessibilityEnabled) {
                0xFF4CAF50.toInt() // Green
            } else {
                0xFFFF5252.toInt() // Red
            }
        )
        
        // Check overlay permission
        val hasOverlayPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
        binding.overlayStatus.text = if (hasOverlayPermission) {
            "✅ Разрешено"
        } else {
            "❌ Не разрешено"
        }
        binding.overlayStatus.setTextColor(
            if (hasOverlayPermission) {
                0xFF4CAF50.toInt() // Green
            } else {
                0xFFFF5252.toInt() // Red
            }
        )
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        
        for (service in enabledServices) {
            if (service.resolveInfo.serviceInfo.name == SystemBlockAccessibilityService::class.java.name) {
                return true
            }
        }
        return false
    }
    
    private fun setupShizukuButtons() {
        // Install Shizuku button
        binding.installShizukuButton.setOnClickListener {
            try {
                // Open Google Play Store directly
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=moe.shizuku.privileged.api"))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                // Fallback to web if Play Store is not available
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api"))
                startActivity(intent)
            }
        }
        
        // Start Shizuku button
        binding.startShizukuButton.setOnClickListener {
            try {
                val intent = packageManager.getLaunchIntentForPackage("moe.shizuku.privileged.api")
                if (intent != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Shizuku не установлен", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Не удалось открыть Shizuku", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Request permission button
        binding.requestShizukuPermissionButton.setOnClickListener {
            shizukuHelper.requestShizukuPermission()
        }
        
        // Activate with Shizuku button
        binding.activateWithShizukuButton.setOnClickListener {
            lifecycleScope.launch {
                binding.activateWithShizukuButton.isEnabled = false
                binding.activateWithShizukuButton.text = "Активация..."
                
                shizukuHelper.enableAllPermissions(object : ShizukuHelper.ShizukuCallback {
                    override fun onSuccess(message: String) {
                        runOnUiThread {
                            Toast.makeText(this@AdbHelperActivity, message, Toast.LENGTH_LONG).show()
                            checkPermissionStatus()
                            binding.activateWithShizukuButton.isEnabled = true
                            binding.activateWithShizukuButton.text = "✨ АКТИВИРОВАТЬ РАЗРЕШЕНИЯ"
                        }
                    }
                    
                    override fun onError(error: String) {
                        runOnUiThread {
                            Toast.makeText(this@AdbHelperActivity, error, Toast.LENGTH_LONG).show()
                            binding.activateWithShizukuButton.isEnabled = true
                            binding.activateWithShizukuButton.text = "✨ АКТИВИРОВАТЬ РАЗРЕШЕНИЯ"
                        }
                    }
                })
            }
        }
    }
    
    private fun checkShizukuStatus() {
        val isInstalled = shizukuHelper.isShizukuInstalled()
        val isRunning = shizukuHelper.isShizukuRunning()
        val hasPermission = shizukuHelper.hasShizukuPermission()
        
        // Update status text
        val statusText = when {
            !isInstalled -> "❌ Shizuku не установлен"
            !isRunning -> "⚠️ Shizuku установлен, но не запущен"
            !hasPermission -> "⚠️ Shizuku запущен, требуется разрешение"
            else -> "✅ Shizuku готов к использованию!"
        }
        
        binding.shizukuStatusText.text = "Статус: $statusText"
        binding.shizukuStatusText.setTextColor(
            when {
                !isInstalled -> 0xFFFF5252.toInt() // Red
                !isRunning || !hasPermission -> 0xFFFFA726.toInt() // Orange
                else -> 0xFF4CAF50.toInt() // Green
            }
        )
        
        // Show/hide buttons based on status
        binding.installShizukuButton.visibility = if (!isInstalled) View.VISIBLE else View.GONE
        binding.startShizukuButton.visibility = if (isInstalled && !isRunning) View.VISIBLE else View.GONE
        binding.requestShizukuPermissionButton.visibility = if (isRunning && !hasPermission) View.VISIBLE else View.GONE
        binding.activateWithShizukuButton.visibility = if (isRunning && hasPermission) View.VISIBLE else View.GONE
    }
}

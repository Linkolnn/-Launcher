package com.customlauncher.app.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.data.model.KeyCombination
import com.customlauncher.app.databinding.ActivityMainBinding
import com.customlauncher.app.service.TouchBlockService
import com.customlauncher.app.ui.adapter.AppGridAdapter
import com.customlauncher.app.ui.viewmodel.AppViewModel
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var appAdapter: AppGridAdapter
    private val preferences by lazy { LauncherApplication.instance.preferences }
    
    // For key combination detection
    private var volumeUpPressed = false
    private var volumeDownPressed = false
    private var powerPressed = false
    private val keyPressHandler = Handler(Looper.getMainLooper())
    private var longPressRunnable: Runnable? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // MainActivity is now only for HOME screen
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Make system bars transparent
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        )
        
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        
        setupRecyclerView()
        setupListeners()
        observeViewModel()
        
        // Check overlay permission for touch blocking
        checkOverlayPermission()
        
        // Check initial state
        updateVisibility()
    }
    
    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Требуется разрешение наложения для блокировки сенсора", Toast.LENGTH_LONG).show()
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = android.net.Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }
    
    private fun setupRecyclerView() {
        appAdapter = AppGridAdapter { app ->
            viewModel.launchApp(app.packageName)
        }
        
        binding.appsGrid.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 4)
            adapter = appAdapter
            
            // Set custom fade sizes to match padding
            val topFadeSize = (50 * resources.displayMetrics.density).toInt()
            val bottomFadeSize = (40 * resources.displayMetrics.density).toInt()
            setFadeSizes(topFadeSize, bottomFadeSize)
            
            // Disable overscroll effect
            overScrollMode = View.OVER_SCROLL_NEVER
        }
    }
    
    private fun setupListeners() {
        // Long press on home screen opens app list
        binding.appsGrid.setOnLongClickListener {
            startActivity(Intent(this, AppListActivity::class.java))
            true
        }
    }
    
    private fun observeViewModel() {
        viewModel.allApps.observe(this) { apps ->
            // Delay UI update slightly for weak devices
            binding.appsGrid.postDelayed({
                // Show all apps or only visible based on hidden state
                val hidden = preferences.appsHidden
                if (hidden) {
                    // Filter out hidden apps
                    val visibleApps = apps.filter { !it.isHidden }
                    appAdapter.submitList(visibleApps)
                } else {
                    // Show all apps
                    appAdapter.submitList(apps)
                }
            }, 50) // Small delay for Android 11 optimization
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadApps()
        
        // Restore touch blocking state if apps are hidden
        if (preferences.appsHidden && preferences.touchScreenBlocked) {
            val intent = Intent(this, TouchBlockService::class.java)
            intent.action = TouchBlockService.ACTION_BLOCK_TOUCH
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            Log.d("MainActivity", "Restored touch blocking on resume")
        }
        
        updateVisibility()
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("MainActivity", "Key down: $keyCode")
        
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                volumeUpPressed = true
                Log.d("MainActivity", "Volume UP pressed")
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                volumeDownPressed = true
                Log.d("MainActivity", "Volume DOWN pressed")
            }
            KeyEvent.KEYCODE_POWER -> {
                powerPressed = true
                Log.d("MainActivity", "Power pressed")
            }
        }
        
        val combo = preferences.keyCombination
        Log.d("MainActivity", "Current combo setting: $combo")
        
        // Check for simultaneous button presses
        when (combo) {
            KeyCombination.BOTH_VOLUME -> {
                if (volumeUpPressed && volumeDownPressed) {
                    Log.d("MainActivity", "Both volume buttons detected!")
                    toggleAppsVisibility()
                    return true
                }
            }
            KeyCombination.POWER_VOL_UP -> {
                if (powerPressed && volumeUpPressed) {
                    Log.d("MainActivity", "Power + Vol Up detected!")
                    toggleAppsVisibility()
                    return true
                }
            }
            KeyCombination.POWER_VOL_DOWN -> {
                if (powerPressed && volumeDownPressed) {
                    Log.d("MainActivity", "Power + Vol Down detected!")
                    toggleAppsVisibility()
                    return true
                }
            }
            KeyCombination.VOL_UP_LONG -> {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && event?.repeatCount == 0) {
                    Log.d("MainActivity", "Starting Vol Up long press timer")
                    longPressRunnable?.let { keyPressHandler.removeCallbacks(it) }
                    longPressRunnable = Runnable {
                        Log.d("MainActivity", "Vol Up long press triggered!")
                        toggleAppsVisibility()
                    }
                    keyPressHandler.postDelayed(longPressRunnable!!, 1000)
                    return true
                }
            }
            KeyCombination.VOL_DOWN_LONG -> {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && event?.repeatCount == 0) {
                    Log.d("MainActivity", "Starting Vol Down long press timer")
                    longPressRunnable?.let { keyPressHandler.removeCallbacks(it) }
                    longPressRunnable = Runnable {
                        Log.d("MainActivity", "Vol Down long press triggered!")
                        toggleAppsVisibility()
                    }
                    keyPressHandler.postDelayed(longPressRunnable!!, 1000)
                    return true
                }
            }
            KeyCombination.POWER_HOLD -> {
                if (keyCode == KeyEvent.KEYCODE_POWER && event?.repeatCount == 0) {
                    Log.d("MainActivity", "Starting Power long press timer")
                    longPressRunnable?.let { keyPressHandler.removeCallbacks(it) }
                    longPressRunnable = Runnable {
                        Log.d("MainActivity", "Power long press triggered!")
                        toggleAppsVisibility()
                    }
                    keyPressHandler.postDelayed(longPressRunnable!!, 1000)
                    return true
                }
            }
        }
        
        return super.onKeyDown(keyCode, event)
    }
    
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> volumeUpPressed = false
            KeyEvent.KEYCODE_VOLUME_DOWN -> volumeDownPressed = false
            KeyEvent.KEYCODE_POWER -> powerPressed = false
        }
        
        // Cancel long press if key is released
        longPressRunnable?.let {
            keyPressHandler.removeCallbacks(it)
            longPressRunnable = null
        }
        
        return super.onKeyUp(keyCode, event)
    }
    
    private fun toggleAppsVisibility() {
        val isHidden = preferences.appsHidden
        preferences.appsHidden = !isHidden
        updateVisibility()
        
        // Toggle Do Not Disturb mode
        toggleDoNotDisturb(!isHidden)
        
        val message = if (!isHidden) {
            "Приложения скрыты, режим не беспокоить включен"
        } else {
            "Приложения показаны, режим не беспокоить выключен"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun toggleDoNotDisturb(enable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Check if we have permission
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                Toast.makeText(this, "Требуется разрешение для управления режимом не беспокоить", Toast.LENGTH_LONG).show()
                return
            }
            
            try {
                if (enable) {
                    // Enable Do Not Disturb (Total silence mode)
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                    Log.d("MainActivity", "DND enabled")
                } else {
                    // Disable Do Not Disturb (Allow all)
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                    Log.d("MainActivity", "DND disabled")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error toggling DND", e)
                Toast.makeText(this, "Ошибка управления режимом не беспокоить", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateVisibility() {
        val hidden = preferences.appsHidden
        
        // Update touch screen blocked state
        preferences.touchScreenBlocked = hidden
        
        if (hidden) {
            // Start touch block service as foreground service
            val intent = Intent(this, TouchBlockService::class.java)
            intent.action = TouchBlockService.ACTION_BLOCK_TOUCH
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            Log.d("MainActivity", "Touch blocking enabled")
        } else {
            // Stop touch block service
            val intent = Intent(this, TouchBlockService::class.java)
            intent.action = TouchBlockService.ACTION_UNBLOCK_TOUCH
            startService(intent)
            Log.d("MainActivity", "Touch blocking disabled")
        }
        
        // Reload apps to update visibility
        viewModel.loadApps()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        longPressRunnable?.let {
            keyPressHandler.removeCallbacks(it)
        }
    }
    
    
    override fun onBackPressed() {
        // Do nothing - launcher shouldn't close
    }
}

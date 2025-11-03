package com.customlauncher.app.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.data.SelectionManager
import com.customlauncher.app.databinding.ActivityAppListBinding
import com.customlauncher.app.ui.adapter.AppListAdapter
import com.customlauncher.app.ui.viewmodel.AppViewModel

class AppListActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAppListBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var appAdapter: AppListAdapter
    
    // BroadcastReceiver for package changes (install/uninstall)
    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_PACKAGE_REMOVED,
                Intent.ACTION_PACKAGE_ADDED,
                Intent.ACTION_PACKAGE_REPLACED -> {
                    val packageName = intent.dataString?.removePrefix("package:")
                    Log.d("AppListActivity", "Package changed: ${intent.action} - $packageName")
                    
                    // Reload apps list after small delay to ensure package manager updated
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.loadApps()
                    }, 500)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Check if we should open settings directly
        if (intent.getBooleanExtra("open_settings", false)) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            finish()
            return
        }
        
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        
        // Initialize SelectionManager with saved hidden apps if not already set
        if (!SelectionManager.hasSelection()) {
            val savedHiddenApps = LauncherApplication.instance.preferences.getHiddenApps()
            if (savedHiddenApps.isNotEmpty()) {
                SelectionManager.setSelection(savedHiddenApps)
            }
        }
        
        setupRecyclerView()
        setupListeners()
        observeViewModel()
        
        // Load apps
        viewModel.loadApps()
    }
    
    private fun setupRecyclerView() {
        appAdapter = AppListAdapter(
            onAppClick = { app ->
                viewModel.toggleAppSelection(app)
            },
            onAppLongClick = { app ->
                viewModel.launchApp(app.packageName)
            }
        )
        
        binding.appsList.apply {
            layoutManager = LinearLayoutManager(this@AppListActivity)
            adapter = appAdapter
            
            // Set custom fade sizes
            val fadeSize = (30 * resources.displayMetrics.density).toInt()
            setFadeSizes(fadeSize, fadeSize)
            
            overScrollMode = android.view.View.OVER_SCROLL_NEVER
        }
    }
    
    private fun setupListeners() {
        // Tab navigation
        binding.tabApplications.setOnClickListener {
            // Already on applications tab
        }
        
        binding.tabSettings.setOnClickListener {
            // Save selection before switching to settings
            val selectedApps = viewModel.getSelectedApps()
            val packageNames = selectedApps.map { it.packageName }.toSet()
            SelectionManager.setSelection(packageNames)
            
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
        
        // Select all button (now TextView)
        binding.selectAllButton.setOnClickListener {
            val allSelected = viewModel.getSelectedApps().isNotEmpty()
            viewModel.selectAllApps(!allSelected)
            updateSelectAllButton()
        }
        
        // Search functionality
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchApps(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        
        // Launch button - saves selected apps as hidden and goes to home screen
        binding.launchButton.setOnClickListener {
            val preferences = LauncherApplication.instance.preferences
            
            // Only check permissions if the setting is enabled
            if (preferences.checkPermissionsOnStartup) {
                // Check if all permissions are granted
                val allPermissionsGranted = checkAllPermissionsGranted()
                
                if (!allPermissionsGranted) {
                    // Open settings page if permissions not granted
                    Toast.makeText(this, "Необходимо активировать все разрешения", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, SettingsActivity::class.java))
                    return@setOnClickListener
                }
            }
            
            // Save current selection to both SelectionManager and preferences
            val selectedApps = viewModel.getSelectedApps()
            val packageNames = selectedApps.map { it.packageName }.toSet()
            
            // Update permanent storage
            LauncherApplication.instance.preferences.setHiddenApps(packageNames)
            
            // Save selected apps as hidden in database
            viewModel.saveSelectedAppsAsHidden()
            
            // Go to our home screen activity directly
            val intent = Intent(this, HomeScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    
    private fun observeViewModel() {
        viewModel.filteredApps.observe(this) { apps ->
            appAdapter.submitList(apps)
            updateSelectAllButton()
            updateLaunchButton()
        }
    }
    
    private fun updateSelectAllButton() {
        val selectedApps = viewModel.getSelectedApps()
        val hasSelection = selectedApps.isNotEmpty()
        binding.selectAllButton.text = if (hasSelection) {
            "УБРАТЬ ВСЁ"
        } else {
            "ВЫБРАТЬ ВСЕ"
        }
    }
    
    private fun updateLaunchButton() {
        binding.launchButton.isEnabled = true
        binding.launchButton.text = "ЗАПУСТИТЬ"
    }
    
    private fun checkAllPermissionsGranted(): Boolean {
        // If permission checking is disabled, always return true
        val preferences = LauncherApplication.instance.preferences
        if (!preferences.checkPermissionsOnStartup) {
            return true
        }
        
        val serviceName = "${packageName}/${com.customlauncher.app.service.SystemBlockAccessibilityService::class.java.canonicalName}"
        val enabledServices = android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        val accessibilityEnabled = enabledServices?.contains(serviceName) == true
        
        val overlayGranted = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M || 
                            android.provider.Settings.canDrawOverlays(this)
        
        val notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val dndGranted = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M || 
                        notificationManager.isNotificationPolicyAccessGranted
        
        val writeSettingsGranted = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M || 
                                  android.provider.Settings.System.canWrite(this)
        
        val intent = android.content.Intent(android.content.Intent.ACTION_MAIN)
        intent.addCategory(android.content.Intent.CATEGORY_HOME)
        val resolveInfo = packageManager.resolveActivity(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
        val isDefaultHome = resolveInfo?.activityInfo?.packageName == packageName
        
        return accessibilityEnabled && overlayGranted && dndGranted && writeSettingsGranted && isDefaultHome
    }
    
    override fun onResume() {
        super.onResume()
        
        // Register package change receiver
        val packageFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        
        // Android 12+ requires explicit export flag
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(packageChangeReceiver, packageFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(packageChangeReceiver, packageFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(packageChangeReceiver, packageFilter)
        }
        
        // Reload apps in case something changed while activity was paused
        viewModel.loadApps()
    }
    
    override fun onPause() {
        super.onPause()
        
        // Unregister receiver
        try {
            unregisterReceiver(packageChangeReceiver)
        } catch (e: Exception) {
            Log.d("AppListActivity", "packageChangeReceiver already unregistered")
        }
        
        // Save current selection to SelectionManager when leaving activity
        val selectedApps = viewModel.getSelectedApps()
        val packageNames = selectedApps.map { it.packageName }.toSet()
        SelectionManager.setSelection(packageNames)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Make sure receiver is unregistered
        try {
            unregisterReceiver(packageChangeReceiver)
        } catch (e: Exception) {
            Log.d("AppListActivity", "packageChangeReceiver already unregistered in onDestroy")
        }
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

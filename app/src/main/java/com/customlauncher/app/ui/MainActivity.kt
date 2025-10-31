package com.customlauncher.app.ui

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.service.TouchBlockService
import com.customlauncher.app.manager.HiddenModeStateManager
import com.customlauncher.app.receiver.KeyCombinationReceiver
import com.customlauncher.app.receiver.CustomKeyListener
import com.customlauncher.app.data.model.CustomKeyCombination
import com.customlauncher.app.ui.adapter.AppGridAdapter
import com.customlauncher.app.ui.viewmodel.AppViewModel
import com.customlauncher.app.utils.IconCache
import android.util.Log
import android.widget.Toast
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.app.KeyguardManager
import android.view.WindowManager
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.lifecycleScope
import android.provider.Settings
import android.text.TextUtils
import android.widget.PopupWindow
import com.customlauncher.app.data.model.AppInfo
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.net.Uri
import android.view.Gravity
import com.customlauncher.app.databinding.ActivityMainBinding
import android.content.ActivityNotFoundException
import com.customlauncher.app.R
import com.customlauncher.app.ui.layout.PaginatedGridLayoutManager

class MainActivity : AppCompatActivity() {
    
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var appAdapter: AppGridAdapter
    private val preferences by lazy { LauncherApplication.instance.preferences }
    private var currentPopupWindow: PopupWindow? = null
    
    // Key combination tracking
    private var customKeyListener: CustomKeyListener? = null
    private val keyPressHandler = Handler(Looper.getMainLooper())
    private var paginatedLayoutManager: PaginatedGridLayoutManager? = null
    private var currentFocusedPosition = 0
    
    // Touch event throttling
    private var lastTouchEventTime = 0L
    private val TOUCH_THROTTLE_MS = 100L
    private var touchEventCount = 0
    private val MAX_TOUCH_EVENTS_PER_SECOND = 30
    
    // BroadcastReceiver for key combinations from AccessibilityService
    private val keyCombinationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                KeyCombinationReceiver.ACTION_KEY_COMBINATION_DETECTED -> {
                    Log.d("MainActivity", "Received key combination broadcast")
                    // Update UI after visibility change
                    updateVisibility()
                }
            }
        }
    }
    
    // BroadcastReceiver for hidden mode changes
    private val hiddenModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "com.customlauncher.HIDDEN_MODE_CHANGED" -> {
                    val isHidden = intent.getBooleanExtra("is_hidden", false)
                    Log.d("MainActivity", "Received hidden mode change broadcast: $isHidden")
                    // Force refresh state and UI
                    HiddenModeStateManager.refreshState(context ?: return)
                    updateVisibility()
                }
            }
        }
    }
    
    // Add handler for debouncing reloads
    private val reloadHandler = Handler(Looper.getMainLooper())
    private var reloadRunnable: Runnable? = null
    
    // BroadcastReceiver for icon pack changes
    private val iconPackChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.ICON_PACK_CHANGED") {
                Log.d("MainActivity", "Icon pack changed, refreshing app list")
                // Clear icon cache and reload apps
                IconCache.clear()
                viewModel.loadApps()
            }
        }
    }
    
    // BroadcastReceiver for screenshot blocking
    private val screenshotBlockingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.SCREENSHOT_BLOCKING") {
                val shouldBlock = intent.getBooleanExtra("block_screenshots", false)
                Log.d("MainActivity", "Screenshot blocking: $shouldBlock")
                
                if (shouldBlock) {
                    // Add FLAG_SECURE to prevent screenshots
                    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
                } else {
                    // Remove FLAG_SECURE to allow screenshots
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }
        }
    }
    
    // BroadcastReceiver for app labels changes
    private val appLabelsChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.APP_LABELS_CHANGED") {
                Log.d("MainActivity", "App labels setting changed, refreshing app list")
                // Refresh the adapter to update labels visibility
                appAdapter.notifyDataSetChanged()
            }
        }
    }
    
    // BroadcastReceiver for button phone mode changes
    private val buttonPhoneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.BUTTON_PHONE_MODE_CHANGED") {
                Log.d("MainActivity", "Button phone mode changed, updating grid")
                updateGridColumns()
                viewModel.loadApps()
            }
        }
    }
    
    // BroadcastReceiver for package changes (install/uninstall)
    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_PACKAGE_REMOVED,
                Intent.ACTION_PACKAGE_ADDED,
                Intent.ACTION_PACKAGE_REPLACED -> {
                    val packageName = intent.dataString?.removePrefix("package:")
                    Log.d("MainActivity", "Package changed: ${intent.action} - $packageName")
                    
                    // Cancel previous reload if pending
                    reloadRunnable?.let { reloadHandler.removeCallbacks(it) }
                    
                    // Invalidate cache when package changes
                    LauncherApplication.instance.repository.invalidateAppCache()
                    
                    // Schedule new reload with shorter delay for responsiveness
                    reloadRunnable = Runnable {
                        Log.d("MainActivity", "Executing debounced app reload")
                        if (!isFinishing && !isDestroyed) {
                            viewModel.loadApps()
                        }
                    }
                    reloadHandler.postDelayed(reloadRunnable!!, 150) // 150ms for quick response
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // MainActivity is now only for HOME screen
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            // Remove lock screen flags - we only want them when explicitly needed
            // These flags were preventing normal lock screen from appearing
            
            // Make system bars transparent - use new API for Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate setup", e)
        }
        
        try {
            viewModel = ViewModelProvider(this)[AppViewModel::class.java]
            
            setupRecyclerView()
            setupListeners()
            observeViewModel()
            
            // Observe state changes from StateManager with better debouncing
            lifecycleScope.launch {
                try {
                    var lastUpdateTime = 0L
                    HiddenModeStateManager.isHiddenMode.collectLatest { isHidden ->
                        Log.d("MainActivity", "State changed: hidden=$isHidden")
                        
                        val currentTime = System.currentTimeMillis()
                        // Prevent too frequent updates (min 500ms between updates)
                        if (currentTime - lastUpdateTime < 500) {
                            return@collectLatest
                        }
                        lastUpdateTime = currentTime
                        
                        // Longer debounce for better performance
                        keyPressHandler.removeCallbacksAndMessages("load_apps")
                        keyPressHandler.postDelayed({
                            viewModel.loadApps()
                        }, "load_apps", 500)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error observing state changes", e)
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing viewModel", e)
        }
        
        // Check overlay permission for touch blocking
        checkOverlayPermission()
        
        // Register broadcast receiver for multiple actions
        val filter = IntentFilter().apply {
            addAction(KeyCombinationReceiver.ACTION_KEY_COMBINATION_DETECTED)
            addAction("com.customlauncher.HIDDEN_MODE_CHANGED")
        }
        
        // Register package change receiver
        val packageFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        
        // Register icon pack change receiver
        val iconPackFilter = IntentFilter("com.customlauncher.ICON_PACK_CHANGED")
        
        // Register screenshot blocking receiver
        val screenshotFilter = IntentFilter("com.customlauncher.SCREENSHOT_BLOCKING")
        
        // Register button phone mode receiver
        val buttonPhoneModeFilter = IntentFilter("com.customlauncher.BUTTON_PHONE_MODE_CHANGED")
        
        // Register app labels change receiver
        val appLabelsFilter = IntentFilter("com.customlauncher.APP_LABELS_CHANGED")
        
        // Android 12+ requires explicit export flag
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ has the constant
            registerReceiver(keyCombinationReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            registerReceiver(hiddenModeReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            registerReceiver(packageChangeReceiver, packageFilter, Context.RECEIVER_NOT_EXPORTED)
            registerReceiver(iconPackChangeReceiver, iconPackFilter, Context.RECEIVER_NOT_EXPORTED)
            registerReceiver(screenshotBlockingReceiver, screenshotFilter, Context.RECEIVER_NOT_EXPORTED)
            registerReceiver(buttonPhoneModeReceiver, buttonPhoneModeFilter, Context.RECEIVER_NOT_EXPORTED)
            registerReceiver(appLabelsChangeReceiver, appLabelsFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 needs the flag value directly (2)
            registerReceiver(keyCombinationReceiver, filter, 2) // RECEIVER_NOT_EXPORTED = 2
            registerReceiver(hiddenModeReceiver, filter, 2)
            registerReceiver(packageChangeReceiver, packageFilter, 2)
            registerReceiver(iconPackChangeReceiver, iconPackFilter, 2)
            registerReceiver(screenshotBlockingReceiver, screenshotFilter, 2)
            registerReceiver(buttonPhoneModeReceiver, buttonPhoneModeFilter, 2)
            registerReceiver(appLabelsChangeReceiver, appLabelsFilter, 2)
        } else {
            // Android 11 and below
            registerReceiver(keyCombinationReceiver, filter)
            registerReceiver(hiddenModeReceiver, filter)
            registerReceiver(packageChangeReceiver, packageFilter)
            registerReceiver(iconPackChangeReceiver, iconPackFilter)
            registerReceiver(screenshotBlockingReceiver, screenshotFilter)
            registerReceiver(buttonPhoneModeReceiver, buttonPhoneModeFilter)
            registerReceiver(appLabelsChangeReceiver, appLabelsFilter)
        }
        
        // Check initial state
        updateVisibility()
        
        // Initialize custom key listener
        setupCustomKeyListener()
    }
    
    private fun setupCustomKeyListener() {
        // Clean up old listener first
        customKeyListener?.destroy()
        customKeyListener = null
        
        val preferences = LauncherApplication.instance.preferences
        
        if (preferences.useCustomKeys) {
            val customKeysString = preferences.customKeyCombination
            if (customKeysString != null) {
                val keys = customKeysString.split(",").mapNotNull { it.toIntOrNull() }
                if (keys.isNotEmpty()) {
                    val combination = CustomKeyCombination(keys)
                    customKeyListener = CustomKeyListener {
                        Log.d("MainActivity", "Custom key combination triggered")
                        toggleHiddenMode()
                    }
                    customKeyListener?.setCombination(combination)
                    Log.d("MainActivity", "Custom key listener setup with keys: $keys")
                }
            }
        } else {
            Log.d("MainActivity", "Custom keys disabled")
        }
    }
    
    private fun checkOverlayPermission() {
        // Skip permission checking if disabled
        val preferences = LauncherApplication.instance.preferences
        if (!preferences.checkPermissionsOnStartup) {
            return
        }
        
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
        appAdapter = AppGridAdapter(
            onAppClick = { app ->
                viewModel.launchApp(app.packageName)
            },
            onAppLongClick = { app, view ->
                showAppContextMenu(app, view)
            }
        )
        
        binding.appsGrid.apply {
            val preferences = LauncherApplication.instance.preferences
            
            // Check if button phone mode is enabled
            if (preferences.buttonPhoneMode) {
                // Use paginated layout for button phones
                val gridSize = preferences.buttonPhoneGridSize
                val (cols, rows) = when (gridSize) {
                    "3x4" -> 3 to 4
                    else -> 3 to 3  // Default to 3x3
                }
                
                paginatedLayoutManager = PaginatedGridLayoutManager(this@MainActivity, cols, rows)
                layoutManager = paginatedLayoutManager
            } else {
                // Use standard grid layout for touch phones
                val columnCount = if (preferences.gridColumnCount == 0) 4 else preferences.gridColumnCount
                layoutManager = GridLayoutManager(this@MainActivity, columnCount)
            }
            
            adapter = appAdapter
            
            // Performance optimizations
            setHasFixedSize(true)
            setItemViewCacheSize(20) // Cache more views
            recycledViewPool.setMaxRecycledViews(0, 20) // Increase recycled view pool
            isNestedScrollingEnabled = false // Disable nested scrolling if not needed
            
            // Set custom fade sizes to match padding
            val topFadeSize = (50 * resources.displayMetrics.density).toInt()
            val bottomFadeSize = (40 * resources.displayMetrics.density).toInt()
            setFadeSizes(topFadeSize, bottomFadeSize)
            
            // Disable overscroll effect
            overScrollMode = View.OVER_SCROLL_NEVER
            
            // Add scroll listener to dismiss popup menu
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    // Dismiss popup when scrolling starts
                    if (newState != androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                        currentPopupWindow?.dismiss()
                        currentPopupWindow = null
                    }
                }
                
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // Also dismiss on any scroll movement
                    if (dx != 0 || dy != 0) {
                        currentPopupWindow?.dismiss()
                        currentPopupWindow = null
                    }
                }
            })
        }
    }
    
    private fun setupListeners() {
        // Long press on home screen opens app list
        binding.appsGrid.setOnLongClickListener {
            val preferences = LauncherApplication.instance.preferences
            
            // Check if permission checking is enabled
            if (preferences.checkPermissionsOnStartup) {
                // Check if all permissions are granted
                if (!checkAllPermissions()) {
                    showPermissionsDialog()
                    return@setOnLongClickListener false
                }
            }
            
            startActivity(Intent(this, AppListActivity::class.java))
            true
        }
    }
    
    private fun checkAllPermissions(): Boolean {
        // If permission checking is disabled, always return true
        val preferences = LauncherApplication.instance.preferences
        if (!preferences.checkPermissionsOnStartup) {
            return true
        }
        
        var allGranted = true
        val missingPermissions = mutableListOf<String>()
        
        // Check Accessibility Service
        if (!isAccessibilityServiceEnabled()) {
            allGranted = false
            missingPermissions.add("Специальные возможности")
        }
        
        // Check Overlay Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                allGranted = false
                missingPermissions.add("Наложение поверх окон")
            }
        }
        
        // Check Do Not Disturb Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                allGranted = false
                missingPermissions.add("Режим не беспокоить")
            }
        }
        
        // Check Write Settings Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.System.canWrite(this)) {
                allGranted = false
                missingPermissions.add("Изменение системных настроек")
            }
        }
        
        return allGranted
    }
    
    private fun showPermissionsDialog() {
        val missingPermissions = mutableListOf<String>()
        
        // Check which permissions are missing
        if (!isAccessibilityServiceEnabled()) {
            missingPermissions.add("• Специальные возможности")
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                missingPermissions.add("• Наложение поверх окон")
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                missingPermissions.add("• Режим не беспокоить")
            }
            
            if (!android.provider.Settings.System.canWrite(this)) {
                missingPermissions.add("• Изменение системных настроек")
            }
        }
        
        val message = "Для корректной работы приложения необходимо включить следующие разрешения:\n\n" +
                missingPermissions.joinToString("\n") +
                "\n\nПерейти в настройки?"
        
        AlertDialog.Builder(this)
            .setTitle("Требуются разрешения")
            .setMessage(message)
            .setPositiveButton("Настройки") { _, _ ->
                val intent = Intent(this, SettingsActivity::class.java)
                intent.putExtra("scroll_to_permissions", true)
                startActivity(intent)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    
    private fun observeViewModel() {
        viewModel.allApps.observe(this) { apps ->
            // Debounce updates to avoid excessive recomposition
            keyPressHandler.removeCallbacksAndMessages("update_apps")
            keyPressHandler.postDelayed({
                // Process in background thread to avoid UI blocking
                lifecycleScope.launch(Dispatchers.Default) {
                    try {
                        // Show all apps or only visible based on hidden state
                        val hidden = preferences.appsHidden
                        val appsToShow = if (hidden) {
                            // Filter out hidden apps
                            apps.filter { !it.isHidden }
                        } else {
                            // Show all apps
                            apps
                        }
                        
                        // Update UI on main thread
                        withContext(Dispatchers.Main) {
                            try {
                                // Only update if activity is still active
                                if (!isFinishing && !isDestroyed) {
                                    appAdapter.submitList(appsToShow)
                                } else {
                                    Log.d("MainActivity", "Activity is finishing, skipping update")
                                }
                            } catch (e: Exception) {
                                Log.e("MainActivity", "Error updating app list", e)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error processing apps", e)
                    }
                }
            }, "update_apps", 100) // 100ms debounce
        }
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Handle button phone navigation
        if (preferences.buttonPhoneMode && paginatedLayoutManager != null) {
            val handled = when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_4 -> {
                    if (paginatedLayoutManager?.navigateLeft() == true) {
                        updateFocusedItem()
                        true
                    } else false
                }
                KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_6 -> {
                    if (paginatedLayoutManager?.navigateRight() == true) {
                        updateFocusedItem()
                        true
                    } else false
                }
                KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_2 -> {
                    if (paginatedLayoutManager?.navigateUp() == true) {
                        updateFocusedItem()
                        true
                    } else false
                }
                KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_8 -> {
                    if (paginatedLayoutManager?.navigateDown() == true) {
                        updateFocusedItem()
                        true
                    } else false
                }
                KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_ENTER -> {
                    val position = paginatedLayoutManager?.getSelectedPosition() ?: 0
                    val app = appAdapter.currentList.getOrNull(position)
                    app?.let {
                        viewModel.launchApp(it.packageName)
                    }
                    true
                }
                else -> false
            }
            
            if (handled) return true
        }
        
        // Handle custom key combinations
        if (preferences.useCustomKeys) {
            customKeyListener?.let { listener ->
                if (listener.onKeyEvent(keyCode)) {
                    return true
                }
            }
        }
        
        return super.onKeyDown(keyCode, event)
    }
    
    private fun updateFocusedItem() {
        // Update visual focus on the selected item
        val position = paginatedLayoutManager?.getSelectedPosition() ?: 0
        binding.appsGrid.smoothScrollToPosition(position)
        
        // Find the view holder and update focus
        binding.appsGrid.post {
            val viewHolder = binding.appsGrid.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.requestFocus()
        }
    }
    
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyUp(keyCode, event)
    }
    
    private fun toggleHiddenMode() {
        // Toggle hidden mode
        val currentState = HiddenModeStateManager.currentState
        val newState = !currentState
        HiddenModeStateManager.setHiddenMode(this, newState)
        
        // Update UI immediately
        updateVisibility()
    }
    
    private fun showAppContextMenu(app: AppInfo, anchor: View) {
        // Dismiss any existing popup
        currentPopupWindow?.dismiss()
        
        // Inflate the popup menu layout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_app_menu, null)
        
        // Create the popup window
        val popupWindow = PopupWindow(
            popupView,
            resources.getDimensionPixelSize(R.dimen.popup_menu_width),
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )
        
        // Save reference to current popup
        currentPopupWindow = popupWindow
        
        // Set background for proper dismissal
        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 8f
        }
        
        // Set dismiss listener to clear reference
        popupWindow.setOnDismissListener {
            currentPopupWindow = null
        }
        
        // Setup click listeners for menu items
        val menuAppInfo = popupView.findViewById<android.widget.TextView>(R.id.menu_app_info)
        val menuUninstall = popupView.findViewById<android.widget.TextView>(R.id.menu_uninstall)
        val menuArrow = popupView.findViewById<View>(R.id.menu_arrow)
        
        Log.d("MainActivity", "Menu views found - AppInfo: ${menuAppInfo != null}, Uninstall: ${menuUninstall != null}")
        
        if (menuAppInfo != null) {
            menuAppInfo.setOnClickListener {
                Log.d("MainActivity", "App info button clicked")
                popupWindow.dismiss()
                openAppInfo(app.packageName)
            }
        } else {
            Log.e("MainActivity", "menuAppInfo is null!")
        }
        
        if (menuUninstall != null) {
            menuUninstall.setOnClickListener { view ->
                Log.d("MainActivity", "=== UNINSTALL CLICKED ===")
                Log.d("MainActivity", "App: ${app.appName}")
                Log.d("MainActivity", "Package: ${app.packageName}")
                
                // Show debug toast
                Toast.makeText(this@MainActivity, "Удаляем: ${app.appName}", Toast.LENGTH_SHORT).show()
                
                // Dismiss popup first
                popupWindow.dismiss()
                currentPopupWindow = null
                
                // Validate and uninstall
                if (!app.packageName.isNullOrEmpty() && app.packageName != "null") {
                    // Direct call, no delay
                    uninstallApp(app.packageName.trim())
                } else {
                    Log.e("MainActivity", "Invalid package name: ${app.packageName}")
                    Toast.makeText(this@MainActivity, "Ошибка: неверное имя пакета", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e("MainActivity", "menuUninstall is null!")
        }
        
        // Calculate popup position (above the icon with more space)
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        
        val popupWidth = resources.getDimensionPixelSize(R.dimen.popup_menu_width)
        val popupHeight = resources.getDimensionPixelSize(R.dimen.popup_menu_height)
        val screenWidth = resources.displayMetrics.widthPixels
        
        // Calculate icon center position
        val iconCenterX = location[0] + (anchor.width / 2)
        
        // Calculate popup X position (try to center on icon)
        var xPos = iconCenterX - (popupWidth / 2)
        
        // Adjust if goes off screen edges
        val margin = 10
        if (xPos < margin) {
            xPos = margin
        } else if (xPos + popupWidth > screenWidth - margin) {
            xPos = screenWidth - popupWidth - margin
        }
        
        // Position above the icon with arrow overlapping the icon significantly
        var yPos = location[1] - popupHeight + 30  // Large overlap with icon (30dp)
        
        // If too close to top, show below icon instead
        if (yPos < 50) {  // Give some margin at top
            yPos = location[1] + anchor.height - 50
            // Hide arrow when showing below (or flip it)
            menuArrow.visibility = View.GONE
        } else {
            // Calculate arrow position to always point to icon
            menuArrow.visibility = View.VISIBLE
            
            // Calculate how much to shift the arrow horizontally
            val popupCenterX = xPos + (popupWidth / 2)
            val arrowOffsetX = iconCenterX - popupCenterX
            
            // Apply horizontal offset to arrow
            val layoutParams = menuArrow.layoutParams as android.widget.LinearLayout.LayoutParams
            layoutParams.leftMargin = (popupWidth / 2 - 10 + arrowOffsetX).toInt() // 10 is half of arrow width
            menuArrow.layoutParams = layoutParams
        }
        
        // Show the popup with animation
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos)
    }
    
    private fun openAppInfo(packageName: String) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Не удалось открыть информацию о приложении", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun uninstallApp(packageName: String) {
        Log.d("MainActivity", "Starting uninstall for package: $packageName")
        
        // Verify package exists
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            Log.d("MainActivity", "Package found: ${appInfo.packageName}")
        } catch (e: Exception) {
            Log.e("MainActivity", "Package not found: $packageName", e)
            Toast.makeText(this, "Приложение не найдено", Toast.LENGTH_SHORT).show()
            return
        }
        
        try {
            // Use standard ACTION_DELETE which works on all Android versions
            val packageUri = Uri.parse("package:$packageName")
            val uninstallIntent = Intent(Intent.ACTION_DELETE, packageUri)
            
            // This will show the system's uninstall dialog
            startActivity(uninstallIntent)
            
            Log.d("MainActivity", "Uninstall dialog launched successfully for: $packageName")
            
        } catch (e: ActivityNotFoundException) {
            Log.e("MainActivity", "No uninstall activity found", e)
            
            // Fallback: Open app info page where user can uninstall manually
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                Toast.makeText(this, "Нажмите 'Удалить' в настройках приложения", Toast.LENGTH_LONG).show()
            } catch (ex: Exception) {
                Log.e("MainActivity", "Cannot open app settings", ex)
                Toast.makeText(this, "Не удалось открыть настройки", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error launching uninstall", e)
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
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
    
    private var lastVisibilityUpdate = 0L
    private val VISIBILITY_UPDATE_DEBOUNCE = 100L // Reduced to 100ms for responsiveness
    
    private fun updateVisibility() {
        // Prevent rapid consecutive calls
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastVisibilityUpdate < VISIBILITY_UPDATE_DEBOUNCE) {
            Log.d("MainActivity", "Skipping visibility update - too frequent")
            return
        }
        lastVisibilityUpdate = currentTime
        
        // Use state manager for consistent state
        val hidden = HiddenModeStateManager.currentState
        
        Log.d("MainActivity", "Updating visibility: hidden=$hidden")
        
        // If entering hidden mode and using paginated layout, reset to first page
        if (hidden && paginatedLayoutManager != null) {
            Log.d("MainActivity", "Resetting to first page in button phone mode")
            paginatedLayoutManager?.resetToFirstPage()
        }
        
        // Cancel previous app reload
        keyPressHandler.removeCallbacksAndMessages("load_apps")
        
        // Optimized delay - 150ms max for UI responsiveness
        val delay = if (!hidden) 150L else 100L 
        keyPressHandler.postDelayed({
            if (!isFinishing && !isDestroyed) {
                Log.d("MainActivity", "Executing delayed app reload")
                viewModel.loadApps()
            }
        }, "load_apps", delay)
        
        // Touch blocking is handled by StateManager
        // No need to manage it here
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val serviceName = "${packageName}/${com.customlauncher.app.service.SystemBlockAccessibilityService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return enabledServices?.contains(serviceName) == true
    }
    
    override fun onResume() {
        super.onResume()
        // Update grid columns if changed in settings
        updateGridColumns()
        // Update custom key listener
        setupCustomKeyListener()
    }
    
    private fun updateGridColumns() {
        val preferences = LauncherApplication.instance.preferences
        
        if (preferences.buttonPhoneMode) {
            // Switch to paginated layout for button phones
            val gridSize = preferences.buttonPhoneGridSize
            val (cols, rows) = when (gridSize) {
                "3x4" -> 3 to 4
                else -> 3 to 3  // Default to 3x3
            }
            
            if (paginatedLayoutManager == null || 
                paginatedLayoutManager?.columns != cols || 
                paginatedLayoutManager?.rows != rows) {
                paginatedLayoutManager = PaginatedGridLayoutManager(this, cols, rows)
                binding.appsGrid.layoutManager = paginatedLayoutManager
            }
        } else {
            // Switch to standard grid layout
            val columnCount = if (preferences.gridColumnCount == 0) 4 else preferences.gridColumnCount
            if (binding.appsGrid.layoutManager !is GridLayoutManager ||
                (binding.appsGrid.layoutManager as? GridLayoutManager)?.spanCount != columnCount) {
                binding.appsGrid.layoutManager = GridLayoutManager(this, columnCount)
                paginatedLayoutManager = null
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Clean up custom key listener
        customKeyListener?.destroy()
        customKeyListener = null
        
        // Clean up handlers to prevent memory leaks
        keyPressHandler.removeCallbacksAndMessages(null)
        reloadHandler.removeCallbacksAndMessages(null)
        reloadRunnable = null
        
        // Unregister receivers safely
        try {
            unregisterReceiver(keyCombinationReceiver)
        } catch (e: Exception) {
            Log.d("MainActivity", "keyCombinationReceiver already unregistered")
        }
        
        try {
            unregisterReceiver(hiddenModeReceiver)
        } catch (e: Exception) {
            Log.d("MainActivity", "hiddenModeReceiver already unregistered")
        }
        
        try {
            unregisterReceiver(packageChangeReceiver)
        } catch (e: Exception) {
            Log.d("MainActivity", "packageChangeReceiver already unregistered")
        }
        
        try {
            unregisterReceiver(iconPackChangeReceiver)
        } catch (e: Exception) {
            Log.d("MainActivity", "iconPackChangeReceiver already unregistered")
        }
        
        try {
            unregisterReceiver(screenshotBlockingReceiver)
        } catch (e: Exception) {
            Log.d("MainActivity", "screenshotBlockingReceiver already unregistered")
        }
        
        try {
            unregisterReceiver(buttonPhoneModeReceiver)
        } catch (e: Exception) {
            Log.d("MainActivity", "buttonPhoneModeReceiver already unregistered")
        }
        
        try {
            unregisterReceiver(appLabelsChangeReceiver)
        } catch (e: Exception) {
            Log.d("MainActivity", "appLabelsChangeReceiver already unregistered")
        }
        
        // Clear adapter to free memory
        appAdapter.submitList(emptyList())
    }
    
    
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        try {
            // Throttle touch events to prevent overflow
            val currentTime = System.currentTimeMillis()
            
            // Reset counter every second
            if (currentTime - lastTouchEventTime > 1000) {
                touchEventCount = 0
                lastTouchEventTime = currentTime
            }
            
            // If too many touch events, skip processing
            touchEventCount++
            if (touchEventCount > MAX_TOUCH_EVENTS_PER_SECOND) {
                Log.w("MainActivity", "Too many touch events, throttling")
                return true
            }
            
            return super.dispatchTouchEvent(ev)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in dispatchTouchEvent", e)
            return true
        }
    }
    
    override fun onBackPressed() {
        // Do nothing - launcher shouldn't close
    }
}

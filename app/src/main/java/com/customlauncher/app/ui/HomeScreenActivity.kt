package com.customlauncher.app.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetProviderInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.Toast
import android.widget.TextView
import android.widget.ImageView
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.customlauncher.app.R
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.lifecycleScope
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.receiver.CustomKeyListener
import com.customlauncher.app.data.model.CustomKeyCombination
import com.customlauncher.app.databinding.ActivityHomeScreenBinding
import com.customlauncher.app.manager.HiddenModeStateManager
import com.customlauncher.app.manager.HomeScreenModeManager
import com.customlauncher.app.manager.GridFocusManager
import com.customlauncher.app.data.model.GridConfiguration
import com.customlauncher.app.ui.layout.HomeScreenGridLayout
import com.customlauncher.app.data.repository.HomeScreenRepository
import com.customlauncher.app.ui.adapter.HomeScreenAdapter
import com.customlauncher.app.data.model.HomeItemModel
import com.customlauncher.app.ui.dragdrop.DragDropManager
import com.customlauncher.app.ui.widget.WidgetResizeManager
import com.customlauncher.app.ui.widget.MenuButtonWidget
import com.customlauncher.app.utils.IconCache
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

class HomeScreenActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var repository: HomeScreenRepository
    private lateinit var adapter: HomeScreenAdapter
    private lateinit var gridLayout: HomeScreenGridLayout
    private lateinit var dragDropManager: DragDropManager
    private var widgetResizeManager: WidgetResizeManager? = null
    private lateinit var modeManager: HomeScreenModeManager
    private lateinit var focusManager: GridFocusManager
    private var customKeyListener: CustomKeyListener? = null
    private var isLoadingItems = false // Flag to prevent concurrent loading
    private var lastShowHomeScreen: Boolean? = null // Track last known state
    private var currentPopupWindow: PopupWindow? = null
    private var isAppDrawerOpen = false // Prevent multiple drawer instances
    private var lastDrawerOpenTime = 0L // For debouncing drawer opening
    private val preferences by lazy { LauncherApplication.instance.preferences }
    
    // Widget management
    private lateinit var appWidgetHost: AppWidgetHost
    private lateinit var appWidgetManager: AppWidgetManager
    private var pendingWidgetId: Int = -1
    // Removed isDragModeEnabled - now both drag and menu work automatically
    
    companion object {
        private const val TAG = "HomeScreenActivity"
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
        private const val REQUEST_PICK_WIDGET = 1001
        private const val REQUEST_CREATE_WIDGET = 1002
        private const val APPWIDGET_HOST_ID = 2024
        private const val DRAWER_OPEN_DEBOUNCE_MS = 500L // Задержка между открытиями меню
    }
    
    // BroadcastReceiver for hidden mode changes
    private val hiddenModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.HIDDEN_MODE_CHANGED") {
                val isHidden = intent.getBooleanExtra("is_hidden", false)
                Log.d(TAG, "Hidden mode changed: $isHidden")
                updateVisibility(isHidden)
            }
        }
    }
    
    // BroadcastReceiver for icon pack changes
    private val iconPackChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.ICON_PACK_CHANGED") {
                Log.d(TAG, "Icon pack changed, clearing cache and reloading items")
                lifecycleScope.launch {
                    // Clear icon cache to force reload with new icon pack
                    IconCache.clearCache()
                    
                    withContext(Dispatchers.Main) {
                        // Clear all existing views to force redraw
                        adapter.clearAllViews()
                        // Reload items with new icons
                        loadHomeScreenItems()
                    }
                }
            }
        }
    }
    
    // BroadcastReceiver for grid configuration changes
    private val gridConfigReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.GRID_CONFIG_CHANGED") {
                Log.d(TAG, "Grid config changed, recreating grid")
                setupUI()
                // Force reload items to apply new adaptive sizes
                loadHomeScreenItems()
            }
        }
    }
    
    // BroadcastReceiver for mode changes
    private val modeChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == HomeScreenModeManager.ACTION_MODE_CHANGED) {
                val newMode = intent.getIntExtra(HomeScreenModeManager.EXTRA_MODE, HomeScreenModeManager.MODE_TOUCH)
                val previousMode = intent.getIntExtra(HomeScreenModeManager.EXTRA_PREVIOUS_MODE, HomeScreenModeManager.MODE_TOUCH)
                Log.d(TAG, "Mode changed from $previousMode to $newMode")
                handleModeChange(newMode)
            }
        }
    }
    
    // BroadcastReceiver for menu access method changes
    private val menuMethodChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.MENU_METHOD_CHANGED") {
                Log.d(TAG, "Menu access method changed")
                updateMenuButtonVisibility()
            }
        }
    }
    
    // BroadcastReceiver for home screen visibility changes
    private val homeScreenVisibilityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.HOME_SCREEN_VISIBILITY_CHANGED") {
                val showHomeScreen = preferences.showHomeScreen
                Log.d(TAG, "Home screen visibility changed: $showHomeScreen")
                
                if (showHomeScreen && lastShowHomeScreen == false) {
                    // Home screen was re-enabled, reload everything
                    Log.d(TAG, "Home screen re-enabled via settings, reloading...")
                    binding.gridContainer.visibility = View.VISIBLE
                    gridLayout.visibility = View.VISIBLE
                    
                    lifecycleScope.launch {
                        if (adapter.getItemCount() == 0) {
                            Log.d(TAG, "No items on home screen after transition from app drawer")
                            gridLayout.visibility = View.VISIBLE
                            
                            // Just reload items, don't initialize defaults again
                            loadHomeScreenItems()
                        } else {
                            repository.initializeDefaultItems(GridConfiguration.fromPreferences(preferences))
                            withContext(Dispatchers.Main) {
                                // Clear existing views
                                adapter.clearAllViews()
                                
                                // Reload items
                                loadHomeScreenItems()
                                
                                // Force layout update
                                gridLayout.requestLayout()
                                gridLayout.invalidate()
                            }
                        }
                    }
                } else if (!showHomeScreen && lastShowHomeScreen == true) {
                    // Home screen was disabled
                    Log.d(TAG, "Home screen disabled via settings")
                    binding.gridContainer.visibility = View.GONE
                    // Don't open app drawer automatically when home screen is disabled in settings
                    // User might be in settings and doesn't want the drawer to open
                } else if (!showHomeScreen) {
                    // Still disabled, keep grid hidden
                    binding.gridContainer.visibility = View.GONE
                }
                
                lastShowHomeScreen = showHomeScreen
            }
        }
    }
    
    // BroadcastReceiver for package changes (install/uninstall)
    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.PACKAGE_CHANGED") {
                val action = intent.getStringExtra("action")
                val packageName = intent.getStringExtra("package")
                Log.d(TAG, "Package change detected: $action for $packageName")
                
                // Handle package removal
                if (action == Intent.ACTION_PACKAGE_REMOVED) {
                    packageName?.let { pkg ->
                        // First, animate removal from UI
                        adapter.removePackageAnimated(pkg)
                        
                        // Then remove from database in background
                        lifecycleScope.launch(Dispatchers.IO) {
                            val items = repository.getAllItems()
                            val itemToRemove = items.find { it.packageName == pkg }
                            itemToRemove?.let {
                                repository.deleteItem(it)
                                Log.d(TAG, "Removed $pkg from database")
                            }
                        }
                    }
                } else if (action == Intent.ACTION_PACKAGE_ADDED) {
                    // For new packages, just reload items
                    // Could potentially add animation here too
                    loadHomeScreenItems()
                } else {
                    // For other changes, just reload items
                    loadHomeScreenItems()
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize mode manager
        modeManager = HomeScreenModeManager(this)
        
        // Initialize last known home screen state
        lastShowHomeScreen = preferences.showHomeScreen
        
        // Setup transparent background
        setupTransparentBackground()
        
        // Initialize gesture detector for swipe
        setupGestureDetector()
        
        // Register receivers
        registerReceivers()
        
        // Initialize hidden mode state
        HiddenModeStateManager.initializeState()
        
        // Setup initial UI
        setupUI()
        
        // Check if this is the first launch as system launcher
        handleSystemLauncherMode()
        
        // Check if home screen is disabled and show apps menu directly
        if (!preferences.showHomeScreen) {
            // Hide home screen content
            binding.gridContainer.visibility = View.GONE
            
            // Open app drawer immediately
            binding.root.post {
                showAppDrawer()
            }
        }
    }
    
    private fun setupTransparentBackground() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setDecorFitsSystemWindows(false)
            } else {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = 
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            
            statusBarColor = android.graphics.Color.TRANSPARENT
            navigationBarColor = android.graphics.Color.TRANSPARENT
        }
    }
    
    private fun setupGestureDetector() {
        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    return false // Horizontal swipe, ignore
                }
                
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY < 0) {
                        // Swipe up - open app drawer
                        openAppDrawer()
                        return true
                    }
                }
                
                return false
            }
            
            override fun onLongPress(e: MotionEvent) {
                // Show context menu on long press on empty space
                Log.d(TAG, "Long press detected at ${e.x}, ${e.y}")
                showContextMenu(e.x, e.y)
            }
            
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                // Return false to allow click events to pass through
                return false
            }
        })
    }
    
    private fun setupUI() {
        try {
            // Initialize grid layout with configuration from preferences
            val gridConfig = GridConfiguration.fromPreferences(preferences)
            
            // Create and add grid layout to container
            gridLayout = HomeScreenGridLayout(this).apply {
                setGridConfiguration(gridConfig)
                
                // Enable focus handling for D-pad navigation
                isFocusable = true
                // Never use focusableInTouchMode to prevent white background
                isFocusableInTouchMode = false
                // Ensure transparent background
                background = null
                
                // Setup button phone mode if enabled
                if (preferences.homeScreenMode == HomeScreenModeManager.MODE_BUTTON) {
                    setButtonPhoneMode(true)
                }
                
                // Setup D-pad navigation for opening app drawer
                // This works for both button mode and touch mode with physical keyboard
                onBottomReached = {
                    // Open app drawer when navigating down from bottom row
                    val menuMethod = preferences.menuAccessMethod
                    Log.d(TAG, "Bottom reached via D-pad, menu method: $menuMethod")
                    
                    // In button mode, respect the menu access method setting
                    if (preferences.homeScreenMode == HomeScreenModeManager.MODE_BUTTON) {
                        when (menuMethod) {
                            "dpad_down" -> openAppDrawer()
                            "button", "gesture" -> {
                                // Focus on menu button if visible
                                binding.menuButtonWidget.let { button ->
                                    if (button.visibility == View.VISIBLE) {
                                        button.requestFocus()
                                    } else {
                                        openAppDrawer()
                                    }
                                }
                            }
                            else -> openAppDrawer()
                        }
                    } else {
                        // In touch mode (with physical keyboard), always open directly
                        openAppDrawer()
                    }
                }
            }
        
            binding.gridContainer.removeAllViews()
            binding.gridContainer.addView(gridLayout)
            
            // Set up touch listener to handle long press ONLY on empty space
            binding.gridContainer.setOnTouchListener { v, event ->
                // Don't process gesture detector here - will be handled in onTouchEvent
                false
            }
            
            gridLayout.setOnTouchListener { _, event ->
                // Don't process gesture detector here - will be handled in onTouchEvent
                false
            }
            
            // Initialize repository
            repository = HomeScreenRepository(this)
            
            // Initialize drag and drop manager
            dragDropManager = DragDropManager(
                gridLayout = gridLayout,
                onItemMoved = { item, newX, newY ->
                    handleItemMoved(item, newX, newY)
                },
                onItemDropped = { item, x, y ->
                    handleItemDropped(item, x, y)
                },
                onAppDroppedFromDrawer = { app, x, y ->
                    handleAppDroppedFromDrawer(app, x, y)
                }
            )
            
            // Initialize widget management
            appWidgetManager = AppWidgetManager.getInstance(this)
            appWidgetHost = AppWidgetHost(this, APPWIDGET_HOST_ID)
            appWidgetHost.startListening()
            
            // Initialize adapter
            adapter = HomeScreenAdapter(
                context = this,
                gridLayout = gridLayout,
                onItemClick = { item ->
                    launchApp(item)
                },
                onItemLongClick = { item, view ->
                    showItemContextMenu(item, view)
                    true
                },
                onItemStartDrag = { view, item ->
                    // Dismiss any open context menu when starting drag
                    currentPopupWindow?.dismiss()
                    currentPopupWindow = null
                    
                    if (dragDropManager.startDrag(view, item)) {
                        Log.d(TAG, "Drag started for item ${item.id}")
                        true
                    } else {
                        false
                    }
                },
                appWidgetHost = appWidgetHost,
                appWidgetManager = appWidgetManager
            )
            
            // Set initial button mode state
            adapter.setButtonMode(modeManager.isButtonMode())
        
            // Initialize focus manager for button mode
            focusManager = GridFocusManager(gridConfig.columns, gridConfig.rows)
            focusManager.setFocusChangeListener(object : GridFocusManager.FocusChangeListener {
                override fun onFocusChanged(oldPosition: Int, newPosition: Int, item: HomeItemModel?) {
                    // Update adapter focus
                    if (modeManager.isButtonMode()) {
                        adapter.setFocusedPosition(newPosition)
                    }
                }
                
                override fun onGridFocusChanged(oldRow: Int, oldCol: Int, newRow: Int, newCol: Int, item: HomeItemModel?) {
                    // Update grid layout focus visualization
                    if (modeManager.isButtonMode()) {
                        gridLayout.setFocusedCell(newRow, newCol)
                    }
                }
                
                override fun onItemSelected(position: Int, item: HomeItemModel?) {
                    // Launch the selected item
                    item?.let { launchApp(it) }
                }
                
                override fun onNavigateToMenu() {
                    val menuMethod = preferences.menuAccessMethod
                    Log.d(TAG, "Navigate to menu requested, method: $menuMethod")
                    
                    // In button mode, check menu access method
                    if (modeManager.isButtonMode()) {
                        when (menuMethod) {
                            "dpad_down" -> {
                                // Direct open menu when navigating down
                                Log.d(TAG, "Opening menu directly (dpad_down)")
                                openAppDrawer()
                            }
                            "button", "gesture" -> {
                                // Focus on menu button if visible
                                if (binding.menuButtonWidget.shouldBeVisible()) {
                                    Log.d(TAG, "Navigating to menu button")
                                    // Clear grid focus
                                    adapter.clearFocus()
                                    // Focus on menu button
                                    binding.menuButtonWidget.requestButtonFocus()
                                } else {
                                    // If button is not visible, open directly
                                    openAppDrawer()
                                }
                            }
                            else -> openAppDrawer()
                        }
                    } else {
                        // In touch mode, always open menu directly for keyboard users
                        Log.d(TAG, "Opening menu directly (touch mode with keyboard)")
                        openAppDrawer()
                    }
                }
                
                override fun shouldNavigateToMenuOnDown(): Boolean {
                    // Allow navigation to menu in both button mode and touch mode (for keyboard users)
                    return true
                }
            })
            
            // Initialize default items if first run, then load items
            lifecycleScope.launch {
                // Check if home screen is completely empty
                val itemCount = repository.getAllItems().size
                if (itemCount == 0) {
                    Log.d(TAG, "Home screen is empty, initializing defaults")
                    repository.initializeDefaultItems(gridConfig)
                } else {
                    Log.d(TAG, "Home screen has $itemCount items")
                }
                
                // Then load items from database (including any newly created defaults)
                withContext(Dispatchers.Main) {
                    loadHomeScreenItems()
                }
            }
            
            // Setup menu button for button mode
            setupMenuButton()
            
            // Update visibility based on hidden mode
            updateVisibility()
            
            Log.d(TAG, "Grid initialized with ${gridConfig.columns}x${gridConfig.rows}")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up UI", e)
            Toast.makeText(this, "Ошибка инициализации главного экрана", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun registerReceivers() {
        // Register hidden mode receiver
        val hiddenFilter = IntentFilter("com.customlauncher.HIDDEN_MODE_CHANGED")
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(hiddenModeReceiver, hiddenFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(hiddenModeReceiver, hiddenFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(hiddenModeReceiver, hiddenFilter)
        }
        
        // Register grid config receiver
        val gridFilter = IntentFilter("com.customlauncher.GRID_CONFIG_CHANGED")
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(gridConfigReceiver, gridFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(gridConfigReceiver, gridFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(gridConfigReceiver, gridFilter)
        }
        
        // Register mode change receiver
        val modeFilter = IntentFilter(HomeScreenModeManager.ACTION_MODE_CHANGED)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(modeChangeReceiver, modeFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(modeChangeReceiver, modeFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(modeChangeReceiver, modeFilter)
        }
        
        // Register menu method change receiver
        val menuMethodFilter = IntentFilter("com.customlauncher.MENU_METHOD_CHANGED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(menuMethodChangeReceiver, menuMethodFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(menuMethodChangeReceiver, menuMethodFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(menuMethodChangeReceiver, menuMethodFilter)
        }
        
        // Register home screen visibility receiver
        val homeScreenVisibilityFilter = IntentFilter("com.customlauncher.HOME_SCREEN_VISIBILITY_CHANGED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(homeScreenVisibilityReceiver, homeScreenVisibilityFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(homeScreenVisibilityReceiver, homeScreenVisibilityFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(homeScreenVisibilityReceiver, homeScreenVisibilityFilter)
        }
        
        // Register package change receiver
        val packageFilter = IntentFilter("com.customlauncher.PACKAGE_CHANGED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(packageChangeReceiver, packageFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(packageChangeReceiver, packageFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(packageChangeReceiver, packageFilter)
        }
        
        // Register icon pack change receiver
        val iconPackFilter = IntentFilter("com.customlauncher.ICON_PACK_CHANGED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(iconPackChangeReceiver, iconPackFilter, Context.RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerReceiver(iconPackChangeReceiver, iconPackFilter, 2) // RECEIVER_NOT_EXPORTED = 2
        } else {
            registerReceiver(iconPackChangeReceiver, iconPackFilter)
        }
    }
    
    private fun handleSystemLauncherMode() {
        // Check if we're running as the default launcher
        val isDefaultLauncher = isDefaultLauncher()
        
        if (isDefaultLauncher) {
            // Always show home screen when we're the default launcher
            Log.d(TAG, "Running as default launcher - home screen enabled")
            return
        }
        
        // Otherwise check user preference
        checkHomeScreenEnabled()
    }
    
    private fun isDefaultLauncher(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName == packageName
    }
    
    private fun checkHomeScreenEnabled() {
        // Only check this preference when NOT the default launcher
        if (isDefaultLauncher()) {
            return
        }
        
        // Check if home screen is enabled in settings
        val showHomeScreen = preferences.showHomeScreen
        if (!showHomeScreen) {
            // If home screen is disabled, just hide the grid
            binding.gridContainer.visibility = View.GONE
            // Don't automatically open app drawer or finish activity
            // User will manually open it if needed
        }
    }
    
    private fun handleModeChange(newMode: Int) {
        Log.d(TAG, "Handling mode change to: ${modeManager.getModeString(newMode)}")
        
        when (newMode) {
            HomeScreenModeManager.MODE_BUTTON -> {
                // Enable button mode features
                Log.d(TAG, "Switching to button mode")
                // Enable button mode in adapter
                adapter.setButtonMode(true)
                // Request initial focus
                focusManager.requestInitialFocus()
                // Update menu button visibility for button mode
                updateMenuButtonVisibility()
            }
            HomeScreenModeManager.MODE_TOUCH -> {
                // Disable button mode features
                Log.d(TAG, "Switching to touch mode")
                // Disable button mode in adapter
                adapter.setButtonMode(false)
                // Clear focus
                focusManager.clearFocus()
                // Update menu button visibility for touch mode
                updateMenuButtonVisibility()
            }
        }
    }
    
    private fun setupMenuButton() {
        // Setup click listener
        binding.menuButtonWidget.setOnMenuClickListener {
            Log.d(TAG, "Menu button clicked, opening app drawer")
            openAppDrawer()
        }
        
        // Initial setup based on current mode
        updateMenuButtonVisibility()
    }
    
    private fun updateMenuButtonVisibility() {
        if (modeManager.isButtonMode()) {
            binding.menuButtonWidget.setButtonMode(true)
            
            // Adjust grid layout if menu button is visible
            val menuMethod = preferences.menuAccessMethod
            if (menuMethod == "button" || menuMethod == "gesture") {
                // Show bottom bar with menu button
                binding.bottomBarContainer.visibility = View.VISIBLE
                binding.menuButtonWidget.updateVisibility()
                Log.d(TAG, "Bottom bar visible with menu button")
            } else {
                // Hide bottom bar, grid takes full height
                binding.bottomBarContainer.visibility = View.GONE
                Log.d(TAG, "Bottom bar hidden, grid uses full height")
            }
            
            // Request layout to update constraints
            binding.gridContainer.requestLayout()
        } else {
            // Hide bottom bar in touch mode
            binding.bottomBarContainer.visibility = View.GONE
            // Request layout to update constraints
            binding.gridContainer.requestLayout()
        }
    }
    
    private fun updateVisibility(isHidden: Boolean? = null) {
        lifecycleScope.launch {
            val currentHiddenState = isHidden ?: HiddenModeStateManager.currentState
            val hideApps = preferences.hideAppsInHiddenMode
            val showHomeScreen = preferences.showHomeScreen
            
            Log.d(TAG, "Updating visibility, hidden mode: $currentHiddenState, hideApps: $hideApps, showHomeScreen: $showHomeScreen")
            
            // Always reload items when visibility changes to ensure correct filtering
            // Use Main context to avoid concurrent modification
            withContext(Dispatchers.Main) {
                loadHomeScreenItems()
            }
        }
    }
    
    private fun openAppDrawer() {
        // Проверяем, не открыт ли уже drawer
        if (isAppDrawerOpen) {
            Log.d(TAG, "App drawer is already open, ignoring request")
            return
        }
        
        // Debouncing - игнорируем быстрые повторные вызовы
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastDrawerOpenTime < DRAWER_OPEN_DEBOUNCE_MS) {
            Log.d(TAG, "Ignoring rapid drawer open request (debouncing ${DRAWER_OPEN_DEBOUNCE_MS}ms)")
            return
        }
        
        // Проверяем, нет ли уже открытого диалога
        val existingDialog = supportFragmentManager.findFragmentByTag("AppDrawerBottomSheet")
        if (existingDialog != null) {
            Log.d(TAG, "App drawer dialog already exists, ignoring request")
            return
        }
        
        // Cancel any active widget resize
        widgetResizeManager?.stopResize(save = false)
        
        try {
            Log.d(TAG, "Opening app drawer bottom sheet")
            
            // Устанавливаем флаги
            isAppDrawerOpen = true
            lastDrawerOpenTime = currentTime
            
            val bottomSheet = com.customlauncher.app.ui.dialog.AppDrawerBottomSheet()
            bottomSheet.show(supportFragmentManager, "AppDrawerBottomSheet")
        } catch (e: Exception) {
            Log.e(TAG, "Error opening app drawer", e)
            isAppDrawerOpen = false
            
            // Fallback to old method if needed
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
    
    private fun showAppDrawer() {
        openAppDrawer()
    }
    
    fun onAppDrawerClosed() {
        isAppDrawerOpen = false
        Log.d(TAG, "App drawer closed callback received")
    }
    
    private fun setButtonPhoneMode(enabled: Boolean) {
        Log.d(TAG, "Setting button phone mode: $enabled")
        gridLayout.setButtonPhoneMode(enabled)
        adapter.setButtonMode(enabled)
        
        // In button mode, always request initial focus
        // In touch mode, keep focus if user is using D-pad
        if (enabled) {
            focusManager.requestInitialFocus()
        }
        // Don't clear focus in touch mode - user might be using physical keyboard
    }
    
    private fun loadHomeScreenItems() {
        // Prevent concurrent loading
        if (isLoadingItems) {
            Log.d(TAG, "Already loading items, skipping duplicate call")
            return
        }
        
        isLoadingItems = true
        
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val allItems = repository.getAllItems()
            
            // Check if launcher is on home screen
            val hasLauncher = allItems.any { 
                it.packageName == "com.customlauncher.app" 
            }
            
            if (!hasLauncher) {
                // Add launcher to home screen at position (2, 5)
                val launcherApp = HomeItemModel(
                    id = 0,
                    type = HomeItemModel.ItemType.APP,
                    packageName = "com.customlauncher.app",
                    componentName = "com.customlauncher.app/.ui.AppListActivity",
                    label = "ИLauncher",
                    cellX = 2,
                    cellY = 5,
                    spanX = 1,
                    spanY = 1
                )
                val launcherId = repository.addItem(launcherApp)
                Log.d(TAG, "Added ИLauncher to home screen at position 2,5 with id $launcherId")
                
                // Reload items including the new launcher
                val updatedItems = repository.getAllItems()
                val filteredItems = filterHiddenApps(updatedItems)
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "Loaded ${filteredItems.size} items from database")
                    adapter.submitList(filteredItems)
                    // Update focus manager with items
                    focusManager.updateItems(filteredItems)
                }
            } else {
                // Filter hidden apps if needed
                val filteredItems = filterHiddenApps(allItems)
                
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "Loaded ${filteredItems.size} items from database (filtered from ${allItems.size})")
                    
                    // Separate bottom bar items from regular grid items
                    val bottomBarItems = filteredItems.filter { it.cellY == -1 } // Special Y for bottom bar
                    val gridItems = filteredItems.filter { it.cellY != -1 }
                    
                    // Load bottom bar icons
                    loadBottomBarIcons(bottomBarItems)
                    
                    // Load regular grid items
                    adapter.submitList(gridItems)
                    // Update focus manager with items
                    focusManager.updateItems(gridItems)
                }
            }
            } finally {
                isLoadingItems = false
            }
        }
    }
    
    private fun filterHiddenApps(items: List<HomeItemModel>): List<HomeItemModel> {
        val preferences = LauncherApplication.instance.preferences
        
        // First, filter out system Android components (like ResolverActivity)
        val nonSystemItems = items.filter { item ->
            // Remove Android system components
            val isSystemComponent = item.packageName == "android" || 
                                   item.componentName?.contains("com.android.internal") == true ||
                                   item.componentName?.contains("ResolverActivity") == true
            
            if (isSystemComponent) {
                Log.d(TAG, "Filtering out system component: ${item.packageName}/${item.componentName}")
            }
            
            !isSystemComponent
        }
        
        // If not in hidden mode or hideAppsInHiddenMode is disabled, return non-system items
        if (!HiddenModeStateManager.currentState || !preferences.hideAppsInHiddenMode) {
            return nonSystemItems
        }
        
        // Filter out hidden apps
        val hiddenApps = preferences.getHiddenApps()
        return nonSystemItems.filter { item ->
            // Keep widgets and non-app items
            if (item.type != HomeItemModel.ItemType.APP) {
                true
            } else {
                // Keep app if it's not in hidden list
                item.packageName?.let { pkg ->
                    !hiddenApps.contains(pkg)
                } ?: true
            }
        }
    }
    
    private fun launchApp(item: HomeItemModel) {
        Log.d(TAG, "Launching app: ${item.packageName}, our package: ${this.packageName}")
        try {
            item.packageName?.let { packageName ->
                // Check if it's our own launcher app - always open AppListActivity
                if (packageName == this.packageName || packageName == "com.customlauncher.app") {
                    Log.d(TAG, "Launching our own launcher app - opening AppListActivity")
                    // Launch the AppListActivity for our launcher (app selection screen)
                    val intent = Intent(this, AppListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    
                    // Optional: Show a subtle transition
                    @Suppress("DEPRECATION")
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                } else {
                    Log.d(TAG, "Launching external app: $packageName")
                    // Launch other apps normally
                    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(launchIntent)
                    } else {
                        Toast.makeText(this, "Не удалось запустить приложение", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching app: ${item.packageName}", e)
            Toast.makeText(this, "Ошибка запуска приложения", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showItemContextMenu(item: HomeItemModel, anchorView: View) {
        // Dismiss any existing popup
        currentPopupWindow?.dismiss()
        
        // Inflate the popup menu layout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_home_item_menu, null)
        
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
        popupWindow.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 8f
        }
        
        // Set dismiss listener to clear reference
        popupWindow.setOnDismissListener {
            currentPopupWindow = null
        }
        
        // Setup menu items based on item type
        val menuResize = popupView.findViewById<TextView>(R.id.menu_resize)
        val dividerResize = popupView.findViewById<View>(R.id.divider_resize)
        val menuRemove = popupView.findViewById<TextView>(R.id.menu_remove)
        val menuInfo = popupView.findViewById<TextView>(R.id.menu_info)
        val menuUninstall = popupView.findViewById<TextView>(R.id.menu_uninstall)
        val divider1 = popupView.findViewById<View>(R.id.divider1)
        val divider2 = popupView.findViewById<View>(R.id.divider2)
        val menuArrow = popupView.findViewById<View>(R.id.menu_arrow)
        
        if (item.type == HomeItemModel.ItemType.APP) {
            // For apps, show all menu items except resize
            menuResize.visibility = View.GONE
            dividerResize.visibility = View.GONE
            menuRemove.text = "Убрать"
            menuInfo.visibility = View.VISIBLE
            menuUninstall.visibility = View.VISIBLE
            divider1.visibility = View.VISIBLE
            divider2.visibility = View.VISIBLE
        } else if (item.type == HomeItemModel.ItemType.WIDGET) {
            // For widgets, show resize and remove options
            // But hide resize for clock widget as it's handled via dialog
            val isClockWidget = item.componentName == "com.customlauncher.widget.clock" || 
                                item.componentName == "clock"
            
            if (isClockWidget) {
                // Clock widget - no resize option (size is selected via dialog)
                menuResize.visibility = View.GONE
                dividerResize.visibility = View.GONE
            } else {
                // Other widgets - show resize option
                menuResize.visibility = View.VISIBLE
                dividerResize.visibility = View.VISIBLE
            }
            
            menuRemove.text = "Убрать виджет"
            menuInfo.visibility = View.GONE
            menuUninstall.visibility = View.GONE
            divider1.visibility = View.GONE
            divider2.visibility = View.GONE
        }
        
        // Setup click listeners
        menuResize?.setOnClickListener {
            popupWindow.dismiss()
            // Start resize mode for the widget
            startWidgetResize(item, anchorView)
        }
        
        menuRemove.setOnClickListener {
            popupWindow.dismiss()
            // Remove item from home screen
            lifecycleScope.launch {
                // First remove from adapter immediately for visual feedback
                adapter.removeItem(item.id)
                
                // If it's a widget, also delete from AppWidgetHost
                if (item.type == HomeItemModel.ItemType.WIDGET && item.widgetId != null && item.widgetId > 0) {
                    try {
                        appWidgetHost.deleteAppWidgetId(item.widgetId)
                        Log.d(TAG, "Deleted widget from host: ${item.widgetId}")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error deleting widget from host", e)
                    }
                }
                
                // Then delete from database
                withContext(Dispatchers.IO) {
                    repository.deleteItem(item)
                }
                
                // Show toast
                Toast.makeText(this@HomeScreenActivity, 
                    if (item.type == HomeItemModel.ItemType.WIDGET) "Виджет удален" 
                    else "Убрано с главного экрана", 
                    Toast.LENGTH_SHORT
                ).show()
                
                // Don't reload all items - they're already correctly positioned
                // Just update the adapter's internal list
                withContext(Dispatchers.IO) {
                    val updatedItems = repository.getAllItems()
                    withContext(Dispatchers.Main) {
                        // Update without reloading to preserve positions
                        adapter.updateItemsList(updatedItems)
                    }
                }
            }
        }
        
        menuInfo?.setOnClickListener {
            popupWindow.dismiss()
            showAppInfo(item.packageName)
        }
        
        menuUninstall?.setOnClickListener {
            popupWindow.dismiss()
            uninstallApp(item.packageName)
        }
        
        // Calculate popup position relative to icon
        val iconLocation = IntArray(2)
        anchorView.getLocationOnScreen(iconLocation)
        
        val popupWidth = resources.getDimensionPixelSize(R.dimen.popup_menu_width)
        val popupHeight = resources.getDimensionPixelSize(R.dimen.popup_menu_height)
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val density = resources.displayMetrics.density
        
        // Calculate icon center position
        val iconCenterX = iconLocation[0] + anchorView.width / 2
        val iconTopY = iconLocation[1]
        val iconBottomY = iconLocation[1] + anchorView.height
        
        // Calculate where popup should be (centered above icon)
        var popupX = iconCenterX - popupWidth / 2
        
        // Margin between menu and icon
        val marginFromIcon = (1 * density).toInt()
        var popupY = iconTopY - popupHeight - marginFromIcon
        
        // Handle horizontal screen boundaries
        val horizontalMargin = (10 * density).toInt()
        if (popupX < horizontalMargin) {
            popupX = horizontalMargin
        } else if (popupX + popupWidth > screenWidth - horizontalMargin) {
            popupX = screenWidth - popupWidth - horizontalMargin
        }
        
        // Calculate where the arrow should point (relative to popup position)
        val arrowTargetX = iconCenterX - popupX // Where arrow should point relative to popup left edge
        
        // Handle vertical screen boundaries
        val statusBarHeight = (25 * density).toInt() // Status bar height in pixels
        var isShowingBelow = false
        
        if (popupY < statusBarHeight) {
            // Show below icon if not enough space above
            popupY = iconBottomY + marginFromIcon
            isShowingBelow = true
            
            // Adjust arrow for showing below icon
            if (menuArrow != null) {
                // Rotate 180 degrees to point up
                menuArrow.rotation = 180f
                
                // Adjust the arrow margin to position it at the top
                val arrowParams = menuArrow.layoutParams as? ViewGroup.MarginLayoutParams
                if (arrowParams != null) {
                    // Move arrow to top of popup by adjusting margin
                    arrowParams.topMargin = -(5 * density).toInt() // Overlap with menu top
                    arrowParams.bottomMargin = 0
                    menuArrow.layoutParams = arrowParams
                }
            }
        } else {
            // Arrow points down normally
            menuArrow?.rotation = 0f
            
            // Reset arrow margins for bottom position
            if (menuArrow != null) {
                val arrowParams = menuArrow.layoutParams as? ViewGroup.MarginLayoutParams
                if (arrowParams != null) {
                    arrowParams.topMargin = -(5 * density).toInt() // Default margin
                    arrowParams.bottomMargin = (6 * density).toInt()
                    menuArrow.layoutParams = arrowParams
                }
            }
        }
        
        // Position arrow to point at icon center
        if (menuArrow != null) {
            // Arrow is centered by default (layout_gravity="center_horizontal")
            // Calculate how much to translate it from center to point at icon
            val popupCenterX = popupWidth / 2f
            val arrowTargetFromCenter = arrowTargetX - popupCenterX
            
            // Apply translation to move arrow to correct position
            menuArrow.translationX = arrowTargetFromCenter
            
            // Make arrow visible
            menuArrow.visibility = View.VISIBLE
            
            Log.d(TAG, "Arrow positioning: translationX=$arrowTargetFromCenter, " +
                    "iconCenterX=$iconCenterX, popupX=$popupX, " +
                    "popupCenterX=$popupCenterX, arrowTargetX=$arrowTargetX, " +
                    "isShowingBelow=$isShowingBelow")
        }
        
        Log.d(TAG, "Showing popup at: x=$popupX, y=$popupY, iconCenter=$iconCenterX, iconTop=$iconTopY")
        
        // Show the popup with animation
        try {
            popupWindow.animationStyle = android.R.style.Animation_Dialog
            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, popupY)
        } catch (e: Exception) {
            Log.e(TAG, "Error showing popup", e)
            // Fallback to showAsDropDown
            val offsetX = -popupWidth/2 + anchorView.width/2
            val offsetY = if (isShowingBelow) marginFromIcon else -anchorView.height - popupHeight - marginFromIcon
            popupWindow.showAsDropDown(anchorView, offsetX, offsetY)
        }
    }
    
    private fun showAppInfo(packageName: String?) {
        packageName?.let {
            try {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = android.net.Uri.parse("package:$it")
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Error showing app info", e)
            }
        }
    }
    
    private fun handleItemMoved(item: HomeItemModel, newX: Int, newY: Int) {
        Log.d(TAG, "handleItemMoved: Moving item ${item.id} from ${item.cellX},${item.cellY} to $newX,$newY")
        
        // First update UI visually
        val moved = adapter.moveItem(item.id, newX, newY)
        
        if (moved) {
            Log.d(TAG, "Visual move succeeded for item ${item.id}")
            // If visual update succeeded, update database
            lifecycleScope.launch(Dispatchers.IO) {
                repository.moveItem(item.id, newX, newY)
                Log.d(TAG, "Item ${item.id} moved to $newX, $newY in database")
            }
        } else {
            Log.e(TAG, "Failed to move item ${item.id} to $newX, $newY visually")
            // Reload to ensure UI is in sync with database
            loadHomeScreenItems()
        }
    }
    
    private fun handleItemDropped(item: HomeItemModel, x: Int, y: Int) {
        // Handle the drop completion - UI is already updated by DragDropManager
        Log.d(TAG, "Item dropped at $x, $y")
        // Don't reload - the UI is already in the correct state
    }
    
    private fun canAddToBottomBar(x: Float, y: Float): Boolean {
        // Check if drop position is in bottom bar area
        if (!modeManager.isButtonMode() || binding.bottomBarContainer.visibility != View.VISIBLE) {
            return false
        }
        
        val location = IntArray(2)
        binding.bottomBarContainer.getLocationOnScreen(location)
        val barTop = location[1]
        val barBottom = barTop + binding.bottomBarContainer.height
        
        return y >= barTop && y <= barBottom
    }
    
    private fun addIconToBottomBar(app: com.customlauncher.app.data.model.AppInfo, x: Float) {
        val inflater = LayoutInflater.from(this)
        val iconView = inflater.inflate(R.layout.item_home_app, null, false)
        
        val icon = iconView.findViewById<ImageView>(R.id.appIcon)
        val label = iconView.findViewById<TextView>(R.id.appLabel)
        
        // Load app icon
        icon.setImageDrawable(app.icon)
        label.text = app.appName
        label.visibility = View.GONE // Hide label for bottom bar icons
        
        // Determine which container based on X position
        val screenWidth = resources.displayMetrics.widthPixels
        val centerX = screenWidth / 2f
        
        val container = if (x < centerX) {
            binding.leftIconsContainer
        } else {
            binding.rightIconsContainer
        }
        
        // Check container capacity (max 2 icons per side)
        if (container.childCount >= 2) {
            Toast.makeText(this, "Максимум 2 иконки с каждой стороны", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Set layout params
        val params = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.bottom_bar_icon_size),
            resources.getDimensionPixelSize(R.dimen.bottom_bar_icon_size)
        )
        params.setMargins(4.dpToPx(), 0, 4.dpToPx(), 0)
        iconView.layoutParams = params
        
        // Add click listener
        iconView.setOnClickListener {
            val tempItem = HomeItemModel(
                type = HomeItemModel.ItemType.APP,
                packageName = app.packageName,
                label = app.appName,
                cellX = 0,
                cellY = 0,
                spanX = 1,
                spanY = 1
            )
            launchApp(tempItem)
        }
        
        // Add to container
        container.addView(iconView)
        
        // Save to database as special bottom bar item
        lifecycleScope.launch {
            val position = if (container == binding.leftIconsContainer) {
                -1 - binding.leftIconsContainer.childCount // Negative for left side
            } else {
                1000 + binding.rightIconsContainer.childCount // Large number for right side
            }
            
            val homeItem = HomeItemModel(
                cellX = position,
                cellY = -1, // Special Y coordinate for bottom bar
                spanX = 1,
                spanY = 1,
                packageName = app.packageName,
                label = app.appName,
                type = HomeItemModel.ItemType.APP
            )
            repository.addItem(homeItem)
        }
    }
    
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
    
    private fun loadBottomBarIcons(items: List<HomeItemModel>) {
        // Clear existing icons
        binding.leftIconsContainer.removeAllViews()
        binding.rightIconsContainer.removeAllViews()
        
        // Sort items by position
        val leftItems = items.filter { it.cellX < 0 }.sortedByDescending { it.cellX }
        val rightItems = items.filter { it.cellX >= 1000 }.sortedBy { it.cellX }
        
        // Load left side icons
        leftItems.forEach { item ->
            addBottomBarIcon(item, binding.leftIconsContainer)
        }
        
        // Load right side icons
        rightItems.forEach { item ->
            addBottomBarIcon(item, binding.rightIconsContainer)
        }
    }
    
    private fun addBottomBarIcon(item: HomeItemModel, container: LinearLayout) {
        val inflater = LayoutInflater.from(this)
        val iconView = inflater.inflate(R.layout.item_home_app, container, false)
        
        val icon = iconView.findViewById<ImageView>(R.id.appIcon)
        val label = iconView.findViewById<TextView>(R.id.appLabel)
        
        // Load app icon
        item.packageName?.let { packageName ->
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                icon.setImageDrawable(packageManager.getApplicationIcon(appInfo))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load icon for $packageName", e)
            }
        }
        
        label.text = item.label
        label.visibility = View.GONE // Hide label for bottom bar
        
        // Set layout params
        val params = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.bottom_bar_icon_size),
            resources.getDimensionPixelSize(R.dimen.bottom_bar_icon_size)
        )
        params.setMargins(4.dpToPx(), 0, 4.dpToPx(), 0)
        iconView.layoutParams = params
        
        // Add click listener
        iconView.setOnClickListener {
            launchApp(item)
        }
        
        // Add long click for context menu
        iconView.setOnLongClickListener { view ->
            showItemContextMenu(item, view)
            true
        }
        
        // Store item reference for drag & drop
        iconView.tag = item
        
        // Add to container
        container.addView(iconView)
    }
    
    private fun showWidgetPicker() {
        try {
            Log.d(TAG, "Opening widget picker")
            
            val bottomSheet = com.customlauncher.app.ui.dialog.WidgetPickerBottomSheet { widgetInfo ->
                // Request to add the widget
                requestAddWidget(widgetInfo)
            }
            bottomSheet.show(supportFragmentManager, "widget_picker")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error showing widget picker", e)
            Toast.makeText(this, "Не удалось открыть выбор виджетов", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun requestAddWidget(widgetInfo: AppWidgetProviderInfo) {
        val appWidgetId = appWidgetHost.allocateAppWidgetId()
        pendingWidgetId = appWidgetId
        
        val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, widgetInfo.provider)
        
        if (canBind) {
            // If allowed, configure the widget
            configureWidget(appWidgetId, widgetInfo)
        } else {
            // Request permission to bind
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, widgetInfo.provider)
            startActivityForResult(intent, REQUEST_CREATE_WIDGET)
        }
    }
    
    private fun configureWidget(appWidgetId: Int, widgetInfo: AppWidgetProviderInfo) {
        if (widgetInfo.configure != null) {
            // Widget needs configuration
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.component = widgetInfo.configure
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            startActivityForResult(intent, REQUEST_PICK_WIDGET)
        } else {
            // No configuration needed, add widget directly
            onActivityResult(REQUEST_PICK_WIDGET, RESULT_OK, Intent().apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            })
        }
    }
    
    fun addClockWidget(spanX: Int = 2, spanY: Int = 1) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Find an empty spot for the widget with specified size
            val gridConfig = GridConfiguration.fromPreferences(preferences)
            val existingItems = repository.getAllItems()
            
            // Check if clock widget already exists
            if (existingItems.any { it.componentName == "com.customlauncher.widget.clock" }) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeScreenActivity, "Виджет часов уже добавлен", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            
            // Find an empty spot for the widget
            for (y in 0 until gridConfig.rows) {
                for (x in 0 until gridConfig.columns) {
                    // Check if widget fits at this position
                    if (x + spanX > gridConfig.columns || y + spanY > gridConfig.rows) {
                        continue // Widget doesn't fit here
                    }
                    
                    // Check if this position is free
                    val positionFree = !existingItems.any { item ->
                        val itemEndX = item.cellX + item.spanX - 1
                        val itemEndY = item.cellY + item.spanY - 1
                        val widgetEndX = x + spanX - 1
                        val widgetEndY = y + spanY - 1
                        
                        // Check for overlap
                        !(x > itemEndX || widgetEndX < item.cellX || y > itemEndY || widgetEndY < item.cellY)
                    }
                    
                    if (positionFree) {
                        val clockWidget = HomeItemModel(
                            id = 0, // Will be auto-generated
                            type = HomeItemModel.ItemType.WIDGET,
                            componentName = "com.customlauncher.widget.clock",
                            cellX = x,
                            cellY = y,
                            spanX = spanX,
                            spanY = spanY
                        )
                        
                        val id = repository.addItem(clockWidget)
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "Clock widget added at $x, $y with size ${spanX}x${spanY}")
                            Toast.makeText(this@HomeScreenActivity, "Часы ${spanX}x${spanY} добавлены на экран", Toast.LENGTH_SHORT).show()
                            // Force complete refresh to show the new widget
                            adapter.clearAllViews()
                            loadHomeScreenItems()
                            // Request layout update
                            gridLayout.requestLayout()
                            gridLayout.invalidate()
                        }
                        return@launch
                    }
                }
            }
            
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeScreenActivity, "Нет места для виджета", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun uninstallApp(packageName: String?) {
        packageName?.let {
            try {
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = android.net.Uri.parse("package:$it")
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Error uninstalling app", e)
                Toast.makeText(this, "Не удалось удалить приложение", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun handleGridResize() {
        // Get new grid configuration
        val newGridConfig = GridConfiguration.fromPreferences(preferences)
        
        // Check and fix out-of-bounds items
        lifecycleScope.launch(Dispatchers.IO) {
            val movedItems = repository.fixOutOfBoundsItems(newGridConfig)
            
            withContext(Dispatchers.Main) {
                if (movedItems.isNotEmpty()) {
                    Toast.makeText(
                        this@HomeScreenActivity,
                        "Некоторые элементы были перемещены из-за изменения размера сетки",
                        Toast.LENGTH_LONG
                    ).show()
                }
                
                // Reload the UI with new grid configuration
                setupUI()
                // Force reload items to apply new adaptive sizes
                loadHomeScreenItems()
            }
        }
    }
    
    private fun showContextMenu(x: Float, y: Float) {
        Log.d(TAG, "Showing context menu at $x, $y")
        
        // Dismiss any existing popup
        currentPopupWindow?.dismiss()
        
        // Create an anchor view at the touch position
        val anchorView = View(this).apply {
            layoutParams = FrameLayout.LayoutParams(1, 1).apply {
                leftMargin = x.toInt()
                topMargin = y.toInt()
            }
        }
        binding.gridContainer.addView(anchorView)
        
        // Inflate the popup menu layout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_home_empty_menu, null)
        
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
        popupWindow.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 8f
        }
        
        // Set dismiss listener to clear reference and cleanup anchor view
        popupWindow.setOnDismissListener {
            currentPopupWindow = null
            binding.gridContainer.removeView(anchorView)
        }
        
        // Setup menu items
        val menuSettings = popupView.findViewById<TextView>(R.id.menu_settings)
        val menuWidgets = popupView.findViewById<TextView>(R.id.menu_widgets)
        val menuArrow = popupView.findViewById<View>(R.id.menu_arrow)
        
        menuSettings.setOnClickListener {
            Log.d(TAG, "Opening settings")
            popupWindow.dismiss()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        
        menuWidgets.setOnClickListener {
            Log.d(TAG, "Opening widget picker")
            popupWindow.dismiss()
            showWidgetPicker()
        }
        
        // Calculate popup position
        val popupWidth = resources.getDimensionPixelSize(R.dimen.popup_menu_width)
        val popupHeight = resources.getDimensionPixelSize(R.dimen.popup_menu_height)
        val density = resources.displayMetrics.density
        
        // Position popup at touch position
        val xPos = (x - popupWidth / 2).toInt()
        val marginAbove = (100 * density).toInt() // Show above touch position
        val yPos = (y - popupHeight - marginAbove).toInt()
        
        // Position arrow to point at touch position
        if (menuArrow != null) {
            menuArrow.translationX = 0f // Center arrow since popup is centered
            menuArrow.visibility = View.VISIBLE
        }
        
        // Show the popup with animation
        try {
            popupWindow.animationStyle = android.R.style.Animation_Dialog
            popupWindow.showAtLocation(binding.root, Gravity.NO_GRAVITY, xPos, yPos)
        } catch (e: Exception) {
            Log.e(TAG, "Error showing popup", e)
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // This is called only if no child view consumed the event
        // So we know the touch is on empty space
        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Touch on empty space at ${event.x}, ${event.y}")
        }
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
    
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // Don't process touch events here - let children handle first
        // gestureDetector will be called in onTouchEvent if children didn't consume the event
        return super.dispatchTouchEvent(ev)
    }
    
    override fun onBackPressed() {
        // Check if we're in resize mode
        if (widgetResizeManager?.isResizing() == true) {
            // Cancel resize mode without saving
            widgetResizeManager?.stopResize(save = false)
            return
        }
        
        // On home screen, back button does nothing (we're already at the home)
        // This prevents going back to previous activities or closing the launcher
        Log.d(TAG, "Back pressed on home screen - ignored")
        
        // Optionally, you could show a dialog asking if the user wants to select another launcher
        // showLauncherChooser()
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if it's a D-pad key
        val isDpadKey = when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER -> true
            else -> false
        }
        
        // Handle D-pad navigation in button mode
        if (modeManager.isButtonMode() && event != null) {
            // Check if menu button has focus
            if (binding.menuButtonWidget.hasFocus()) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        // Navigate back to grid from menu button
                        Log.d(TAG, "Navigating from menu button back to grid")
                        binding.menuButtonWidget.clearButtonFocus()
                        // Focus on last item in bottom row
                        val items = focusManager.items
                        if (items.isNotEmpty()) {
                            val bottomItems = items.filter { it.cellY == items.maxOf { item -> item.cellY } }
                            val centerItem = bottomItems.minByOrNull { 
                                kotlin.math.abs(it.cellX - (gridLayout.getGridConfiguration().columns / 2)) 
                            }
                            centerItem?.let {
                                val position = items.indexOf(it)
                                focusManager.setFocusPosition(position)
                            }
                        }
                        return true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        // Open menu
                        openAppDrawer()
                        return true
                    }
                }
            }
            
            if (isDpadKey) {
                // Let focus manager handle D-pad navigation
                if (focusManager.handleKeyEvent(keyCode, event)) {
                    return true
                }
            }
        } else if (isDpadKey && event != null) {
            // In touch mode, still handle D-pad events for physical keyboard support
            // Use focus manager for consistent navigation across both modes
            if (!focusManager.hasFocus()) {
                // Request initial focus if not already focused
                focusManager.requestInitialFocus()
            }
            // Let focus manager handle D-pad navigation
            if (focusManager.handleKeyEvent(keyCode, event)) {
                return true
            }
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
    
    override fun onResume() {
        super.onResume()
        
        // Start listening for widget updates
        appWidgetHost.startListening()
        
        // Проверяем и сбрасываем флаг меню, если диалог не существует
        val existingDialog = supportFragmentManager.findFragmentByTag("AppDrawerBottomSheet")
        if (existingDialog == null && isAppDrawerOpen) {
            Log.d(TAG, "App drawer dialog not found but flag was set, resetting")
            isAppDrawerOpen = false
        }
        
        // Check if showHomeScreen setting changed
        val currentShowHomeScreen = preferences.showHomeScreen
        if (lastShowHomeScreen != null && lastShowHomeScreen != currentShowHomeScreen) {
            Log.d(TAG, "showHomeScreen changed from $lastShowHomeScreen to $currentShowHomeScreen")
            if (currentShowHomeScreen) {
                // Home screen was re-enabled, reload everything to show properly
                Log.d(TAG, "Home screen re-enabled, reloading all items with proper settings")
                
                // Make grid container visible first
                binding.gridContainer.visibility = View.VISIBLE
                gridLayout.visibility = View.VISIBLE
                
                // Force re-initialization of adapter
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        // Clear existing views
                        adapter.clearAllViews()
                        
                        // Reload items
                        loadHomeScreenItems()
                        
                        // Re-apply adapter settings
                        if (::adapter.isInitialized) {
                            adapter.setButtonMode(modeManager.isButtonMode())
                            // Force layout update
                            gridLayout.requestLayout()
                            gridLayout.invalidate()
                        }
                    }
                }
            }
        }
        lastShowHomeScreen = currentShowHomeScreen
        
        // Refresh state
        updateVisibility()
        
        // Setup custom key listener for hidden mode toggle
        setupCustomKeyListener()
        
        // Re-check launcher mode only if not default launcher
        if (!isDefaultLauncher()) {
            checkHomeScreenEnabled()
        }
        
        // Check if adapter has items when home screen is enabled
        if (currentShowHomeScreen && adapter.getItemCount() == 0) {
            Log.d(TAG, "No items in adapter but home screen is enabled, reloading...")
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    adapter.clearAllViews()
                    loadHomeScreenItems()
                    gridLayout.requestLayout()
                    gridLayout.invalidate()
                }
            }
        }
        
        // Reload grid configuration if it changed
        val newGridConfig = GridConfiguration.fromPreferences(preferences)
        if (gridLayout.getGridConfiguration() != newGridConfig) {
            Log.d(TAG, "Grid configuration changed, reloading...")
            gridLayout.setGridConfiguration(newGridConfig)
            gridLayout.setButtonPhoneMode(newGridConfig.isButtonMode)
            handleGridResize()
        }
        
        // Update menu button visibility in case settings changed
        updateMenuButtonVisibility()
        
        // Refresh button mode state
        if (::adapter.isInitialized) {
            adapter.setButtonMode(modeManager.isButtonMode())
            if (modeManager.isButtonMode()) {
                // Request focus if in button mode
                focusManager.requestInitialFocus()
            }
            
            // Check if adapter is empty and reload items if needed
            if (adapter.getItemCount() == 0) {
                Log.d(TAG, "No items on home screen, reloading...")
                loadHomeScreenItems()
            }
        }
    }
    
    override fun onStop() {
        super.onStop()
        // Stop listening for widget updates when activity is not visible
        appWidgetHost.stopListening()
    }
    
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "Configuration changed: ${newConfig.orientation}")
        
        // Reload grid configuration if needed
        val gridConfig = GridConfiguration.fromPreferences(preferences)
        gridLayout.setGridConfiguration(gridConfig)
        
        // Reload items to adjust to new layout
        loadHomeScreenItems()
    }
    
    private fun setupCustomKeyListener() {
        // Clean up old listener first
        customKeyListener?.destroy()
        customKeyListener = null
        
        if (preferences.useCustomKeys) {
            val customKeysString = preferences.customKeyCombination
            if (!customKeysString.isNullOrEmpty()) {
                val keys = customKeysString.split(",").mapNotNull { it.toIntOrNull() }
                if (keys.isNotEmpty()) {
                    val combination = CustomKeyCombination(keys)
                    customKeyListener = CustomKeyListener {
                        Log.d(TAG, "Custom key combination triggered")
                        toggleHiddenMode()
                    }
                    customKeyListener?.setCombination(combination)
                    Log.d(TAG, "Custom key listener setup with keys: $keys")
                }
            }
        }
    }
    
    private fun toggleHiddenMode() {
        // Toggle hidden mode
        val currentState = HiddenModeStateManager.currentState
        val newState = !currentState
        HiddenModeStateManager.setHiddenMode(this, newState)
        
        // Update UI immediately
        updateVisibility(newState)
        
        Log.d(TAG, "Hidden mode toggled to: $newState")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Clean up focus manager
        focusManager.cleanup()
        
        // Clean up custom key listener
        customKeyListener?.destroy()
        customKeyListener = null
        
        try {
            unregisterReceiver(hiddenModeReceiver)
        } catch (e: Exception) {
            Log.d(TAG, "Hidden mode receiver already unregistered")
        }
        
        try {
            unregisterReceiver(gridConfigReceiver)
        } catch (e: Exception) {
            Log.d(TAG, "Grid config receiver already unregistered")
        }
        
        try {
            unregisterReceiver(modeChangeReceiver)
        } catch (e: Exception) {
            Log.d(TAG, "Mode change receiver already unregistered")
        }
        
        try {
            unregisterReceiver(menuMethodChangeReceiver)
        } catch (e: Exception) {
            Log.d(TAG, "Menu method change receiver already unregistered")
        }
        
        try {
            unregisterReceiver(homeScreenVisibilityReceiver)
        } catch (e: Exception) {
            Log.d(TAG, "Home screen visibility receiver already unregistered")
        }
        
        try {
            unregisterReceiver(packageChangeReceiver)
        } catch (e: Exception) {
            Log.d(TAG, "Package change receiver already unregistered")
        }
        
        try {
            unregisterReceiver(iconPackChangeReceiver)
        } catch (e: Exception) {
            Log.d(TAG, "Icon pack change receiver already unregistered")
        }
        
        if (::adapter.isInitialized) {
            adapter.onDestroy()
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        
        // Handle home button press
        if (Intent.ACTION_MAIN == intent.action) {
            val categories = intent.categories
            if (categories != null && categories.contains(Intent.CATEGORY_HOME)) {
                // User pressed home button
                Log.d(TAG, "Home button pressed")
                
                // Check if home screen is empty
                if (adapter.getItemCount() == 0) {
                    Log.d(TAG, "Home screen empty after returning, loading items...")
                    loadHomeScreenItems()
                }
            }
        }
    }
    
    private fun handleAppDroppedFromDrawer(app: com.customlauncher.app.data.model.AppInfo, x: Int, y: Int) {
        // Check if drop is in bottom bar area
        if (canAddToBottomBar(x.toFloat(), y.toFloat())) {
            addIconToBottomBar(app, x.toFloat())
            return
        }
        
        // Create a new home item from the app for normal grid
        val newItem = HomeItemModel.createAppShortcut(
            packageName = app.packageName,
            componentName = "${app.packageName}/.MainActivity", // Will be resolved properly
            label = app.appName,
            x = x,
            y = y
        )
        
        // Add to database
        lifecycleScope.launch(Dispatchers.IO) {
            val itemId = repository.addItem(newItem)
            Log.d(TAG, "Added app ${app.appName} to home screen at $x, $y with id $itemId")
            
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeScreenActivity, "${app.appName} добавлено на главный экран", Toast.LENGTH_SHORT).show()
                // Reload items to refresh the display
                loadHomeScreenItems()
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            REQUEST_PICK_WIDGET -> {
                if (resultCode == RESULT_OK && data != null) {
                    val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
                    if (appWidgetId != -1) {
                        addWidgetToHomeScreen(appWidgetId)
                    }
                } else if (pendingWidgetId != -1) {
                    // User cancelled, clean up
                    appWidgetHost.deleteAppWidgetId(pendingWidgetId)
                    pendingWidgetId = -1
                }
            }
            REQUEST_CREATE_WIDGET -> {
                if (resultCode == RESULT_OK && pendingWidgetId != -1) {
                    // Permission granted, configure widget
                    val widgetInfo = appWidgetManager.getAppWidgetInfo(pendingWidgetId)
                    if (widgetInfo != null) {
                        configureWidget(pendingWidgetId, widgetInfo)
                    }
                } else if (pendingWidgetId != -1) {
                    // Permission denied, clean up
                    appWidgetHost.deleteAppWidgetId(pendingWidgetId)
                    pendingWidgetId = -1
                    Toast.makeText(this, "Не удалось добавить виджет", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun addWidgetToHomeScreen(appWidgetId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val widgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)
                if (widgetInfo == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeScreenActivity, "Ошибка добавления виджета", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                
                // Calculate widget size in cells
                val gridConfig = GridConfiguration.fromPreferences(preferences)
                val displayMetrics = resources.displayMetrics
                val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
                val screenHeightDp = displayMetrics.heightPixels / displayMetrics.density
                
                // Calculate cell size in dp
                val cellWidthDp = screenWidthDp / gridConfig.columns
                val cellHeightDp = screenHeightDp / gridConfig.rows
                
                // Calculate optimal span based on widget's min size
                // Add some padding for better appearance
                val paddingDp = 8 // 8dp padding around widget content
                val spanX = kotlin.math.max(1, kotlin.math.ceil((widgetInfo.minWidth + paddingDp) / cellWidthDp).toInt())
                val spanY = kotlin.math.max(1, kotlin.math.ceil((widgetInfo.minHeight + paddingDp) / cellHeightDp).toInt())
                
                // Ensure minimum spans for common widgets
                // Log widget info for debugging
                Log.d(TAG, "Widget provider: ${widgetInfo.provider}, className: ${widgetInfo.provider.className}, packageName: ${widgetInfo.provider.packageName}")
                Log.d(TAG, "Widget minWidth: ${widgetInfo.minWidth}, minHeight: ${widgetInfo.minHeight}")
                Log.d(TAG, "Initial calculated span: ${spanX}x${spanY}")
                
                val finalSpanX = when {
                    // Google Clock widgets need proper width
                    widgetInfo.provider.packageName == "com.google.android.deskclock" -> kotlin.math.max(4, spanX)
                    // Android system clock widgets
                    widgetInfo.provider.packageName.contains("android", ignoreCase = true) && 
                    widgetInfo.provider.className.contains("clock", ignoreCase = true) -> kotlin.math.max(3, spanX)
                    // Weather widgets
                    widgetInfo.provider.className.contains("weather", ignoreCase = true) -> kotlin.math.max(3, spanX)
                    // Chrome Dino game needs full width
                    widgetInfo.provider.packageName == "com.chrome.canary" ||
                    widgetInfo.provider.packageName == "com.android.chrome" ||
                    widgetInfo.provider.className.contains("dino", ignoreCase = true) -> {
                        // Chrome Dino needs nearly full width
                        kotlin.math.min(gridConfig.columns, kotlin.math.max(4, spanX))
                    }
                    else -> spanX
                }
                
                val finalSpanY = when {
                    // Google Clock needs at least 2 rows
                    widgetInfo.provider.packageName == "com.google.android.deskclock" -> kotlin.math.max(2, spanY)
                    // Chrome Dino needs proper height for the game
                    widgetInfo.provider.packageName == "com.chrome.canary" ||
                    widgetInfo.provider.packageName == "com.android.chrome" ||
                    widgetInfo.provider.className.contains("dino", ignoreCase = true) -> kotlin.math.max(3, spanY)
                    // Calendar widgets need more height
                    widgetInfo.provider.className.contains("calendar", ignoreCase = true) -> kotlin.math.max(3, spanY)
                    else -> spanY
                }
                
                Log.d(TAG, "Final calculated span: ${finalSpanX}x${finalSpanY}")
                
                // Find empty spot for widget
                val existingItems = repository.getAllItems()
                var placed = false
                
                for (y in 0 until gridConfig.rows - finalSpanY + 1) {
                    for (x in 0 until gridConfig.columns - finalSpanX + 1) {
                        val positionFree = !existingItems.any { item ->
                            val itemEndX = item.cellX + item.spanX - 1
                            val itemEndY = item.cellY + item.spanY - 1
                            val widgetEndX = x + finalSpanX - 1
                            val widgetEndY = y + finalSpanY - 1
                            
                            // Check for overlap
                            !(x > itemEndX || widgetEndX < item.cellX || y > itemEndY || widgetEndY < item.cellY)
                        }
                        
                        if (positionFree) {
                            val widgetItem = HomeItemModel(
                                id = 0, // Will be auto-generated
                                type = HomeItemModel.ItemType.WIDGET,
                                widgetId = appWidgetId,
                                componentName = widgetInfo.provider.flattenToString(),
                                cellX = x,
                                cellY = y,
                                spanX = finalSpanX,
                                spanY = finalSpanY
                            )
                            
                            val id = repository.addItem(widgetItem)
                            placed = true
                            
                            withContext(Dispatchers.Main) {
                                Log.d(TAG, "Widget added at $x, $y with size ${finalSpanX}x${finalSpanY}")
                                Toast.makeText(this@HomeScreenActivity, "Виджет добавлен", Toast.LENGTH_SHORT).show()
                                loadHomeScreenItems()
                            }
                            break
                        }
                    }
                    if (placed) break
                }
                
                if (!placed) {
                    // No space available
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeScreenActivity, "Нет места для виджета", Toast.LENGTH_SHORT).show()
                        appWidgetHost.deleteAppWidgetId(appWidgetId)
                    }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error adding widget to home screen", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeScreenActivity, "Ошибка добавления виджета", Toast.LENGTH_SHORT).show()
                    appWidgetHost.deleteAppWidgetId(appWidgetId)
                }
            }
        }
    }
    
    /**
     * Start widget resize mode
     */
    private fun startWidgetResize(item: HomeItemModel, widgetView: View) {
        // Initialize resize manager if needed
        if (widgetResizeManager == null) {
            val gridConfig = GridConfiguration.fromPreferences(preferences)
            widgetResizeManager = WidgetResizeManager(
                this,
                binding.gridContainer,
                gridConfig,
                onResizeComplete = { resizedItem, newSpanX, newSpanY ->
                    // Handle resize complete - save to database
                    onWidgetResized(resizedItem, newSpanX, newSpanY)
                },
                onResizeUpdate = { resizedItem, newSpanX, newSpanY ->
                    // Handle resize update - update UI in real-time
                    onWidgetResizeUpdate(resizedItem, newSpanX, newSpanY)
                }
            )
        }
        
        // Start resize mode
        widgetResizeManager?.startResize(widgetView, item)
    }
    
    /**
     * Handle widget resize completion
     */
    private fun onWidgetResized(item: HomeItemModel, newSpanX: Int, newSpanY: Int) {
        lifecycleScope.launch {
            // Update item in database
            val updatedItem = item.copy(
                spanX = newSpanX,
                spanY = newSpanY
            )
            
            withContext(Dispatchers.IO) {
                repository.updateItem(updatedItem)
            }
            
            Toast.makeText(
                this@HomeScreenActivity,
                "Размер виджета изменен: ${newSpanX}x${newSpanY}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    /**
     * Handle widget resize update in real-time
     */
    private fun onWidgetResizeUpdate(item: HomeItemModel, newSpanX: Int, newSpanY: Int) {
        Log.d(TAG, "onWidgetResizeUpdate: Updating widget ${item.id} to ${newSpanX}x${newSpanY}")
        
        // Update adapter to show new size immediately
        val updatedItem = item.copy(
            spanX = newSpanX,
            spanY = newSpanY
        )
        adapter.updateItem(updatedItem)
    }
}

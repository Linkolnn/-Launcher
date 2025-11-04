package com.customlauncher.app.ui.dialog

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.R
import com.customlauncher.app.data.model.AppInfo
import com.customlauncher.app.databinding.BottomSheetAppDrawerBinding
import com.customlauncher.app.manager.HiddenModeStateManager
import com.customlauncher.app.manager.HomeScreenModeManager
import com.customlauncher.app.ui.AppListActivity
import com.customlauncher.app.ui.HomeScreenActivity
import com.customlauncher.app.ui.adapter.AppGridAdapter
import com.customlauncher.app.ui.layout.PaginatedGridLayoutManager
import com.customlauncher.app.ui.viewmodel.AppViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.delay

class AppDrawerBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "AppDrawerBottomSheet"
        private const val FOCUS_TIMEOUT_MS = 10000L // 10 seconds
    }

    private lateinit var binding: BottomSheetAppDrawerBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var appAdapter: AppGridAdapter
    private lateinit var searchTextWatcher: TextWatcher
    private val preferences by lazy { LauncherApplication.instance.preferences }
    private val currentHiddenState: Boolean
        get() = HiddenModeStateManager.currentState
    
    // D-pad navigation support
    private lateinit var modeManager: HomeScreenModeManager
    private var focusedPosition: Int = 0
    private var isButtonMode: Boolean = false
    private var currentPopupWindow: PopupWindow? = null
    
    // Focus timeout handler
    private val focusTimeoutHandler = Handler(Looper.getMainLooper())
    private val focusTimeoutRunnable = Runnable {
        Log.d(TAG, "Focus timeout - hiding focus after $FOCUS_TIMEOUT_MS ms of inactivity")
        clearFocus()
    }

    // BroadcastReceiver for package changes
    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            val packageName = intent?.data?.schemeSpecificPart
            
            Log.d(TAG, "Package change detected: $action for $packageName")
            
            when (action) {
                Intent.ACTION_PACKAGE_REMOVED -> {
                    // Check if package is being replaced
                    val replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
                    if (!replacing) {
                        Log.d(TAG, "Package removed: $packageName")
                        // Use special handler for package removal
                        packageName?.let { viewModel.onPackageRemoved(it) }
                    }
                }
                Intent.ACTION_PACKAGE_ADDED,
                Intent.ACTION_PACKAGE_REPLACED,
                Intent.ACTION_PACKAGE_CHANGED -> {
                    Log.d(TAG, "Package changed: $action - $packageName")
                    // Invalidate cache and reload apps
                    viewModel.invalidateCache()
                    viewModel.loadApps()
                }
            }
        }
    }
    
    // System dialogs close receiver
    private val systemDialogsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                // Block closing if home screen is disabled
                val preferences = LauncherApplication.instance.preferences
                if (!preferences.showHomeScreen) {
                    Log.d(TAG, "Blocking ACTION_CLOSE_SYSTEM_DIALOGS - home screen disabled")
                    abortBroadcast()
                }
            }
        }
    }
    
    // Receiver for explicit close drawer command (from hidden mode)
    private val closeDrawerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.customlauncher.CLOSE_APP_DRAWER") {
                // Only close if we're actually entering hidden mode and close apps is enabled
                val isEnteringHiddenMode = HiddenModeStateManager.currentState
                val shouldCloseApps = preferences.closeAppsOnHiddenMode
                
                Log.d(TAG, "Received close drawer broadcast - Hidden mode: $isEnteringHiddenMode, Should close: $shouldCloseApps")
                
                if (isEnteringHiddenMode && shouldCloseApps) {
                    dismiss()
                } else {
                    Log.d(TAG, "Ignoring close drawer broadcast - conditions not met")
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as com.google.android.material.bottomsheet.BottomSheetDialog
        
        // Disable dismissal if home screen is disabled
        if (!preferences.showHomeScreen) {
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
        
        // Configure window for proper transparency
        dialog.window?.let { window ->
            // Keep dim but make it more subtle
            window.setDimAmount(0.5f)
            // Set transparent background to see our custom rounded background
            window.setBackgroundDrawableResource(android.R.color.transparent)
            
            // Set status bar color to match the bottom sheet background
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.background_dark)
                
                // Set navigation bar color to match as well
                window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.background_dark)
            }
            
            // Configure system UI visibility flags
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                var flags = window.decorView.systemUiVisibility
                
                // For Android 6.0+ ensure status bar icons are light (white)
                @Suppress("DEPRECATION")
                flags = flags and android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                
                // For Android 8.0+ ensure navigation bar icons are light (white)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    @Suppress("DEPRECATION")
                    flags = flags and android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                }
                
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = flags
            }
        }
        
        // Apply transparent background to bottom sheet container
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as com.google.android.material.bottomsheet.BottomSheetDialog
            
            // Find the bottom sheet frame layout and make it transparent
            val bottomSheetInternal = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.let { sheet ->
                // Remove the default white background
                sheet.setBackgroundResource(android.R.color.transparent)
                
                // Get parent to also make it transparent
                (sheet.parent as? View)?.setBackgroundResource(android.R.color.transparent)
            }
            
            // Find coordinator layout and make it transparent too
            val coordinator = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.coordinator)
            coordinator?.setBackgroundResource(android.R.color.transparent)
            
            // Find container and make it transparent
            val container = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.container)
            container?.setBackgroundResource(android.R.color.transparent)
        }
        
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        // Prevent cancellation when home screen is disabled
        if (!preferences.showHomeScreen) {
            Log.d(TAG, "Dialog cancel blocked - home screen is disabled")
            return
        }
        super.onCancel(dialog)
    }
    
    override fun onDismiss(dialog: DialogInterface) {
        // Log the dismiss reason for debugging Android 16 issue
        val hiddenModeState = HiddenModeStateManager.currentState
        Log.d(TAG, "onDismiss called - Hidden mode: $hiddenModeState, Show home: ${preferences.showHomeScreen}")
        
        // Get stack trace to find what triggered dismiss
        if (Build.VERSION.SDK_INT >= 35) { // Android 16
            Log.d(TAG, "Dismiss stack trace: ${Thread.currentThread().stackTrace.take(5).joinToString("\n")}")
        }
        
        // Prevent dismissal when home screen is disabled
        if (!preferences.showHomeScreen) {
            Log.d(TAG, "Dialog dismiss blocked - home screen is disabled")
            return
        }
        
        // Notify HomeScreenActivity that the drawer is closed
        val homeScreen = activity as? HomeScreenActivity
        if (homeScreen != null) {
            homeScreen.onAppDrawerClosed()
        }
        
        super.onDismiss(dialog)
        
        // Reset status and navigation bar colors when dismissing
        activity?.window?.let { window ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Make status bar transparent again for the launcher
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                
                // Make navigation bar transparent as well
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
                
                // Reset the system UI visibility to the launcher's default
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    @Suppress("DEPRECATION")
                    window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                }
            }
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Set up the BottomSheet to be full screen
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0
                behavior.skipCollapsed = true
                
                // If home screen is disabled, make bottom sheet non-draggable and fullscreen
                if (!preferences.showHomeScreen) {
                    behavior.isDraggable = false
                    behavior.isHideable = false
                    
                    // Set height to full screen including status bar
                    val layoutParams = it.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    it.layoutParams = layoutParams
                    
                    // Hide drag handle when home screen is disabled
                    binding.dragHandle.visibility = View.GONE
                } else {
                    // Normal height for regular mode
                    val layoutParams = it.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    it.layoutParams = layoutParams
                }
            }
        }
        
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        
        // Initialize mode manager
        modeManager = HomeScreenModeManager(requireContext())
        isButtonMode = modeManager.isButtonMode()
        
        // Hide drag handle in button mode (if not already hidden)
        if ((isButtonMode || (preferences.hasButtonGridSelection && preferences.buttonPhoneGridSize.isNotEmpty())) && preferences.showHomeScreen) {
            binding.dragHandle.visibility = View.GONE
        }
        
        setupRecyclerView()
        setupListeners()
        observeViewModel()
        
        // Setup D-pad navigation for both touch and button modes (for keyboard users)
        setupDpadNavigation()
        
        // Setup search if enabled
        updateSearchVisibility()
        
        // Register package change receiver - directly register system events for Android 16 compatibility
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }
        
        // Use RECEIVER_EXPORTED for Android 16 to ensure we receive system broadcasts
        if (Build.VERSION.SDK_INT >= 34) { // Android 14+
            requireContext().registerReceiver(packageChangeReceiver, filter, Context.RECEIVER_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(packageChangeReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            requireContext().registerReceiver(packageChangeReceiver, filter)
        }
        
        // Register system dialogs receiver to prevent closing when home screen is disabled
        if (!preferences.showHomeScreen) {
            val systemFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            systemFilter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireContext().registerReceiver(systemDialogsReceiver, systemFilter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                requireContext().registerReceiver(systemDialogsReceiver, systemFilter)
            }
        }
        
        // Register close drawer receiver for hidden mode
        val closeDrawerFilter = IntentFilter("com.customlauncher.CLOSE_APP_DRAWER")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(closeDrawerReceiver, closeDrawerFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            requireContext().registerReceiver(closeDrawerReceiver, closeDrawerFilter)
        }
    }

    private fun setupRecyclerView() {
        appAdapter = AppGridAdapter(
            onAppClick = { app -> launchApp(app) },
            onAppLongClick = { app, view -> showAppContextMenu(app, view) },
            isDragEnabled = true,
            onDragStarted = { 
                // Close the bottom sheet when drag starts only if home screen is enabled
                if (preferences.showHomeScreen) {
                    dismiss()
                }
            }
        )
        
        binding.appsGrid.apply {
            adapter = appAdapter
            updateGridLayoutManager()
            
            // Настройка плавной анимации для изменений списка
            itemAnimator?.apply {
                addDuration = 200
                removeDuration = 200
                moveDuration = 250
                changeDuration = 250
            }
            
            // Enable drag & drop to home screen
            setOnDragListener { _, event ->
                when (event.action) {
                    DragEvent.ACTION_DROP -> {
                        // Let HomeScreenActivity handle the drop
                        false
                    }
                    else -> true
                }
            }
        }
    }

    private fun setupListeners() {
        // Setup search if enabled
        if (preferences.showAppSearch) {
            binding.searchContainer.visibility = View.VISIBLE
            setupSearchView()
        } else {
            binding.searchContainer.visibility = View.GONE
        }
        
        // Setup drag handle for dismissing (only if home screen is enabled)
        binding.dragHandle.setOnClickListener {
            if (preferences.showHomeScreen) {
                dismiss()
            }
        }
    }

    private fun setupSearchView() {
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                if (query.isNotEmpty()) {
                    binding.clearSearchButton.visibility = View.VISIBLE
                    appAdapter.filter(query)
                } else {
                    binding.clearSearchButton.visibility = View.GONE
                    appAdapter.filter("")
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        }
        binding.searchEditText.addTextChangedListener(searchTextWatcher)
        
        binding.clearSearchButton.setOnClickListener {
            binding.searchEditText.text.clear()
        }
        
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Hide keyboard
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        // Keep track of current hidden state for updates
        var currentHiddenState = HiddenModeStateManager.currentState
        
        viewLifecycleOwner.lifecycleScope.launch {
            HiddenModeStateManager.isHiddenMode.collectLatest { isHidden ->
                Log.d(TAG, "Hidden mode state changed: $isHidden")
                
                // При изменении скрытого режима в режиме кнопочных телефонов 
                // переходим на первую страницу, чтобы избежать пустых страниц
                if (isHidden != currentHiddenState) {
                    val isButtonMode = preferences.homeScreenMode == HomeScreenModeManager.MODE_BUTTON
                    val hasButtonGridSelection = preferences.hasButtonGridSelection && preferences.buttonPhoneGridSize.isNotEmpty()
                    
                    if (isButtonMode || hasButtonGridSelection) {
                        // Переключаемся на первую страницу при изменении скрытого режима
                        binding.appsGrid.post {
                            val layoutManager = binding.appsGrid.layoutManager
                            
                            if (layoutManager is PaginatedGridLayoutManager) {
                                layoutManager.navigateToPosition(0)
                                focusedPosition = 0
                                updateItemFocus(0, true)
                                val modeStatus = if (isHidden) "entering" else "exiting"
                                Log.d(TAG, "Reset to first page due to $modeStatus hidden mode")
                            } else {
                                // Для обычного GridLayoutManager просто скроллим к началу
                                binding.appsGrid.scrollToPosition(0)
                                focusedPosition = 0
                                updateItemFocus(0, true)
                            }
                        }
                    }
                    
                    currentHiddenState = isHidden
                    
                    // Используем плавное обновление списка вместо полной перезагрузки
                    // Добавляем небольшую задержку для более плавной анимации
                    binding.appsGrid.postDelayed({
                        updateAppsList()
                    }, 100)
                } else {
                    // Если состояние не изменилось, просто перезагружаем
                    viewModel.loadApps()
                }
            }
        }
        
        // Observe both lists and update based on current state
        viewModel.visibleApps.observe(viewLifecycleOwner) { visibleApps ->
            if (preferences.hideAppsInHiddenMode && HiddenModeStateManager.currentState) {
                Log.d(TAG, "Updating with visible apps: ${visibleApps.size}")
                // Используем callback для плавной анимации
                appAdapter.submitList(visibleApps) {
                    // Список обновлен
                    Log.d(TAG, "Visible apps list updated with animation")
                }
            }
        }
        
        viewModel.allApps.observe(viewLifecycleOwner) { apps ->
            if (!preferences.hideAppsInHiddenMode || !HiddenModeStateManager.currentState) {
                Log.d(TAG, "Updating with all apps: ${apps.size}")
                // Используем callback для плавной анимации
                appAdapter.submitList(apps) {
                    // Список обновлен
                    Log.d(TAG, "All apps list updated with animation")
                }
            }
        }
    }
    
    private fun updateAppsList() {
        // Force update the list based on current hidden state
        if (preferences.hideAppsInHiddenMode && HiddenModeStateManager.currentState) {
            viewModel.visibleApps.value?.let { visibleApps ->
                Log.d(TAG, "Forcing update with visible apps: ${visibleApps.size}")
                appAdapter.submitList(visibleApps) {
                    Log.d(TAG, "Forced visible apps update complete")
                }
            }
        } else {
            viewModel.allApps.value?.let { apps ->
                Log.d(TAG, "Forcing update with all apps: ${apps.size}")
                appAdapter.submitList(apps) {
                    Log.d(TAG, "Forced all apps update complete")
                }
            }
        }
    }

    private fun updateGridLayoutManager() {
        // Check if button grid is selected for app menu
        val useButtonGrid = preferences.hasButtonGridSelection && preferences.buttonPhoneGridSize.isNotEmpty()
        
        val columns = if (useButtonGrid) {
            val gridSize = preferences.buttonPhoneGridSize
            when (gridSize) {
                "3x3" -> 3
                "3x4" -> 3
                "3x5" -> 3
                "4x5" -> 4
                else -> 3  // Default to 3 columns
            }
        } else {
            preferences.gridColumnCount.takeIf { it > 0 } ?: 4
        }
        
        if (useButtonGrid) {
            // Use paginated layout for button phones menu
            val rows = when (preferences.buttonPhoneGridSize) {
                "3x3" -> 3
                "3x4" -> 4
                "3x5" -> 5
                "4x5" -> 5
                else -> 4  // Default to 4 rows
            }
            binding.appsGrid.layoutManager = PaginatedGridLayoutManager(
                requireContext(), 
                columns, 
                rows
            )
        } else {
            // Use regular grid for touch phones
            binding.appsGrid.layoutManager = GridLayoutManager(requireContext(), columns)
        }
    }

    private fun setupDpadNavigation() {
        // Set initial focus on the first item
        binding.appsGrid.post {
            val layoutManager = binding.appsGrid.layoutManager
            val itemCount = appAdapter.itemCount
            
            if (layoutManager != null && itemCount > 0) {
                // Set initial focus to first item
                focusedPosition = 0
                updateItemFocus(0, true)
                
                // If using paginated layout, ensure it's on the first page
                if (layoutManager is PaginatedGridLayoutManager) {
                    layoutManager.navigateToPosition(0)
                    Log.d(TAG, "Initialized paginated layout at position 0")
                }
                
                // Request focus on the first view
                val firstView = layoutManager.findViewByPosition(0)
                firstView?.requestFocus()
                
                Log.d(TAG, "D-pad navigation setup complete, focused on position 0")
            }
        }
        
        // Setup key listener for the dialog
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                // Handle BACK key specially when home screen is disabled
                if (keyCode == KeyEvent.KEYCODE_BACK && !preferences.showHomeScreen) {
                    Log.d(TAG, "Back key pressed but home screen is disabled, ignoring")
                    return@setOnKeyListener true // Consume the event to prevent dismissal
                }
                handleDpadKeyEvent(keyCode)
            } else {
                false
            }
        }
    }
    
    private fun handleDpadKeyEvent(keyCode: Int): Boolean {
        val layoutManager = binding.appsGrid.layoutManager
        val itemCount = appAdapter.itemCount
        
        if (itemCount == 0) return false
        
        // Restart focus timer on any D-pad interaction
        restartFocusTimer()
        
        // Check if we're using paginated layout
        if (layoutManager is PaginatedGridLayoutManager) {
            return handlePaginatedDpadKeyEvent(keyCode, layoutManager)
        }
        
        // Regular grid layout handling
        val gridLayoutManager = layoutManager as? GridLayoutManager ?: return false
        val columnCount = gridLayoutManager.spanCount
        
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                // Block BACK key when home screen is disabled
                if (!preferences.showHomeScreen) {
                    Log.d(TAG, "Back key blocked - home screen is disabled")
                    true // Consume the event
                } else {
                    false // Let it propagate normally
                }
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                // Check if we're at the top row
                if (focusedPosition < columnCount) {
                    // We're at the top, close the bottom sheet only if home screen is enabled
                    if (preferences.showHomeScreen) {
                        Log.d(TAG, "Reached top of list, closing drawer")
                        dismiss()
                    } else {
                        Log.d(TAG, "At top of list, but home screen is disabled")
                    }
                    true
                } else {
                    // Move up one row
                    val newPosition = focusedPosition - columnCount
                    if (newPosition >= 0) {
                        moveFocusToPosition(newPosition)
                    }
                    true
                }
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                // Move down one row
                val newPosition = focusedPosition + columnCount
                if (newPosition < itemCount) {
                    moveFocusToPosition(newPosition)
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                // Move left
                if (focusedPosition > 0) {
                    moveFocusToPosition(focusedPosition - 1)
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                // Move right
                if (focusedPosition < itemCount - 1) {
                    moveFocusToPosition(focusedPosition + 1)
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                // Launch the focused app
                val app = appAdapter.getItemAt(focusedPosition)
                app?.let {
                    launchApp(it)
                }
                true
            }
            else -> false
        }
    }
    
    private fun handlePaginatedDpadKeyEvent(keyCode: Int, layoutManager: PaginatedGridLayoutManager): Boolean {
        val itemCount = appAdapter.itemCount
        val columns = layoutManager.columns
        val rows = layoutManager.rows
        val itemsPerPage = columns * rows
        
        // Calculate current page and position within page
        val currentPage = focusedPosition / itemsPerPage
        val positionInPage = focusedPosition % itemsPerPage
        val currentRow = positionInPage / columns
        val currentCol = positionInPage % columns
        
        val totalPages = (itemCount + itemsPerPage - 1) / itemsPerPage
        Log.d(TAG, "Paginated D-pad: key=$keyCode, pos=$focusedPosition, page=$currentPage/$totalPages, row=$currentRow, col=$currentCol, items=$itemCount")
        Log.d(TAG, "Layout Manager state: ${layoutManager.getCurrentPageInfo()}")
        
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (currentRow == 0) {
                    // At top row, close the drawer only if home screen is enabled
                    if (preferences.showHomeScreen) {
                        Log.d(TAG, "Reached top of page, closing drawer")
                        dismiss()
                    } else {
                        Log.d(TAG, "At top of page, but home screen is disabled")
                    }
                    true
                } else {
                    // Move up one row
                    val newPosition = focusedPosition - columns
                    moveFocusToPosition(newPosition)
                    true
                }
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (currentRow < rows - 1) {
                    // Move down one row if not at bottom
                    val newPosition = focusedPosition + columns
                    if (newPosition < itemCount) {
                        moveFocusToPosition(newPosition)
                    }
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (currentCol == 0) {
                    // At left edge, try to go to previous page
                    if (currentPage > 0) {
                        Log.d(TAG, "Moving to previous page from position $focusedPosition")
                        // Calculate new position on previous page
                        val newPage = currentPage - 1
                        val newPosition = (newPage * itemsPerPage) + (currentRow * columns) + (columns - 1)
                        
                        // Ensure position is valid
                        val validPosition = minOf(newPosition, itemCount - 1)
                        
                        // Update layout manager's page
                        layoutManager.navigateToPosition(validPosition)
                        
                        // Update our focus
                        moveFocusToPosition(validPosition)
                        
                        // Force layout refresh
                        binding.appsGrid.requestLayout()
                        Log.d(TAG, "Moved to position $validPosition on page $newPage")
                    } else {
                        Log.d(TAG, "Already on first page")
                    }
                } else {
                    // Move left within current page
                    moveFocusToPosition(focusedPosition - 1)
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (currentCol == columns - 1) {
                    // At right edge, try to go to next page
                    val totalPages = (itemCount + itemsPerPage - 1) / itemsPerPage
                    if (currentPage < totalPages - 1) {
                        Log.d(TAG, "Moving to next page from position $focusedPosition")
                        // Calculate new position on next page
                        val newPage = currentPage + 1
                        val newPosition = (newPage * itemsPerPage) + (currentRow * columns)
                        
                        // Check if position exists
                        if (newPosition < itemCount) {
                            // Update layout manager's page
                            layoutManager.navigateToPosition(newPosition)
                            
                            // Update our focus
                            moveFocusToPosition(newPosition)
                            
                            // Force layout refresh
                            binding.appsGrid.requestLayout()
                            Log.d(TAG, "Moved to position $newPosition on page $newPage")
                        } else {
                            Log.d(TAG, "No more items on next page")
                        }
                    } else {
                        Log.d(TAG, "Already on last page")
                    }
                } else {
                    // Move right within current page
                    if (focusedPosition < itemCount - 1) {
                        moveFocusToPosition(focusedPosition + 1)
                    }
                }
                true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                // Launch the focused app
                val app = appAdapter.getItemAt(focusedPosition)
                app?.let {
                    launchApp(it)
                }
                true
            }
            else -> false
        }
    }
    
    private fun scrollToPage(page: Int, layoutManager: PaginatedGridLayoutManager) {
        val itemsPerPage = layoutManager.columns * layoutManager.rows
        val firstItemOfPage = page * itemsPerPage
        
        Log.d(TAG, "Scrolling to page $page, first item: $firstItemOfPage")
        
        binding.appsGrid.post {
            binding.appsGrid.scrollToPosition(firstItemOfPage)
            // Force layout manager to update visible items
            layoutManager.scrollToPosition(firstItemOfPage)
        }
    }
    
    private fun moveFocusToPosition(position: Int) {
        // Clear previous focus
        updateItemFocus(focusedPosition, false)
        
        // Set new focus
        focusedPosition = position
        updateItemFocus(position, true)
        
        // Ensure item is visible
        val layoutManager = binding.appsGrid.layoutManager
        if (layoutManager is PaginatedGridLayoutManager) {
            // For paginated layout, just ensure the position is set
            // The layout manager handles page switching
            Log.d(TAG, "Focus moved to position $position")
        } else {
            // For regular grid, scroll to position
            binding.appsGrid.scrollToPosition(position)
        }
        
        // Request focus on the view
        binding.appsGrid.post {
            val viewHolder = binding.appsGrid.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.requestFocus()
        }
        
        // Restart the focus timeout timer
        restartFocusTimer()
    }
    
    private fun updateItemFocus(position: Int, hasFocus: Boolean = true) {
        binding.appsGrid.post {
            val viewHolder = binding.appsGrid.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.let { view ->
                if (hasFocus) {
                    view.setBackgroundResource(R.drawable.bg_item_focused)
                    view.scaleX = 1.05f
                    view.scaleY = 1.05f
                } else {
                    view.background = null
                    view.scaleX = 1.0f
                    view.scaleY = 1.0f
                }
            }
        }
    }
    
    private fun updateSearchVisibility() {
        if (preferences.showAppSearch) {
            binding.searchContainer.visibility = View.VISIBLE
            if (!this::searchTextWatcher.isInitialized) {
                setupSearchView()
            }
        } else {
            binding.searchContainer.visibility = View.GONE
            binding.searchEditText.text.clear()
        }
    }

    private fun launchApp(app: AppInfo) {
        try {
            // Special handling for the launcher itself
            if (app.packageName == "com.customlauncher.app") {
                // Open the app list activity (the settings/app selection screen)
                val intent = Intent(requireContext(), AppListActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dismiss()
                return
            }
            
            // Normal app launch
            val intent = requireContext().packageManager.getLaunchIntentForPackage(app.packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dismiss()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching app: ${app.packageName}", e)
            Toast.makeText(requireContext(), "Не удалось запустить приложение", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAppContextMenu(app: AppInfo, anchorView: View): Boolean {
        // Dismiss any existing popup
        currentPopupWindow?.dismiss()
        
        // Inflate the popup menu layout
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_app_menu, null)
        
        // Create the popup window
        val popupWindow = PopupWindow(
            popupView,
            resources.getDimensionPixelSize(R.dimen.popup_menu_width),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        
        // Save reference to current popup
        currentPopupWindow = popupWindow
        
        // Set background for proper dismissal
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
        val menuAppInfo = popupView.findViewById<TextView>(R.id.menu_app_info)
        val menuUninstall = popupView.findViewById<TextView>(R.id.menu_uninstall)
        val menuArrow = popupView.findViewById<View>(R.id.menu_arrow)
        
        Log.d(TAG, "Menu views found - AppInfo: ${menuAppInfo != null}, Uninstall: ${menuUninstall != null}")
        
        if (menuAppInfo != null) {
            menuAppInfo.setOnClickListener {
                Log.d(TAG, "App info button clicked")
                popupWindow.dismiss()
                showAppInfo(app)
            }
        } else {
            Log.e(TAG, "menuAppInfo is null!")
        }
        
        if (menuUninstall != null) {
            menuUninstall.setOnClickListener {
                Log.d(TAG, "Uninstall clicked for ${app.appName}")
                // Show debug toast
                Toast.makeText(requireContext(), "Удаляем: ${app.appName}", Toast.LENGTH_SHORT).show()
                
                // Dismiss popup first
                popupWindow.dismiss()
                currentPopupWindow = null
                
                // Validate and uninstall
                if (!app.packageName.isNullOrEmpty() && app.packageName != "null") {
                    uninstallApp(app)
                } else {
                    Log.e(TAG, "Invalid package name: ${app.packageName}")
                    Toast.makeText(requireContext(), "Ошибка: неверное имя пакета", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e(TAG, "menuUninstall is null!")
        }
        
        // Get exact icon position on screen
        val iconLocation = IntArray(2)
        anchorView.getLocationOnScreen(iconLocation)
        
        val popupWidth = resources.getDimensionPixelSize(R.dimen.popup_menu_width)
        val popupHeight = resources.getDimensionPixelSize(R.dimen.popup_menu_height)
        val screenWidth = resources.displayMetrics.widthPixels
        
        // Calculate icon center position
        val iconCenterX = iconLocation[0] + anchorView.width / 2
        val iconTopY = iconLocation[1]
        
        // Calculate where popup should be (centered above icon)
        var popupX = iconCenterX - popupWidth / 2
        
        // Convert dp to pixels for margins
        val density = resources.displayMetrics.density
        // Increased margin between menu and icon (80dp for better spacing)
        val marginAboveIcon = (1 * density).toInt()
        var popupY = iconTopY - popupHeight - marginAboveIcon
        
        // Handle horizontal screen boundaries
        val horizontalMargin = (10 * density).toInt()
        if (popupX < horizontalMargin) {
            popupX = horizontalMargin
        } else if (popupX + popupWidth > screenWidth - horizontalMargin) {
            popupX = screenWidth - popupWidth - horizontalMargin
        }
        
        // Calculate where the arrow should point (relative to popup position)
        val arrowTargetX = iconCenterX - popupX // Where arrow should point relative to popup left edge
        
        // Adjust arrow position dynamically based on actual popup position
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
                    "popupCenterX=$popupCenterX, arrowTargetX=$arrowTargetX")
        } else {
            Log.w(TAG, "Arrow view is null!")
        }
        
        // Handle vertical screen boundaries
        val statusBarHeight = (25 * density).toInt() // Status bar height in pixels
        var isShowingBelow = false
        
        if (popupY < statusBarHeight) {
            // Show below icon if not enough space above
            popupY = iconLocation[1] + anchorView.height + (1 * density).toInt()
            isShowingBelow = true
            
            // Rotate arrow to point upward when showing below
            if (menuArrow != null) {
                // Rotate 180 degrees to point up
                menuArrow.rotation = 180f
                
                // Move arrow to top of popup layout  
                val parent = menuArrow.parent as? ViewGroup
                if (parent != null) {
                    parent.removeView(menuArrow)
                    parent.addView(menuArrow, 0) // Add at beginning for top position
                }
            }
        } else {
            // Arrow points down normally
            menuArrow?.rotation = 0f
        }
        
        Log.d(TAG, "Showing popup at: x=$popupX, y=$popupY, iconCenter=$iconCenterX, iconTop=$iconTopY")
        
        // Show the popup with animation
        try {
            popupWindow.animationStyle = android.R.style.Animation_Dialog
            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, popupY)
        } catch (e: Exception) {
            Log.e(TAG, "Error showing popup", e)
            // Fallback position
            popupWindow.showAsDropDown(anchorView, -popupWidth/2 + anchorView.width/2, -anchorView.height - popupHeight - marginAboveIcon)
        }
        
        return true
    }

    private fun showAppInfo(app: AppInfo) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.parse("package:${app.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun uninstallApp(app: AppInfo) {
        val intent = Intent(Intent.ACTION_DELETE).apply {
            data = android.net.Uri.parse("package:${app.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        currentPopupWindow?.dismiss()
        
        // Stop focus timer
        stopFocusTimer()
        
        // Unregister package change receiver
        try {
            requireContext().unregisterReceiver(packageChangeReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister package receiver", e)
        }
        
        // Unregister system dialogs receiver
        if (!preferences.showHomeScreen) {
            try {
                requireContext().unregisterReceiver(systemDialogsReceiver)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to unregister system dialogs receiver", e)
            }
        }
        
        // Unregister close drawer receiver
        try {
            requireContext().unregisterReceiver(closeDrawerReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister close drawer receiver", e)
        }
    }
    
    /**
     * Clear focus from all items
     */
    private fun clearFocus() {
        if (focusedPosition >= 0) {
            updateItemFocus(focusedPosition, false)
            focusedPosition = -1
            Log.d(TAG, "Focus cleared due to timeout")
        }
    }
    
    /**
     * Restart the focus timer - called on any user interaction
     */
    private fun restartFocusTimer() {
        // Start focus timer for both modes - useful for keyboard navigation
        stopFocusTimer()
        focusTimeoutHandler.postDelayed(focusTimeoutRunnable, FOCUS_TIMEOUT_MS)
        Log.v(TAG, "Focus timer restarted - will hide in ${FOCUS_TIMEOUT_MS}ms")
    }
    
    /**
     * Stop the focus timer
     */
    private fun stopFocusTimer() {
        focusTimeoutHandler.removeCallbacks(focusTimeoutRunnable)
        Log.v(TAG, "Focus timer stopped")
    }
}

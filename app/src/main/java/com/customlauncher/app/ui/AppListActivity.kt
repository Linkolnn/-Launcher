package com.customlauncher.app.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
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
            // Save current selection to both SelectionManager and preferences
            val selectedApps = viewModel.getSelectedApps()
            val packageNames = selectedApps.map { it.packageName }.toSet()
            
            // Update permanent storage
            LauncherApplication.instance.preferences.setHiddenApps(packageNames)
            
            // Save selected apps as hidden in database
            viewModel.saveSelectedAppsAsHidden()
            
            // Go to home screen (press home button programmatically)
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
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
    
    override fun onPause() {
        super.onPause()
        // Save current selection to SelectionManager when leaving activity
        val selectedApps = viewModel.getSelectedApps()
        val packageNames = selectedApps.map { it.packageName }.toSet()
        SelectionManager.setSelection(packageNames)
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

package com.customlauncher.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.data.SelectionManager
import com.customlauncher.app.data.model.AppInfo
import com.customlauncher.app.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AppViewModel : ViewModel() {
    
    private val repository: AppRepository = LauncherApplication.instance.repository
    
    private val _allApps = MutableLiveData<List<AppInfo>>()
    val allApps: LiveData<List<AppInfo>> = _allApps
    
    private val _visibleApps = MutableLiveData<List<AppInfo>>()
    val visibleApps: LiveData<List<AppInfo>> = _visibleApps
    
    private val _hiddenApps = MutableLiveData<List<AppInfo>>()
    val hiddenApps: LiveData<List<AppInfo>> = _hiddenApps
    
    private val _filteredApps = MutableLiveData<List<AppInfo>>()
    val filteredApps: LiveData<List<AppInfo>> = _filteredApps
    
    // Optimization for weak devices
    private val loadMutex = Mutex()
    private var loadJob: Job? = null
    private var saveJob: Job? = null
    
    init {
        loadApps()
    }
    
    fun loadApps() {
        // Cancel previous load job if still running
        loadJob?.cancel()
        
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            loadMutex.withLock {
                try {
                    val apps = repository.getAllInstalledApps()
                    
                    // First check temporary selection, then saved hidden apps
                    val tempSelection = SelectionManager.getSelection()
                    val savedHiddenApps = if (tempSelection.isEmpty()) {
                        LauncherApplication.instance.preferences.getHiddenApps()
                    } else {
                        tempSelection
                    }
                    
                    val appsWithSelection = apps.map { app ->
                        app.copy(isSelected = savedHiddenApps.contains(app.packageName))
                    }
                    
                    // Switch to Main thread for UI updates
                    launch(Dispatchers.Main) {
                        _allApps.value = appsWithSelection
                        _visibleApps.value = appsWithSelection.filter { !it.isHidden }
                        _hiddenApps.value = appsWithSelection.filter { it.isHidden }
                        _filteredApps.value = appsWithSelection
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun searchApps(query: String) {
        val apps = _allApps.value ?: return
        if (query.isEmpty()) {
            _filteredApps.value = apps
        } else {
            _filteredApps.value = apps.filter { app ->
                app.appName.contains(query, ignoreCase = true) || 
                app.packageName.contains(query, ignoreCase = true)
            }
        }
    }
    
    fun toggleAppSelection(app: AppInfo) {
        val newSelection = !app.isSelected
        
        // Update temporary selection
        if (newSelection) {
            SelectionManager.addSelection(app.packageName)
        } else {
            SelectionManager.removeSelection(app.packageName)
        }
        
        // Update filtered apps
        val apps = _filteredApps.value?.map {
            if (it.packageName == app.packageName) {
                it.copy(isSelected = newSelection)
            } else {
                it
            }
        } ?: return
        _filteredApps.value = apps
        
        // Also update in allApps
        val allAppsList = _allApps.value?.map {
            if (it.packageName == app.packageName) {
                it.copy(isSelected = newSelection)
            } else {
                it
            }
        } ?: return
        _allApps.value = allAppsList
    }
    
    fun selectAllApps(select: Boolean) {
        // Update SelectionManager
        if (select) {
            val allPackageNames = _filteredApps.value?.map { it.packageName }?.toSet() ?: emptySet()
            SelectionManager.setSelection(allPackageNames)
        } else {
            SelectionManager.clearSelection()
        }
        
        val apps = _filteredApps.value?.map { it.copy(isSelected = select) } ?: return
        _filteredApps.value = apps
        
        val allAppsList = _allApps.value?.map { it.copy(isSelected = select) } ?: return
        _allApps.value = allAppsList
    }
    
    fun saveSelectedAppsAsHidden() {
        // Cancel previous save job if still running
        saveJob?.cancel()
        
        saveJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val selectedApps = getSelectedApps()
                val selectedPackageNames = selectedApps.map { it.packageName }.toSet()
                
                // Use batch update for better reliability
                val preferences = LauncherApplication.instance.preferences
                preferences.setHiddenApps(selectedPackageNames)
                
                // Clear temporary selection after saving
                SelectionManager.clearSelection()
                
                // Invalidate repository cache
                repository.invalidateCache()
                
                // Small delay for Android 11 to ensure save completes
                delay(100)
                
                // Also update repository for consistency
                val allHiddenApps = repository.getHiddenApps()
                allHiddenApps.forEach { app ->
                    if (!selectedPackageNames.contains(app.packageName)) {
                        repository.unhideApp(app.packageName)
                    }
                }
                selectedPackageNames.forEach { packageName ->
                    repository.hideApp(packageName)
                }
                
                // Keep selection and reload
                val currentSelection = _filteredApps.value?.filter { it.isSelected }?.map { it.packageName } ?: emptyList()
                val apps = repository.getAllInstalledApps()
                val appsWithSelection = apps.map { app ->
                    app.copy(isSelected = currentSelection.contains(app.packageName))
                }
                
                // Update UI on main thread
                launch(Dispatchers.Main) {
                    _allApps.value = appsWithSelection
                    _visibleApps.value = appsWithSelection.filter { !it.isHidden }
                    _hiddenApps.value = appsWithSelection.filter { it.isHidden }
                    _filteredApps.value = appsWithSelection
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun hideSelectedApps() {
        viewModelScope.launch {
            val selectedApps = _allApps.value?.filter { it.isSelected } ?: return@launch
            val packageNames = selectedApps.map { it.packageName }
            repository.hideApps(packageNames)
            loadApps()
        }
    }
    
    fun unhideSelectedApps() {
        viewModelScope.launch {
            val selectedApps = _allApps.value?.filter { it.isSelected } ?: return@launch
            val packageNames = selectedApps.map { it.packageName }
            repository.unhideApps(packageNames)
            loadApps()
        }
    }
    
    fun launchApp(packageName: String) {
        repository.launchApp(packageName)
    }
    
    fun getSelectedApps(): List<AppInfo> {
        return _allApps.value?.filter { it.isSelected } ?: emptyList()
    }
}

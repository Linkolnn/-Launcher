package com.customlauncher.app.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005J\u0006\u0010\u001c\u001a\u00020\u001dJ\u000e\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020 J\u0006\u0010!\u001a\u00020\u001dJ\u0006\u0010\"\u001a\u00020\u001dJ\u000e\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020 J\u000e\u0010%\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\'J\u000e\u0010(\u001a\u00020\u001d2\u0006\u0010)\u001a\u00020\u0006J\u0006\u0010*\u001a\u00020\u001dR\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\r\u00a8\u0006+"}, d2 = {"Lcom/customlauncher/app/ui/viewmodel/AppViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_allApps", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/customlauncher/app/data/model/AppInfo;", "_filteredApps", "_hiddenApps", "_visibleApps", "allApps", "Landroidx/lifecycle/LiveData;", "getAllApps", "()Landroidx/lifecycle/LiveData;", "filteredApps", "getFilteredApps", "hiddenApps", "getHiddenApps", "loadJob", "Lkotlinx/coroutines/Job;", "loadMutex", "Lkotlinx/coroutines/sync/Mutex;", "repository", "Lcom/customlauncher/app/data/repository/AppRepository;", "saveJob", "visibleApps", "getVisibleApps", "getSelectedApps", "hideSelectedApps", "", "launchApp", "packageName", "", "loadApps", "saveSelectedAppsAsHidden", "searchApps", "query", "selectAllApps", "select", "", "toggleAppSelection", "app", "unhideSelectedApps", "app_debug"})
public final class AppViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.repository.AppRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> _allApps = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> allApps = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> _visibleApps = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> visibleApps = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> _hiddenApps = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> hiddenApps = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> _filteredApps = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> filteredApps = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.sync.Mutex loadMutex = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job loadJob;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job saveJob;
    
    public AppViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> getAllApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> getVisibleApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> getHiddenApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.customlauncher.app.data.model.AppInfo>> getFilteredApps() {
        return null;
    }
    
    public final void loadApps() {
    }
    
    public final void searchApps(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    public final void toggleAppSelection(@org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.model.AppInfo app) {
    }
    
    public final void selectAllApps(boolean select) {
    }
    
    public final void saveSelectedAppsAsHidden() {
    }
    
    public final void hideSelectedApps() {
    }
    
    public final void unhideSelectedApps() {
    }
    
    public final void launchApp(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.customlauncher.app.data.model.AppInfo> getSelectedApps() {
        return null;
    }
}
package com.customlauncher.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0016J\u0012\u0010\u000e\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0014J\b\u0010\u0011\u001a\u00020\fH\u0014J\b\u0010\u0012\u001a\u00020\fH\u0002J\b\u0010\u0013\u001a\u00020\fH\u0002J\b\u0010\u0014\u001a\u00020\fH\u0002J\b\u0010\u0015\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/customlauncher/app/ui/AppListActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "appAdapter", "Lcom/customlauncher/app/ui/adapter/AppListAdapter;", "binding", "Lcom/customlauncher/app/databinding/ActivityAppListBinding;", "viewModel", "Lcom/customlauncher/app/ui/viewmodel/AppViewModel;", "checkAllPermissionsGranted", "", "observeViewModel", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "setupListeners", "setupRecyclerView", "updateLaunchButton", "updateSelectAllButton", "app_debug"})
public final class AppListActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.customlauncher.app.databinding.ActivityAppListBinding binding;
    private com.customlauncher.app.ui.viewmodel.AppViewModel viewModel;
    private com.customlauncher.app.ui.adapter.AppListAdapter appAdapter;
    
    public AppListActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupListeners() {
    }
    
    private final void observeViewModel() {
    }
    
    private final void updateSelectAllButton() {
    }
    
    private final void updateLaunchButton() {
    }
    
    private final boolean checkAllPermissionsGranted() {
        return false;
    }
    
    @java.lang.Override
    protected void onPause() {
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
}
package com.customlauncher.app;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\b\u0010\u0018\u001a\u00020\u0016H\u0016J\b\u0010\u0019\u001a\u00020\u0016H\u0016R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000b\u001a\u00020\f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\b\u001a\u0004\b\r\u0010\u000eR\u001b\u0010\u0010\u001a\u00020\u00118FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0014\u0010\b\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001b"}, d2 = {"Lcom/customlauncher/app/LauncherApplication;", "Landroid/app/Application;", "()V", "database", "Lcom/customlauncher/app/data/database/LauncherDatabase;", "getDatabase", "()Lcom/customlauncher/app/data/database/LauncherDatabase;", "database$delegate", "Lkotlin/Lazy;", "packageChangeReceiver", "Landroid/content/BroadcastReceiver;", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "getPreferences", "()Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "preferences$delegate", "repository", "Lcom/customlauncher/app/data/repository/AppRepository;", "getRepository", "()Lcom/customlauncher/app/data/repository/AppRepository;", "repository$delegate", "checkAndHandleAppUpdate", "", "checkPermissionsOnStartup", "onCreate", "onTerminate", "Companion", "app_debug"})
public final class LauncherApplication extends android.app.Application {
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy repository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy preferences$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final android.content.BroadcastReceiver packageChangeReceiver = null;
    private static com.customlauncher.app.LauncherApplication instance;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.LauncherApplication.Companion Companion = null;
    
    public LauncherApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.database.LauncherDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.repository.AppRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.customlauncher.app.data.preferences.LauncherPreferences getPreferences() {
        return null;
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    private final void checkPermissionsOnStartup() {
    }
    
    private final void checkAndHandleAppUpdate() {
    }
    
    @java.lang.Override
    public void onTerminate() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/customlauncher/app/LauncherApplication$Companion;", "", "()V", "<set-?>", "Lcom/customlauncher/app/LauncherApplication;", "instance", "getInstance", "()Lcom/customlauncher/app/LauncherApplication;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.customlauncher.app.LauncherApplication getInstance() {
            return null;
        }
    }
}
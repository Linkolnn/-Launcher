package com.customlauncher.app;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u000e\u001a\u00020\u000f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\b\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0016"}, d2 = {"Lcom/customlauncher/app/LauncherApplication;", "Landroid/app/Application;", "()V", "database", "Lcom/customlauncher/app/data/database/LauncherDatabase;", "getDatabase", "()Lcom/customlauncher/app/data/database/LauncherDatabase;", "database$delegate", "Lkotlin/Lazy;", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "getPreferences", "()Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "preferences$delegate", "repository", "Lcom/customlauncher/app/data/repository/AppRepository;", "getRepository", "()Lcom/customlauncher/app/data/repository/AppRepository;", "repository$delegate", "onCreate", "", "Companion", "app_debug"})
public final class LauncherApplication extends android.app.Application {
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy repository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy preferences$delegate = null;
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
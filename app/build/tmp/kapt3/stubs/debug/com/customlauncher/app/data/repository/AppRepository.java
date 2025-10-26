package com.customlauncher.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0012\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u00130\u0018J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0019\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u001f\u0010 \u001a\u00020\u001d2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0013H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"J\u0006\u0010#\u001a\u00020\u001dJ\u0019\u0010$\u001a\u00020%2\u0006\u0010\u001e\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u000e\u0010&\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u000bJ\u0019\u0010\'\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u001f\u0010(\u001a\u00020\u001d2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0013H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006)"}, d2 = {"Lcom/customlauncher/app/data/repository/AppRepository;", "", "context", "Landroid/content/Context;", "hiddenAppDao", "Lcom/customlauncher/app/data/database/HiddenAppDao;", "(Landroid/content/Context;Lcom/customlauncher/app/data/database/HiddenAppDao;)V", "CACHE_VALIDITY_MS", "", "cachedHiddenApps", "", "", "lastCacheUpdate", "packageManager", "Landroid/content/pm/PackageManager;", "kotlin.jvm.PlatformType", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "getAllInstalledApps", "", "Lcom/customlauncher/app/data/model/AppInfo;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHiddenApps", "getHiddenAppsFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/customlauncher/app/data/database/HiddenApp;", "getHiddenPackages", "getVisibleApps", "hideApp", "", "packageName", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hideApps", "packageNames", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "invalidateCache", "isAppHidden", "", "launchApp", "unhideApp", "unhideApps", "app_debug"})
public final class AppRepository {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.database.HiddenAppDao hiddenAppDao = null;
    private final android.content.pm.PackageManager packageManager = null;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.preferences.LauncherPreferences preferences = null;
    @org.jetbrains.annotations.NotNull
    private java.util.Set<java.lang.String> cachedHiddenApps;
    private long lastCacheUpdate = 0L;
    private final long CACHE_VALIDITY_MS = 1000L;
    
    public AppRepository(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.customlauncher.app.data.database.HiddenAppDao hiddenAppDao) {
        super();
    }
    
    @kotlin.jvm.Synchronized
    private final synchronized java.util.Set<java.lang.String> getHiddenPackages() {
        return null;
    }
    
    @kotlin.jvm.Synchronized
    public final synchronized void invalidateCache() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.customlauncher.app.data.database.HiddenApp>> getHiddenAppsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getAllInstalledApps(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.model.AppInfo>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getVisibleApps(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.model.AppInfo>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getHiddenApps(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.customlauncher.app.data.model.AppInfo>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object hideApp(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object hideApps(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> packageNames, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object unhideApp(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object unhideApps(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> packageNames, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object isAppHidden(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    public final void launchApp(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
    }
}
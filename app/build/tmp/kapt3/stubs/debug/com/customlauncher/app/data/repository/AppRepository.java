package com.customlauncher.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u0000 .2\u00020\u0001:\u0001.B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0018H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\r0\u0018H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u00180\u001cJ\u000e\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\f0\u000fH\u0002J\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\r0\u0018H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u0019\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u001f\u0010$\u001a\u00020!2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\f0\u0018H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010&J\u0006\u0010\'\u001a\u00020!J\u0006\u0010(\u001a\u00020!J\u0019\u0010)\u001a\u00020*2\u0006\u0010\"\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u000e\u0010+\u001a\u00020!2\u0006\u0010\"\u001a\u00020\fJ\u0019\u0010,\u001a\u00020!2\u0006\u0010\"\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u001f\u0010-\u001a\u00020!2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\f0\u0018H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010&R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0012\u001a\n \u0014*\u0004\u0018\u00010\u00130\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006/"}, d2 = {"Lcom/customlauncher/app/data/repository/AppRepository;", "", "context", "Landroid/content/Context;", "hiddenAppDao", "Lcom/customlauncher/app/data/database/HiddenAppDao;", "(Landroid/content/Context;Lcom/customlauncher/app/data/database/HiddenAppDao;)V", "APP_CACHE_DURATION", "", "CACHE_VALIDITY_MS", "appListCache", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/customlauncher/app/data/model/AppInfo;", "cachedHiddenApps", "", "lastAppListUpdate", "lastCacheUpdate", "packageManager", "Landroid/content/pm/PackageManager;", "kotlin.jvm.PlatformType", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "getAllInstalledApps", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHiddenApps", "getHiddenAppsFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/customlauncher/app/data/database/HiddenApp;", "getHiddenPackages", "getVisibleApps", "hideApp", "", "packageName", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hideApps", "packageNames", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "invalidateAppCache", "invalidateCache", "isAppHidden", "", "launchApp", "unhideApp", "unhideApps", "Companion", "app_debug"})
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
    @org.jetbrains.annotations.NotNull
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.customlauncher.app.data.model.AppInfo> appListCache = null;
    private long lastAppListUpdate = 0L;
    private final long APP_CACHE_DURATION = 2000L;
    @java.lang.Deprecated
    public static final long CACHE_DURATION_MS = 30000L;
    @org.jetbrains.annotations.NotNull
    private static final com.customlauncher.app.data.repository.AppRepository.Companion Companion = null;
    
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
    
    public final void invalidateAppCache() {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/customlauncher/app/data/repository/AppRepository$Companion;", "", "()V", "CACHE_DURATION_MS", "", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
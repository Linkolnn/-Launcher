package com.customlauncher.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0006\u0010\u0012\u001a\u00020\nJ\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0014\u001a\u00020\nJ5\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u001a2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001dJ\u0016\u0010\u001e\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2 = {"Lcom/customlauncher/app/utils/IconCache;", "", "()V", "cacheHits", "", "cacheMisses", "cacheSize", "maxMemory", "memoryCache", "Landroid/util/LruCache;", "", "Landroid/graphics/Bitmap;", "clear", "", "clearCache", "drawableToBitmap", "drawable", "Landroid/graphics/drawable/Drawable;", "getCacheStats", "getCachedIcon", "cacheKey", "loadIcon", "context", "Landroid/content/Context;", "packageName", "packageManager", "Landroid/content/pm/PackageManager;", "componentName", "Landroid/content/ComponentName;", "(Landroid/content/Context;Ljava/lang/String;Landroid/content/pm/PackageManager;Landroid/content/ComponentName;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "putIcon", "icon", "app_debug"})
public final class IconCache {
    private static final int maxMemory = 0;
    private static final int cacheSize = 0;
    @org.jetbrains.annotations.NotNull
    private static final android.util.LruCache<java.lang.String, android.graphics.Bitmap> memoryCache = null;
    private static int cacheHits = 0;
    private static int cacheMisses = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.utils.IconCache INSTANCE = null;
    
    private IconCache() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.graphics.Bitmap getCachedIcon(@org.jetbrains.annotations.NotNull
    java.lang.String cacheKey) {
        return null;
    }
    
    public final void putIcon(@org.jetbrains.annotations.NotNull
    java.lang.String cacheKey, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap icon) {
    }
    
    /**
     * Get cache statistics for debugging
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCacheStats() {
        return null;
    }
    
    /**
     * Clear the entire cache
     */
    public final void clearCache() {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object loadIcon(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    android.content.pm.PackageManager packageManager, @org.jetbrains.annotations.Nullable
    android.content.ComponentName componentName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super android.graphics.drawable.Drawable> $completion) {
        return null;
    }
    
    private final android.graphics.Bitmap drawableToBitmap(android.graphics.drawable.Drawable drawable) {
        return null;
    }
    
    public final void clear() {
    }
}
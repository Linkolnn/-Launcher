package com.customlauncher.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\n\u001a\u00020\u000bJ\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0010\u001a\u00020\bJ)\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Lcom/customlauncher/app/utils/IconCache;", "", "()V", "cacheSize", "", "maxMemory", "memoryCache", "Landroid/util/LruCache;", "", "Landroid/graphics/Bitmap;", "clear", "", "drawableToBitmap", "drawable", "Landroid/graphics/drawable/Drawable;", "getCachedIcon", "packageName", "loadIcon", "context", "Landroid/content/Context;", "packageManager", "Landroid/content/pm/PackageManager;", "(Landroid/content/Context;Ljava/lang/String;Landroid/content/pm/PackageManager;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "putIcon", "icon", "app_debug"})
public final class IconCache {
    private static final int maxMemory = 0;
    private static final int cacheSize = 0;
    @org.jetbrains.annotations.NotNull
    private static final android.util.LruCache<java.lang.String, android.graphics.Bitmap> memoryCache = null;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.utils.IconCache INSTANCE = null;
    
    private IconCache() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.graphics.Bitmap getCachedIcon(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
        return null;
    }
    
    public final void putIcon(@org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    android.graphics.Bitmap icon) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object loadIcon(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    android.content.pm.PackageManager packageManager, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super android.graphics.drawable.Drawable> $completion) {
        return null;
    }
    
    private final android.graphics.Bitmap drawableToBitmap(android.graphics.drawable.Drawable drawable) {
        return null;
    }
    
    public final void clear() {
    }
}
package com.customlauncher.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J \u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u001a\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000fJ \u0010\u0013\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/customlauncher/app/utils/IconPackManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getAvailableIconPacks", "", "Lcom/customlauncher/app/data/model/IconPack;", "getIconFromAppFilter", "", "iconPackResources", "Landroid/content/res/Resources;", "iconPackPackage", "", "componentName", "Landroid/content/ComponentName;", "getIconFromPack", "Landroid/graphics/drawable/Drawable;", "packageName", "getIconResId", "Companion", "app_debug"})
public final class IconPackManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "IconPackManager";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String NOVA_LAUNCHER = "com.teslacoilsw.launcher";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String GO_LAUNCHER = "com.gau.go.launcherex";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String APEX_LAUNCHER = "com.anddoes.launcher";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ADW_LAUNCHER = "org.adw.launcher";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ACTION_LAUNCHER = "com.actionlauncher.playstore";
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.utils.IconPackManager.Companion Companion = null;
    
    public IconPackManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.customlauncher.app.data.model.IconPack> getAvailableIconPacks() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.graphics.drawable.Drawable getIconFromPack(@org.jetbrains.annotations.Nullable
    java.lang.String packageName, @org.jetbrains.annotations.NotNull
    android.content.ComponentName componentName) {
        return null;
    }
    
    private final int getIconResId(android.content.res.Resources iconPackResources, java.lang.String iconPackPackage, android.content.ComponentName componentName) {
        return 0;
    }
    
    private final int getIconFromAppFilter(android.content.res.Resources iconPackResources, java.lang.String iconPackPackage, android.content.ComponentName componentName) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/customlauncher/app/utils/IconPackManager$Companion;", "", "()V", "ACTION_LAUNCHER", "", "ADW_LAUNCHER", "APEX_LAUNCHER", "GO_LAUNCHER", "NOVA_LAUNCHER", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
package com.customlauncher.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005J\u0006\u0010\t\u001a\u00020\u0007J\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bJ\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\b\u001a\u00020\u0005J\u000e\u0010\u000f\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005J\u0014\u0010\u0010\u001a\u00020\u00072\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/customlauncher/app/data/SelectionManager;", "", "()V", "selectedApps", "", "", "addSelection", "", "packageName", "clearSelection", "getSelection", "", "hasSelection", "", "isSelected", "removeSelection", "setSelection", "packageNames", "app_debug"})
public final class SelectionManager {
    @org.jetbrains.annotations.NotNull
    private static final java.util.Set<java.lang.String> selectedApps = null;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.data.SelectionManager INSTANCE = null;
    
    private SelectionManager() {
        super();
    }
    
    @kotlin.jvm.Synchronized
    public final synchronized void addSelection(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
    }
    
    @kotlin.jvm.Synchronized
    public final synchronized void removeSelection(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
    }
    
    @kotlin.jvm.Synchronized
    public final synchronized void setSelection(@org.jetbrains.annotations.NotNull
    java.util.Set<java.lang.String> packageNames) {
    }
    
    @kotlin.jvm.Synchronized
    @org.jetbrains.annotations.NotNull
    public final synchronized java.util.Set<java.lang.String> getSelection() {
        return null;
    }
    
    @kotlin.jvm.Synchronized
    public final synchronized void clearSelection() {
    }
    
    @kotlin.jvm.Synchronized
    public final synchronized boolean isSelected(@org.jetbrains.annotations.NotNull
    java.lang.String packageName) {
        return false;
    }
    
    @kotlin.jvm.Synchronized
    public final synchronized boolean hasSelection() {
        return false;
    }
}
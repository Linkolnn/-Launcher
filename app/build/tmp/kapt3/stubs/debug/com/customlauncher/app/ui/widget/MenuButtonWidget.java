package com.customlauncher.app.ui.widget;

/**
 * Menu button widget for button phone mode
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0014\u001a\u00020\u0011J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0006\u0010\u0017\u001a\u00020\u0011J\u000e\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u0016J\u0014\u0010\u001a\u001a\u00020\u00112\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010J\u0006\u0010\u001c\u001a\u00020\u0016J\u0006\u0010\u001d\u001a\u00020\u0011R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/customlauncher/app/ui/widget/MenuButtonWidget;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "menuButton", "Landroid/widget/LinearLayout;", "menuIcon", "Landroid/widget/ImageView;", "menuText", "Landroid/widget/TextView;", "onMenuClickListener", "Lkotlin/Function0;", "", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "clearButtonFocus", "hasFocus", "", "requestButtonFocus", "setButtonMode", "enabled", "setOnMenuClickListener", "listener", "shouldBeVisible", "updateVisibility", "Companion", "app_debug"})
public final class MenuButtonWidget extends android.widget.FrameLayout {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "MenuButtonWidget";
    private android.widget.LinearLayout menuButton;
    private android.widget.ImageView menuIcon;
    private android.widget.TextView menuText;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.preferences.LauncherPreferences preferences = null;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function0<kotlin.Unit> onMenuClickListener;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.widget.MenuButtonWidget.Companion Companion = null;
    
    @kotlin.jvm.JvmOverloads
    public MenuButtonWidget(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    /**
     * Set the click listener for menu button
     */
    public final void setOnMenuClickListener(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> listener) {
    }
    
    /**
     * Update visibility based on menu access method setting
     */
    public final void updateVisibility() {
    }
    
    /**
     * Check if button should be visible
     */
    public final boolean shouldBeVisible() {
        return false;
    }
    
    /**
     * Request focus on the button
     */
    public final void requestButtonFocus() {
    }
    
    /**
     * Clear focus from the button
     */
    public final void clearButtonFocus() {
    }
    
    /**
     * Set whether button is in button mode (for visual feedback)
     */
    public final void setButtonMode(boolean enabled) {
    }
    
    /**
     * Check if the menu button has focus
     */
    @java.lang.Override
    public boolean hasFocus() {
        return false;
    }
    
    @kotlin.jvm.JvmOverloads
    public MenuButtonWidget(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads
    public MenuButtonWidget(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/customlauncher/app/ui/widget/MenuButtonWidget$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
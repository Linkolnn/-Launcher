package com.customlauncher.app.ui.widget;

/**
 * Phone buttons widget for button phone mode
 * Shows Call and Contacts buttons
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u0000 \"2\u00020\u0001:\u0001\"B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0016\u001a\u00020\u0017J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u0017H\u0002J\b\u0010\u001b\u001a\u00020\u0017H\u0002J\u0006\u0010\u001c\u001a\u00020\u0017J\u0006\u0010\u001d\u001a\u00020\u0017J\u000e\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u001f\u001a\u00020\u0019J\u0006\u0010 \u001a\u00020\u0019J\u0006\u0010!\u001a\u00020\u0017R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/customlauncher/app/ui/widget/PhoneButtonWidget;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "callButton", "Landroid/widget/LinearLayout;", "callIcon", "Landroid/widget/ImageView;", "callText", "Landroid/widget/TextView;", "contactsButton", "contactsIcon", "contactsText", "phoneButtonsContainer", "Landroid/view/ViewGroup;", "preferences", "Lcom/customlauncher/app/data/preferences/LauncherPreferences;", "clearAllFocus", "", "hasFocus", "", "openContacts", "openDialer", "requestCallButtonFocus", "requestContactsButtonFocus", "setButtonMode", "enabled", "shouldBeVisible", "updateVisibility", "Companion", "app_debug"})
public final class PhoneButtonWidget extends android.widget.FrameLayout {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "PhoneButtonWidget";
    private android.view.ViewGroup phoneButtonsContainer;
    private android.widget.LinearLayout callButton;
    private android.widget.LinearLayout contactsButton;
    private android.widget.ImageView callIcon;
    private android.widget.TextView callText;
    private android.widget.ImageView contactsIcon;
    private android.widget.TextView contactsText;
    @org.jetbrains.annotations.NotNull
    private final com.customlauncher.app.data.preferences.LauncherPreferences preferences = null;
    @org.jetbrains.annotations.NotNull
    public static final com.customlauncher.app.ui.widget.PhoneButtonWidget.Companion Companion = null;
    
    @kotlin.jvm.JvmOverloads
    public PhoneButtonWidget(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    /**
     * Open the phone dialer app
     */
    private final void openDialer() {
    }
    
    /**
     * Open the contacts app
     */
    private final void openContacts() {
    }
    
    /**
     * Update visibility based on settings
     */
    public final void updateVisibility() {
    }
    
    /**
     * Check if buttons should be visible
     */
    public final boolean shouldBeVisible() {
        return false;
    }
    
    /**
     * Request focus on the call button
     */
    public final void requestCallButtonFocus() {
    }
    
    /**
     * Request focus on the contacts button
     */
    public final void requestContactsButtonFocus() {
    }
    
    /**
     * Clear focus from all buttons
     */
    public final void clearAllFocus() {
    }
    
    /**
     * Set whether buttons are in button mode (for visual feedback)
     */
    public final void setButtonMode(boolean enabled) {
    }
    
    /**
     * Check if any of the phone buttons has focus
     */
    @java.lang.Override
    public boolean hasFocus() {
        return false;
    }
    
    @kotlin.jvm.JvmOverloads
    public PhoneButtonWidget(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads
    public PhoneButtonWidget(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/customlauncher/app/ui/widget/PhoneButtonWidget$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
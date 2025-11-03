package com.customlauncher.app.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.customlauncher.app.R
import com.customlauncher.app.data.preferences.LauncherPreferences

/**
 * Menu button widget for button phone mode
 */
class MenuButtonWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    companion object {
        private const val TAG = "MenuButtonWidget"
    }
    
    private lateinit var menuButton: LinearLayout
    private lateinit var menuIcon: ImageView
    private lateinit var menuText: TextView
    private val preferences = LauncherPreferences(context)
    
    private var onMenuClickListener: (() -> Unit)? = null
    
    init {
        // Inflate the layout
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.menu_button_widget, this, true)
        
        // The root LinearLayout from the inflated layout is now the first child of this FrameLayout
        menuButton = getChildAt(0) as LinearLayout
        menuIcon = findViewById(R.id.menuIcon)
        menuText = findViewById(R.id.menuText)
        
        // Setup click listener
        menuButton.setOnClickListener {
            Log.d(TAG, "Menu button clicked")
            onMenuClickListener?.invoke()
        }
        
        // Make focusable for D-pad navigation
        menuButton.isFocusable = true
        menuButton.isFocusableInTouchMode = false
        menuButton.isClickable = true
    }
    
    /**
     * Set the click listener for menu button
     */
    fun setOnMenuClickListener(listener: () -> Unit) {
        onMenuClickListener = listener
    }
    
    /**
     * Update visibility based on menu access method setting
     */
    fun updateVisibility() {
        val menuAccessMethod = preferences.menuAccessMethod
        Log.d(TAG, "Updating visibility, menu access method: $menuAccessMethod")
        
        // Show button only if NOT using "navigation down" method
        // Methods: "button", "gesture", "dpad_down"
        visibility = if (menuAccessMethod == "dpad_down") {
            Log.d(TAG, "Hiding menu button (using navigation down)")
            View.GONE
        } else {
            Log.d(TAG, "Showing menu button")
            View.VISIBLE
        }
    }
    
    /**
     * Check if button should be visible
     */
    fun shouldBeVisible(): Boolean {
        return preferences.menuAccessMethod != "dpad_down"
    }
    
    /**
     * Request focus on the button
     */
    fun requestButtonFocus() {
        menuButton.requestFocus()
    }
    
    /**
     * Clear focus from the button
     */
    fun clearButtonFocus() {
        menuButton.clearFocus()
    }
    
    /**
     * Set whether button is in button mode (for visual feedback)
     */
    fun setButtonMode(enabled: Boolean) {
        if (enabled) {
            // Make focusable for D-pad
            menuButton.isFocusableInTouchMode = false
            menuButton.isFocusable = true
        } else {
            // Normal touch mode
            menuButton.isFocusableInTouchMode = false
            menuButton.isFocusable = true
        }
    }
    
    /**
     * Check if the menu button has focus
     */
    override fun hasFocus(): Boolean {
        return if (::menuButton.isInitialized) {
            menuButton.hasFocus()
        } else {
            super.hasFocus()
        }
    }
}

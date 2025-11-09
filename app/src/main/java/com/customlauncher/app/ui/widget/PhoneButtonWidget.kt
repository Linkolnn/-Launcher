package com.customlauncher.app.ui.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.customlauncher.app.R
import com.customlauncher.app.data.preferences.LauncherPreferences

/**
 * Phone buttons widget for button phone mode
 * Shows Call and Contacts buttons
 */
class PhoneButtonWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    companion object {
        private const val TAG = "PhoneButtonWidget"
    }
    
    private lateinit var phoneButtonsContainer: ViewGroup
    private lateinit var callButton: LinearLayout
    private lateinit var contactsButton: LinearLayout
    private lateinit var callIcon: ImageView
    private lateinit var callText: TextView
    private lateinit var contactsIcon: ImageView
    private lateinit var contactsText: TextView
    
    private val preferences = LauncherPreferences(context)
    
    init {
        // Inflate the layout
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.phone_buttons_widget, this, true)
        
        // Find views
        phoneButtonsContainer = findViewById(R.id.phoneButtonsContainer)
        callButton = findViewById(R.id.callButton)
        contactsButton = findViewById(R.id.contactsButton)
        callIcon = findViewById(R.id.callIcon)
        callText = findViewById(R.id.callText)
        contactsIcon = findViewById(R.id.contactsIcon)
        contactsText = findViewById(R.id.contactsText)
        
        // Setup click listeners
        callButton.setOnClickListener {
            Log.d(TAG, "Call button clicked")
            openDialer()
        }
        
        contactsButton.setOnClickListener {
            Log.d(TAG, "Contacts button clicked")
            openContacts()
        }
        
        // Make buttons focusable for D-pad navigation
        callButton.isFocusable = true
        callButton.isFocusableInTouchMode = false
        callButton.isClickable = true
        
        contactsButton.isFocusable = true
        contactsButton.isFocusableInTouchMode = false
        contactsButton.isClickable = true
    }
    
    /**
     * Open the phone dialer app
     */
    private fun openDialer() {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening dialer", e)
            Toast.makeText(context, "Не удалось открыть телефон", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Open the contacts app
     */
    private fun openContacts() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.type = "vnd.android.cursor.dir/contact"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening contacts", e)
            // Try alternative way
            try {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "vnd.android.cursor.dir/contact"
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e2: Exception) {
                Log.e(TAG, "Error opening contacts (alternative)", e2)
                Toast.makeText(context, "Не удалось открыть контакты", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    /**
     * Update visibility based on settings
     */
    fun updateVisibility() {
        val showPhoneButtons = preferences.showPhoneButtons
        val menuAccessMethod = preferences.menuAccessMethod
        
        Log.d(TAG, "Updating visibility, show phone buttons: $showPhoneButtons, menu access: $menuAccessMethod")
        
        // Show buttons only if enabled and menu button is visible
        visibility = if (showPhoneButtons && menuAccessMethod != "dpad_down") {
            Log.d(TAG, "Showing phone buttons")
            View.VISIBLE
        } else {
            Log.d(TAG, "Hiding phone buttons")
            View.GONE
        }
    }
    
    /**
     * Check if buttons should be visible
     */
    fun shouldBeVisible(): Boolean {
        return preferences.showPhoneButtons && preferences.menuAccessMethod != "dpad_down"
    }
    
    /**
     * Request focus on the call button
     */
    fun requestCallButtonFocus() {
        callButton.requestFocus()
    }
    
    /**
     * Request focus on the contacts button
     */
    fun requestContactsButtonFocus() {
        contactsButton.requestFocus()
    }
    
    /**
     * Clear focus from all buttons
     */
    fun clearAllFocus() {
        callButton.clearFocus()
        contactsButton.clearFocus()
    }
    
    /**
     * Set whether buttons are in button mode (for visual feedback)
     */
    fun setButtonMode(enabled: Boolean) {
        if (enabled) {
            // Make focusable for D-pad
            callButton.isFocusableInTouchMode = false
            callButton.isFocusable = true
            contactsButton.isFocusableInTouchMode = false
            contactsButton.isFocusable = true
        } else {
            // Normal touch mode
            callButton.isFocusableInTouchMode = false
            callButton.isFocusable = true
            contactsButton.isFocusableInTouchMode = false
            contactsButton.isFocusable = true
        }
    }
    
    /**
     * Check if any of the phone buttons has focus
     */
    override fun hasFocus(): Boolean {
        return if (::callButton.isInitialized && ::contactsButton.isInitialized) {
            callButton.hasFocus() || contactsButton.hasFocus()
        } else {
            super.hasFocus()
        }
    }
}

package com.customlauncher.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.customlauncher.app.LauncherApplication
import com.customlauncher.app.manager.HiddenModeStateManager
import com.customlauncher.app.ui.MainActivity
import android.app.KeyguardManager
import android.os.Build

class KeyCombinationReceiver : BroadcastReceiver() {
    
    companion object {
        const val TAG = "KeyCombinationReceiver"
        const val ACTION_KEY_COMBINATION_DETECTED = "com.customlauncher.app.KEY_COMBINATION_DETECTED"
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_KEY_COMBINATION_DETECTED) {
            val fromLockScreen = intent.getBooleanExtra("from_lock_screen", false)
            Log.d(TAG, "Key combination detected via broadcast (from lock screen: $fromLockScreen)")
            
            context?.let { ctx ->
                // Check if device is locked
                val keyguardManager = ctx.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                val isDeviceLocked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    keyguardManager.isDeviceLocked
                } else {
                    keyguardManager.isKeyguardLocked
                }
                
                Log.d(TAG, "Processing key combination: fromLockScreen=$fromLockScreen, deviceLocked=$isDeviceLocked")
                
                // Use centralized state manager
                val wasHidden = HiddenModeStateManager.currentState
                HiddenModeStateManager.toggleHiddenMode(ctx)
                
                if (wasHidden) {
                    // Was hidden, now showing - state manager handles unblocking
                    // Start MainActivity to show launcher
                    val mainIntent = Intent(ctx, MainActivity::class.java)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or 
                                      Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                      Intent.FLAG_ACTIVITY_SINGLE_TOP or
                                      Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    
                    ctx.startActivity(mainIntent)
                    
                    Log.d(TAG, "Apps shown via state manager")
                } else {
                    // Was showing, now hiding - state manager handles blocking
                    Log.d(TAG, "Apps hidden via state manager")
                }
            }
        }
    }
}

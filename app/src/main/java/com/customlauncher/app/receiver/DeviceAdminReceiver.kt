package com.customlauncher.app.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class LauncherDeviceAdminReceiver : DeviceAdminReceiver() {
    
    companion object {
        private const val TAG = "DeviceAdminReceiver"
    }
    
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.d(TAG, "Device admin enabled")
        Toast.makeText(context, "Администратор устройства активирован", Toast.LENGTH_SHORT).show()
    }
    
    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.d(TAG, "Device admin disabled")
        Toast.makeText(context, "Администратор устройства деактивирован", Toast.LENGTH_SHORT).show()
    }
    
    override fun onPasswordFailed(context: Context, intent: Intent) {
        super.onPasswordFailed(context, intent)
        Log.d(TAG, "Password failed")
    }
    
    override fun onPasswordSucceeded(context: Context, intent: Intent) {
        super.onPasswordSucceeded(context, intent)
        Log.d(TAG, "Password succeeded")
    }
}

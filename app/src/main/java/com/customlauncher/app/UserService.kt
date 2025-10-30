package com.customlauncher.app

import android.os.RemoteException
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.system.exitProcess

class UserService : IUserService.Stub() {
    
    companion object {
        private const val TAG = "UserService"
    }
    
    override fun destroy() {
        Log.d(TAG, "destroy")
        exitProcess(0)
    }
    
    override fun exit() {
        destroy()
    }
    
    override fun executeCommand(cmd: String): String {
        return try {
            Log.d(TAG, "Executing command: $cmd")
            
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd))
            
            val outputReader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            
            val output = StringBuilder()
            val error = StringBuilder()
            
            outputReader.use { reader ->
                reader.lineSequence().forEach { line ->
                    output.appendLine(line)
                }
            }
            
            errorReader.use { reader ->
                reader.lineSequence().forEach { line ->
                    error.appendLine(line)
                }
            }
            
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                // Return SUCCESS for empty output (common for settings commands)
                val result = output.toString().trim()
                if (result.isEmpty()) "SUCCESS" else result
            } else {
                "ERROR:${error.toString().trim().ifEmpty { "Exit code: $exitCode" }}"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to execute command: $cmd", e)
            "ERROR:${e.message}"
        }
    }
}

package com.customlauncher.app.receiver

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import com.customlauncher.app.data.model.CustomKeyCombination

class CustomKeyListener(
    private val onCombinationDetected: () -> Unit
) {
    private val keySequence = mutableListOf<Int>()
    private var targetCombination: CustomKeyCombination? = null
    private val handler = Handler(Looper.getMainLooper())
    private val resetRunnable = Runnable { reset() }
    private var isProcessing = false
    
    companion object {
        private const val TAG = "CustomKeyListener"
        private const val TIMEOUT_MS = 2000L
    }
    
    fun setCombination(combination: CustomKeyCombination?) {
        targetCombination = combination
        reset()
    }
    
    fun onKeyEvent(keyCode: Int): Boolean {
        val combination = targetCombination ?: return false
        
        // Cancel reset timer
        handler.removeCallbacks(resetRunnable)
        
        // Add key to sequence
        keySequence.add(keyCode)
        Log.d(TAG, "Key sequence: $keySequence")
        
        // Check if sequence matches
        if (keySequence == combination.keys) {
            Log.d(TAG, "Custom combination detected!")
            if (!isProcessing) {
                isProcessing = true
                handler.post {
                    onCombinationDetected()
                    handler.postDelayed({ isProcessing = false }, 500)
                }
            }
            reset()
            return true
        }
        
        // Check if sequence is too long or doesn't match prefix
        if (keySequence.size >= combination.keys.size) {
            // Check if this could be the start of a new sequence
            if (keySequence.takeLast(1) == combination.keys.take(1)) {
                keySequence.clear()
                keySequence.add(keyCode)
            } else {
                reset()
            }
        } else {
            // Check if current sequence matches the prefix
            if (keySequence != combination.keys.take(keySequence.size)) {
                // Check if last key could be start of new sequence
                if (listOf(keyCode) == combination.keys.take(1)) {
                    keySequence.clear()
                    keySequence.add(keyCode)
                } else {
                    reset()
                }
            }
        }
        
        // Set timeout for reset
        handler.postDelayed(resetRunnable, combination.timeoutMs)
        
        return false
    }
    
    private fun reset() {
        keySequence.clear()
        handler.removeCallbacks(resetRunnable)
    }
    
    fun destroy() {
        reset()
    }
}

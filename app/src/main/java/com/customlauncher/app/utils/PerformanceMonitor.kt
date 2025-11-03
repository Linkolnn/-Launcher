package com.customlauncher.app.utils

import android.os.Debug
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlin.system.measureTimeMillis

/**
 * Performance monitoring utility for tracking app performance
 */
object PerformanceMonitor {
    private const val TAG = "PerformanceMonitor"
    private val metrics = mutableMapOf<String, MutableList<Long>>()
    private var memoryCheckHandler: Handler? = null
    private var memoryCheckRunnable: Runnable? = null
    
    // Memory thresholds in MB
    private const val MEMORY_WARNING_THRESHOLD = 100
    private const val MEMORY_CRITICAL_THRESHOLD = 150
    
    /**
     * Measure and log execution time of a block
     */
    fun <T> measureTime(label: String, block: () -> T): T {
        val result: T
        val time = measureTimeMillis {
            result = block()
        }
        
        // Store metric
        metrics.getOrPut(label) { mutableListOf() }.add(time)
        
        // Log if slow
        if (time > 100) {
            Log.w(TAG, "$label took ${time}ms")
        } else {
            Log.d(TAG, "$label took ${time}ms")
        }
        
        return result
    }
    
    /**
     * Start monitoring memory usage
     */
    fun startMemoryMonitoring(intervalMs: Long = 5000L) {
        stopMemoryMonitoring()
        
        memoryCheckHandler = Handler(Looper.getMainLooper())
        memoryCheckRunnable = object : Runnable {
            override fun run() {
                checkMemoryUsage()
                memoryCheckHandler?.postDelayed(this, intervalMs)
            }
        }
        memoryCheckHandler?.post(memoryCheckRunnable!!)
        
        Log.d(TAG, "Started memory monitoring with ${intervalMs}ms interval")
    }
    
    /**
     * Stop monitoring memory usage
     */
    fun stopMemoryMonitoring() {
        memoryCheckRunnable?.let {
            memoryCheckHandler?.removeCallbacks(it)
        }
        memoryCheckHandler = null
        memoryCheckRunnable = null
        
        Log.d(TAG, "Stopped memory monitoring")
    }
    
    /**
     * Check current memory usage
     */
    private fun checkMemoryUsage() {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory() / (1024 * 1024) // Convert to MB
        val freeMemory = runtime.freeMemory() / (1024 * 1024)
        val usedMemory = totalMemory - freeMemory
        val maxMemory = runtime.maxMemory() / (1024 * 1024)
        
        val nativeHeap = Debug.getNativeHeapAllocatedSize() / (1024 * 1024)
        
        when {
            usedMemory > MEMORY_CRITICAL_THRESHOLD -> {
                Log.e(TAG, "CRITICAL memory usage: ${usedMemory}MB / ${maxMemory}MB (Native: ${nativeHeap}MB)")
                // Trigger memory cleanup
                System.gc()
            }
            usedMemory > MEMORY_WARNING_THRESHOLD -> {
                Log.w(TAG, "High memory usage: ${usedMemory}MB / ${maxMemory}MB (Native: ${nativeHeap}MB)")
            }
            else -> {
                Log.d(TAG, "Memory: ${usedMemory}MB / ${maxMemory}MB (Native: ${nativeHeap}MB)")
            }
        }
    }
    
    /**
     * Get performance report
     */
    fun getPerformanceReport(): String {
        val report = StringBuilder()
        report.appendLine("=== Performance Report ===")
        
        // Memory info
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        val maxMemory = runtime.maxMemory() / (1024 * 1024)
        report.appendLine("Memory: ${usedMemory}MB / ${maxMemory}MB")
        
        // Timing metrics
        report.appendLine("\n=== Timing Metrics ===")
        metrics.forEach { (label, times) ->
            if (times.isNotEmpty()) {
                val avg = times.average().toInt()
                val min = times.minOrNull() ?: 0
                val max = times.maxOrNull() ?: 0
                report.appendLine("$label: avg=${avg}ms, min=${min}ms, max=${max}ms (${times.size} samples)")
            }
        }
        
        // Icon cache stats
        report.appendLine("\n=== Icon Cache ===")
        report.appendLine(IconCache.getCacheStats())
        
        return report.toString()
    }
    
    /**
     * Clear all metrics
     */
    fun clearMetrics() {
        metrics.clear()
        Log.d(TAG, "Cleared all metrics")
    }
    
    /**
     * Log current FPS (frames per second)
     * Should be called from Choreographer callback
     */
    fun logFps(frameTimeNanos: Long) {
        val fps = 1_000_000_000 / frameTimeNanos
        if (fps < 30) {
            Log.w(TAG, "Low FPS: $fps")
        } else {
            Log.d(TAG, "FPS: $fps")
        }
    }
}

package com.customlauncher.app.ui.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.customlauncher.app.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Custom clock widget for home screen with adaptive sizing
 */
class ClockWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private val timeView: TextClock
    private val dateView: TextView
    private var timeReceiver: BroadcastReceiver? = null
    
    companion object {
        // Size constraints for the widget (in grid cells)
        const val MIN_SPAN_X = 1
        const val MIN_SPAN_Y = 1
        const val MAX_SPAN_X = 4
        const val MAX_SPAN_Y = 2
        const val DEFAULT_SPAN_X = 2
        const val DEFAULT_SPAN_Y = 1
    }
    
    // Store current span values
    private var currentSpanX: Int = DEFAULT_SPAN_X
    private var currentSpanY: Int = DEFAULT_SPAN_Y
    
    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        // Transparent background
        setBackgroundColor(Color.TRANSPARENT)
        setPadding(16, 16, 16, 16)
        
        // Load custom font
        val customFont = try {
            ResourcesCompat.getFont(context, R.font.mal6lampen)
        } catch (e: Exception) {
            Typeface.create("sans-serif-light", Typeface.NORMAL)
        }
        
        // Create time view
        timeView = TextClock(context).apply {
            format24Hour = "HH:mm"
            format12Hour = "hh:mm"
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 48f)
            typeface = customFont
            gravity = Gravity.CENTER
            setShadowLayer(4f, 2f, 2f, Color.argb(128, 0, 0, 0))
            // Increase line spacing for better readability
            setLineSpacing(16f * context.resources.displayMetrics.density, 1f)
        }
        
        // Create date view
        dateView = TextView(context).apply {
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            typeface = customFont
            gravity = Gravity.CENTER
            setShadowLayer(2f, 1f, 1f, Color.argb(128, 0, 0, 0))
            // Add increased line spacing to prevent text overlap
            setLineSpacing(14f * context.resources.displayMetrics.density, 1f)
        }
        
        // Add views
        addView(timeView, LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ))
        
        addView(dateView, LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            // Add margin to prevent overlap
            topMargin = (12 * context.resources.displayMetrics.density).toInt()
        })
        
        // Initialize date text
        updateDate()
        
        // Setup date update receiver
        setupDateUpdateReceiver()
    }
    
    private fun updateDate() {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
        dateView.text = dateFormat.format(Date())
    }
    
    private fun setupDateUpdateReceiver() {
        timeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_TIME_CHANGED,
                    Intent.ACTION_TIMEZONE_CHANGED,
                    Intent.ACTION_DATE_CHANGED -> {
                        updateDate()
                    }
                }
            }
        }
        
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
            addAction(Intent.ACTION_DATE_CHANGED)
            addAction(Intent.ACTION_TIME_TICK)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(timeReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(timeReceiver, filter)
        }
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateDate()
        if (timeReceiver == null) {
            setupDateUpdateReceiver()
        }
        // Ensure proper formatting on initial display
        if (width > 0 && height > 0) {
            adjustTextSizes(width, height)
        }
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timeReceiver?.let {
            try {
                context.unregisterReceiver(it)
            } catch (e: Exception) {
                // Already unregistered
            }
            timeReceiver = null
        }
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        adjustTextSizes(w, h)
    }
    
    /**
     * Adjust text sizes based on widget dimensions
     */
    private fun adjustTextSizes(width: Int, height: Int) {
        if (width <= 0 || height <= 0) return
        
        // Special formatting for 1x2 size - time on two lines
        val is1x2 = currentSpanX == 1 && currentSpanY == 2
        
        // Update time format based on widget size
        if (is1x2) {
            // For 1x2: display time in two lines (HH on first line, mm on second)
            timeView.format24Hour = "HH\nmm"
            timeView.format12Hour = "hh\nmm"
            // Optimized text size for two-line display
            val twoLineTextSize = minOf(42f, width / 3f)
            timeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, twoLineTextSize)
            // Center text for better appearance
            timeView.gravity = Gravity.CENTER
        } else {
            // For all other sizes: display time in one line
            timeView.format24Hour = "HH:mm"
            timeView.format12Hour = "hh:mm"
            
            // Time text size - adjusted for different configurations
            val timeTextSize = when {
                currentSpanX == 1 && currentSpanY == 1 -> 24f  // Small for 1x1
                currentSpanY == 1 -> 36f  // Medium for single row widgets
                currentSpanX >= 3 -> 48f  // Larger for wide widgets
                else -> maxOf(32f, minOf(56f, height / 3f))  // Adaptive for other sizes
            }
            timeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeTextSize)
        }
        
        // Date visibility - hide for 1x1, 1x2, and optionally 2x1
        val shouldShowDate = currentSpanX > 1 && currentSpanY > 1
        
        if (!shouldShowDate || is1x2) {
            // Always hide date for 1x2 size
            dateView.visibility = GONE
            orientation = VERTICAL
        } else {
            dateView.visibility = VISIBLE
            orientation = VERTICAL
            
            // Date text size (proportional to time)
            val currentTimeSize = timeView.textSize / context.resources.displayMetrics.scaledDensity
            val dateTextSize = maxOf(10f, minOf(16f, currentTimeSize * 0.35f))
            dateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateTextSize)
        }
        
        // Adjust padding based on widget size
        val padding = if (is1x2) {
            // Less padding for 1x2 to maximize space
            (width * 0.02f).toInt()
        } else {
            (minOf(width, height) * 0.03f).toInt()
        }
        setPadding(padding, padding, padding, padding)
    }
    
    /**
     * Update widget size (called when resizing)
     */
    fun updateSize(spanX: Int, spanY: Int) {
        // Constrain to min/max values
        currentSpanX = spanX.coerceIn(MIN_SPAN_X, MAX_SPAN_X)
        currentSpanY = spanY.coerceIn(MIN_SPAN_Y, MAX_SPAN_Y)
        
        // Adjust text sizes if view is already laid out
        if (width > 0 && height > 0) {
            adjustTextSizes(width, height)
        } else {
            // If the view is not laid out yet, schedule text size adjustment
            post {
                if (width > 0 && height > 0) {
                    adjustTextSizes(width, height)
                }
            }
        }
        
        // Also ensure text doesn't get cut off by requesting extra space
        timeView.requestLayout()
        dateView.requestLayout()
        
        // Request layout update
        requestLayout()
        invalidate()
    }
    
    /**
     * Get widget configuration for database storage
     */
    fun getWidgetConfig(): WidgetConfig {
        return WidgetConfig(
            type = "clock",
            spanX = DEFAULT_SPAN_X,
            spanY = DEFAULT_SPAN_Y,
            minSpanX = MIN_SPAN_X,
            minSpanY = MIN_SPAN_Y,
            maxSpanX = MAX_SPAN_X,
            maxSpanY = MAX_SPAN_Y
        )
    }
    
    data class WidgetConfig(
        val type: String,
        val spanX: Int,
        val spanY: Int,
        val minSpanX: Int = 1,
        val minSpanY: Int = 1,
        val maxSpanX: Int = 4,
        val maxSpanY: Int = 4
    )
}

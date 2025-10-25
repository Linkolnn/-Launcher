package com.customlauncher.app.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class FadingEdgeRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    
    private val fadePaint = Paint()
    private var topFadeSize = 0
    private var bottomFadeSize = 0
    private var fadeEnabled = true
    
    init {
        // Set default fade sizes in pixels
        val density = context.resources.displayMetrics.density
        topFadeSize = (50 * density).toInt()
        bottomFadeSize = (40 * density).toInt()
    }
    
    fun setFadeSizes(top: Int, bottom: Int) {
        topFadeSize = top
        bottomFadeSize = bottom
        invalidate()
    }
    
    fun setFadeEnabled(enabled: Boolean) {
        fadeEnabled = enabled
        invalidate()
    }
    
    override fun dispatchDraw(canvas: Canvas) {
        if (!fadeEnabled || (topFadeSize == 0 && bottomFadeSize == 0)) {
            super.dispatchDraw(canvas)
            return
        }
        
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        super.dispatchDraw(canvas)
        
        fadePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        
        // Draw top fade
        if (topFadeSize > 0) {
            val topGradient = LinearGradient(
                0f, 0f,
                0f, topFadeSize.toFloat(),
                0xFF000000.toInt(),
                0x00000000,
                Shader.TileMode.CLAMP
            )
            fadePaint.shader = topGradient
            canvas.drawRect(0f, 0f, width.toFloat(), topFadeSize.toFloat(), fadePaint)
        }
        
        // Draw bottom fade
        if (bottomFadeSize > 0) {
            val bottomGradient = LinearGradient(
                0f, height - bottomFadeSize.toFloat(),
                0f, height.toFloat(),
                0x00000000,
                0xFF000000.toInt(),
                Shader.TileMode.CLAMP
            )
            fadePaint.shader = bottomGradient
            canvas.drawRect(0f, height - bottomFadeSize.toFloat(), width.toFloat(), height.toFloat(), fadePaint)
        }
        
        fadePaint.xfermode = null
        canvas.restoreToCount(saveCount)
    }
}

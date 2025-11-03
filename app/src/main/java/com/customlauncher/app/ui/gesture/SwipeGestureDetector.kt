package com.customlauncher.app.ui.gesture

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

/**
 * Detector for swipe gestures on the home screen
 */
class SwipeGestureDetector(
    context: Context,
    private val onSwipeUp: (() -> Unit)? = null,
    private val onSwipeDown: (() -> Unit)? = null,
    private val onSwipeLeft: (() -> Unit)? = null,
    private val onSwipeRight: (() -> Unit)? = null
) : GestureDetector.SimpleOnGestureListener() {

    private val gestureDetector = GestureDetector(context, this)

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 == null) return false
        
        val diffX = e2.x - e1.x
        val diffY = e2.y - e1.y
        
        if (abs(diffX) > abs(diffY)) {
            // Horizontal swipe
            if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    // Swipe right
                    onSwipeRight?.invoke()
                } else {
                    // Swipe left
                    onSwipeLeft?.invoke()
                }
                return true
            }
        } else {
            // Vertical swipe
            if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    // Swipe down
                    onSwipeDown?.invoke()
                } else {
                    // Swipe up
                    onSwipeUp?.invoke()
                }
                return true
            }
        }
        
        return false
    }
    
    override fun onDown(e: MotionEvent): Boolean {
        // Return true to indicate that we want to handle the gesture
        return false
    }
}

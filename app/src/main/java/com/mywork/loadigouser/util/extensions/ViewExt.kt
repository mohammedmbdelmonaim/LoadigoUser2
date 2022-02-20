package com.mywork.loadigouser.util.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Service
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard() {
    (this.context?.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context?.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}

fun View.setLocationSmooth(newX: Float, newY: Float, smoothDuration: Long) {
    val set = AnimatorSet()
    val xAnim = ObjectAnimator.ofFloat(this, View.X, x, newX)
    val yAnim = ObjectAnimator.ofFloat(this, View.Y, y, newY)
    set.playTogether(xAnim, yAnim)
    set.duration = smoothDuration
    set.start()
}

fun View.offsetXYSmooth(offsetX: Float, offsetY: Float, smoothDuration: Long) {
    setLocationSmooth(this.x + offsetX, this.y + offsetY, smoothDuration)
}

fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    this.apply {
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            val p = layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            requestLayout()
        }
    }
}
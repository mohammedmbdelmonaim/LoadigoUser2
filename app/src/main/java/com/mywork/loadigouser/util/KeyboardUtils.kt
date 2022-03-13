package com.mywork.loadigouser.util

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtils {
    @JvmStatic
    fun showSoftKeyboard(context: Context, view: EditText): Unit {
        Handler(Looper.getMainLooper()).postDelayed({
            view.requestFocus()
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                view.applicationWindowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        }, 500)
    }

    /**
     * show keyboard
     * @author Hady Ahmed
     */
    @JvmStatic
    fun showKeyboard(view: View) {
        view.requestFocus()
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity) {
        val inputManager =
            activity.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager!!.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    /**
     * hide keyboard
     * @author Hady Ahmed
     */
    @JvmStatic
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
package com.mywork.loadigouser.util.extensions

import android.app.Activity
import android.content.DialogInterface
import android.content.res.Resources
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

object BottomSheetDialogUtils {
    fun setHeightAndState(dialogInterface: DialogInterface?): BottomSheetBehavior<*> {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels.minus(250)
        return bottomSheetBehavior

    }

    private fun getWindowHeight(activity: Activity): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        activity.resources.displayMetrics.heightPixels
        return displayMetrics.heightPixels
    }
}
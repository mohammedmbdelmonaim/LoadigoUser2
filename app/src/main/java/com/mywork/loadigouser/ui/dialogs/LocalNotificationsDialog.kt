package com.mywork.loadigouser.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mywork.loadigouser.R


enum class LocalNotificationsType { SUCCESS, INFO, WARNING, ERROR }

class LocalNotificationsDialog(
    private val type: LocalNotificationsType = LocalNotificationsType.INFO,
    private val text: String?,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        this.dialog?.window?.setGravity(Gravity.BOTTOM)
        val p = this.dialog?.window?.attributes
        p?.width = ViewGroup.LayoutParams.MATCH_PARENT
        p?.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        p?.x = 0
        this.dialog?.window?.attributes = p
        val view = inflater.inflate(R.layout.local_notification_layout, container, false)

        when (this.type) {
            LocalNotificationsType.SUCCESS -> {
                this.dialog?.window?.setBackgroundDrawable(
                    ResourcesCompat.getDrawable(
                        this.resources,
                        R.drawable.bg_success_large_radius,
                        this.requireActivity().theme
                    )
                )
            }
            LocalNotificationsType.INFO -> {
                this.dialog?.window?.setBackgroundDrawable(
                    ResourcesCompat.getDrawable(
                        this.resources,
                        R.drawable.bg_info_large_radius,
                        this.requireActivity().theme
                    )
                )
            }
            LocalNotificationsType.WARNING -> {
                this.dialog?.window?.setBackgroundDrawable(
                    ResourcesCompat.getDrawable(
                        this.resources,
                        R.drawable.bg_warning_large_radius,
                        this.requireActivity().theme
                    )
                )
            }
            LocalNotificationsType.ERROR -> {
                this.dialog?.window?.setBackgroundDrawable(
                    ResourcesCompat.getDrawable(
                        this.resources,
                        R.drawable.bg_error_large_radius,
                        this.requireActivity().theme
                    )
                )
            }
        }
        this.dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        this.dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 0, 0, 0, 240)
        dialog!!.window!!.setBackgroundDrawable(inset)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.LocalNotificationDialogTheme)
        if (text.isNullOrBlank()) {
            dismissAllowingStateLoss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialTextView>(R.id.toast_text).text = this.text
        val background = view.findViewById<LinearLayoutCompat>(R.id.toast_root)
        background.setOnClickListener {
            this.dismiss()
        }
        val image = view.findViewById<AppCompatImageView>(R.id.toast_icon)

        when (this.type) {
            LocalNotificationsType.SUCCESS -> {
                background.background = ResourcesCompat.getDrawable(
                    this.resources,
                    R.drawable.bg_success_large_radius,
                    this.requireActivity().theme
                )
                image.setBackgroundResource(R.drawable.ic_correct_notification)
            }
            LocalNotificationsType.INFO -> {
                background.background = ResourcesCompat.getDrawable(
                    this.resources,
                    R.drawable.bg_info_large_radius,
                    this.requireActivity().theme
                )
                image.setBackgroundResource(R.drawable.ic_info_notification)
            }
            LocalNotificationsType.WARNING -> {
                background.background = ResourcesCompat.getDrawable(
                    this.resources,
                    R.drawable.bg_warning_large_radius,
                    this.requireActivity().theme
                )
                image.setBackgroundResource(R.drawable.ic_info_notification)
            }
            LocalNotificationsType.ERROR -> {
                background.background = ResourcesCompat.getDrawable(
                    this.resources,
                    R.drawable.bg_error_large_radius,
                    this.requireActivity().theme
                )
                image.setBackgroundResource(R.drawable.ic_error_notification)
            }
        }

        hideAfter5Sec()
    }

    private fun hideAfter5Sec() {
        this.requireContext().let { context ->
            Handler(context.mainLooper).postDelayed({
                this.activity?.let {
                    if (it.isFinishing.not()) {
                        if (this.isVisible && this.isStateSaved.not()) {
                            dismiss()
                        }
                    }
                }
            }, 5000)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(this.requireActivity(), this.theme) {
            override fun onBackPressed() {}
        }
        dialog.setOnCancelListener {
            if (this.requireActivity().isFinishing.not()) {
                it.dismiss()
            }
        }
        return dialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (this.text != "Job was cancelled") {
                super.show(manager, tag)
            }
        } catch (error: IllegalStateException) {
            FirebaseCrashlytics.getInstance().recordException(error)
        }
    }
}

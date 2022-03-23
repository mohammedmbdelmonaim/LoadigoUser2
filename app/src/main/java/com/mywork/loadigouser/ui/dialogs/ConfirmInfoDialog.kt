package com.mywork.loadigouser.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.DialogConfirmInfoBinding

class ConfirmInfoDialog(
    context: Context,
    private val title: String? = null,
    private val info: String? = null,
    private val positiveText: String = "",
    private val showPositiveButton: Boolean = true,
    private val positiveClickAction: () -> Unit = {},
    private val negativeText: String = "",
    private val showNegativeButton: Boolean = true,
    private val negativeClickAction: () -> Unit = {},
    private val isCancelable: Boolean = true,
    private val imageType: DialogImageType = DialogImageType.CONFIRM,
    private val positiveBackgroundColor: Int? = null,
    private val positiveTextColor: Int? = null,
    private val negativeBackgroundColor: Int? = null,
    private val negativeBorderColor: Int? = null,
    private val negativeTextColor: Int? = null,
    private val negativeTextSize: Float? = null,
    private val positiveTextSize: Float? = null,
) : Dialog(context) {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun show() {
        val binding = DialogConfirmInfoBinding.inflate(LayoutInflater.from(this.context))
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.setCancelable(this.isCancelable)

        binding.apply {
            this.dialogText.apply {
                isVisible = !this@ConfirmInfoDialog.info.isNullOrEmpty()
                text = this@ConfirmInfoDialog.info
            }
            this.dialogTitle.apply {
                isVisible = false
                text = ""
            }

            this.btnPositive.apply {
                positiveBackgroundColor?.apply {
                    btnPositive.backgroundTintList =
                        ContextCompat.getColorStateList(context, positiveBackgroundColor)
                }
                positiveTextColor?.apply {
                    btnPositive.setTextColor(context.getColor(positiveTextColor))
                }
                this.text = this@ConfirmInfoDialog.positiveText
                this.isVisible = this@ConfirmInfoDialog.showPositiveButton
                this.setOnClickListener {
                    this@ConfirmInfoDialog.positiveClickAction.invoke()
                    this@ConfirmInfoDialog.dismiss()
                }
            }

            this.btnNegative.apply {
                negativeBackgroundColor?.apply {
                    btnNegative.backgroundTintList =
                        ContextCompat.getColorStateList(context, negativeBackgroundColor)
                }
                negativeBorderColor?.apply {
                    strokeColor = ContextCompat.getColorStateList(context, negativeBorderColor)
                }
                negativeTextColor?.apply {
                    btnNegative.setTextColor(context.getColor(negativeTextColor))
                    btnNegative.setStrokeColorResource(negativeTextColor)
                }
                this.text = this@ConfirmInfoDialog.negativeText
                this@ConfirmInfoDialog.showNegativeButton.apply {
                    isVisible = this
                    if (this.not()) binding.btnPositive.layoutParams.width =
                        ViewGroup.LayoutParams.MATCH_PARENT
                }

                this.setOnClickListener {
                    this@ConfirmInfoDialog.negativeClickAction.invoke()
                    this@ConfirmInfoDialog.dismiss()
                }

                positiveTextSize?.apply { btnPositive.textSize = this }
                negativeTextSize?.apply { btnNegative.textSize = this }
            }

            val imageResource = when (imageType) {
                DialogImageType.CONFIRM -> R.drawable.ic_warning
                DialogImageType.SUCCESS -> R.drawable.vc_check_complete
                DialogImageType.NONE -> null
            }

            if(imageResource == null ){
                binding.ivInfoType.isVisible = false
            }else {
                binding.ivInfoType.setImageResource(imageResource)
            }


            super.show()
        }
    }
    enum class DialogImageType { CONFIRM, SUCCESS, NONE }
}

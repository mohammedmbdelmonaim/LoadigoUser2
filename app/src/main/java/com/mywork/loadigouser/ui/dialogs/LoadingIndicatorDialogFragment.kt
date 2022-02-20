package com.mywork.loadigouser.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.mywork.loadigouser.R


class LoadingIndicatorDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.general_loading_indicator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.setStyle(STYLE_NO_FRAME, android.R.style.Theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(this.requireActivity(), this.theme) {
            override fun onBackPressed() {}
        }
    }


    override fun onResume() {
        super.onResume()
        val animationView = this.requireView().findViewById<LottieAnimationView>(R.id.progressAnimationView)
        animationView.playAnimation()
    }

    override fun onPause() {
        super.onPause()
        val animationView = this.requireView().findViewById<LottieAnimationView>(R.id.progressAnimationView)
        animationView.pauseAnimation()
    }


    companion object{
         private lateinit  var  loadingInstance : LoadingIndicatorDialogFragment
       fun getInstance() : LoadingIndicatorDialogFragment{
            if(!this::loadingInstance.isInitialized  ){
                loadingInstance =   LoadingIndicatorDialogFragment()
            }
           return  loadingInstance
        }
    }
}
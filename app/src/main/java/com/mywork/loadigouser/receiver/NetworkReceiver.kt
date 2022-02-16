package com.mywork.loadigouser.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.Gravity
import com.mywork.loadigouser.util.NetworkInfo
import com.thecode.aestheticdialogs.*

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NetworkReceiver @Inject constructor(private val networkInfo: NetworkInfo)  : BroadcastReceiver(){
    var status: Int = 0
    override fun onReceive(context: Context?, p1: Intent?) {

//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast. throw new UnsupportedOperationException("Not yet implemented");
        status = networkInfo.getConnectionType()
        if (status == 0) {
            getErrorDialog(context)
        }
    }

    private fun getErrorDialog(context: Context?) {
        AestheticDialog.Builder(context as Activity, DialogStyle.FLAT, DialogType.ERROR)
            .setTitle("حدث خطأ")
            .setMessage("من فضلك تأكد من الأتصال بالأنترنت اولا")
            .setCancelable(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    if (status == 0)
                        (context as Activity).finish()
                }
            })
            .show()
    }
}
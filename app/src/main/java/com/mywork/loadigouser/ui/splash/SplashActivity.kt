package com.mywork.loadigouser.ui.splash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseActivity
import com.mywork.loadigouser.ui.boarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        goToSecondActivityAfterDelay()
    }

    private fun goToSecondActivityAfterDelay(){
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity , OnBoardingActivity::class.java))
            finish()
        }
    }

    private fun showDialogChoosingUpdate(){
        AlertDialog.Builder(this)
            .setTitle("تحديث جديد")
            .setMessage("يوجد تحديث ضروري من فضلك قم بتحديث التطبيق")
            .setCancelable(false)
            .setPositiveButton("تحديث") { dialog, _ ->
                dialog.cancel()

                try {
                    startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("https://www.yalla.online/download")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("https://www.yalla.online/download")
                        )
                    )
                }

            }
            .setNegativeButton("") { dialog, _ ->
                dialog.cancel()
                finish()
            }.show()
    }
}
package com.mywork.loadigouser.ui.splash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseActivity
import com.mywork.loadigouser.model.locale.SharedPreferenceCache
import com.mywork.loadigouser.ui.auth.AuthActivity
import com.mywork.loadigouser.ui.boarding.OnBoardingActivity
import com.mywork.loadigouser.ui.dialogs.LoadingIndicatorDialogFragment
import com.mywork.loadigouser.ui.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    var loadingIndicator: LoadingIndicatorDialogFragment =
        LoadingIndicatorDialogFragment.getInstance()

    @Inject
    lateinit var sharedPreferenceCache: SharedPreferenceCache
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.loadingIndicator.showNow(supportFragmentManager, "")

        setLanguage(sharedPreferenceCache.getLanguage())

        if (sharedPreferenceCache.getAuthToken()
                .isNullOrEmpty().not() && sharedPreferenceCache.getUser().isNullOrEmpty().not()
        )
            goToHome()
        else if (sharedPreferenceCache.getAuthToken()
                .isNullOrEmpty() && sharedPreferenceCache.getHasSeenOnBoardingScreens()!!
        )
            goToAuth()
        else
            goToOnBoarding()
    }

    override fun onResume() {
        super.onResume()
        try {
            this.loadingIndicator.dismissAllowingStateLoss()
        } catch (error: Throwable) {
            error.printStackTrace()
        }
    }

    private fun goToOnBoarding() {
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
            finish()
        }
    }

    private fun goToAuth() {
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            finish()
        }
    }

    private fun goToHome() {
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, UserActivity::class.java))
            finish()
        }
    }


    private fun showDialogChoosingUpdate() {
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
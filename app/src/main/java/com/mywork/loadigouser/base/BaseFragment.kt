package com.mywork.loadigouser.base

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mywork.loadigouser.ui.dialogs.LoadingIndicatorDialogFragment
import com.mywork.loadigouser.model.locale.SharedPreferenceCache
import com.mywork.loadigouser.model.locale.User
import com.mywork.loadigouser.ui.dialogs.LocalNotificationsDialog
import com.mywork.loadigouser.ui.dialogs.LocalNotificationsType
import com.mywork.loadigouser.ui.splash.SplashActivity
import com.mywork.loadigouser.util.LocalNotificationType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface LoadingIndicator {
    var loadingIndicator: LoadingIndicatorDialogFragment
    var isLoadingIndicatorOpen: Boolean
    fun showLoadingIndicator()
    fun hideLoadingIndicator()
}

interface OnUnauthorized {
    fun onUnauthorized()
}


@AndroidEntryPoint
open class BaseFragment : Fragment(), LoadingIndicator,
    OnUnauthorized {

    @Inject
    lateinit var sharedPreferenceCache: SharedPreferenceCache

    @Inject
    lateinit var user: User

    override fun onDestroy() {
        super.onDestroy()
        this.hideLoadingIndicator()

    }

    override fun onDetach() {
        super.onDetach()
        this.hideLoadingIndicator()
    }

    // LoadingIndicator
    override var loadingIndicator: LoadingIndicatorDialogFragment = LoadingIndicatorDialogFragment.getInstance()
    override var isLoadingIndicatorOpen: Boolean = false

    override fun showLoadingIndicator() {
        if (!loadingIndicator.isAdded) {
            Handler(Looper.getMainLooper()).post {
                runCatching {
                    this.loadingIndicator.showNow(this.parentFragmentManager, "")
                }.onFailure { it.printStackTrace() }
            }
        }
    }

    override fun hideLoadingIndicator() {
        if (loadingIndicator.isAdded) {
            try {
                this.loadingIndicator.dismissAllowingStateLoss()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    // OnUnauthorized
    override fun onUnauthorized() {
        sharedPreferenceCache.saveAuthToken("")
        val intent = Intent(requireActivity() , SplashActivity::class.java)
        startActivity(intent)
        (this.requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
        this.requireActivity().finish()
    }

    fun showLocalNotification(type: LocalNotificationType, message: CharSequence) {
        this.activity?.let { it as? AppCompatActivity }?.let {
            when (type) {
                LocalNotificationType.SUCCESS -> {
                    LocalNotificationsDialog(
                        LocalNotificationsType.SUCCESS,
                        message.toString()
                    ).show(it.supportFragmentManager, "LocalNotifications")
                }
                LocalNotificationType.INFO -> {
                    LocalNotificationsDialog(
                        LocalNotificationsType.INFO,
                        message.toString()
                    ).show(it.supportFragmentManager, "LocalNotifications")
                }
                LocalNotificationType.WARNING -> {
                    LocalNotificationsDialog(
                        LocalNotificationsType.WARNING,
                        message.toString()
                    ).show(it.supportFragmentManager, "LocalNotifications")
                }
                LocalNotificationType.ERROR -> {
                    LocalNotificationsDialog(
                        LocalNotificationsType.ERROR,
                        message.toString()
                    ).show(it.supportFragmentManager, "LocalNotifications")
                }
            }
        }
    }
}
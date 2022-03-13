package com.mywork.loadigouser.util.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.LocationManager
import android.provider.Settings
import android.text.InputFilter
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.ui.dialogs.LocalNotificationsDialog
import com.mywork.loadigouser.ui.dialogs.LocalNotificationsType
import com.mywork.loadigouser.util.LocalNotificationType
import java.util.*

fun Context.getApplicationVersionNumber(): String {
    return try {
        val pInfo: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        e.message.toString()
    }
}

fun Context.showLocalNotification(type: LocalNotificationType, message: CharSequence) {
    when (type) {
        LocalNotificationType.SUCCESS -> {
            LocalNotificationsDialog(
                LocalNotificationsType.SUCCESS,
                message.toString()
            ).show((this as AppCompatActivity).supportFragmentManager, "LocalNotifications")
        }
        LocalNotificationType.INFO -> {
            LocalNotificationsDialog(
                LocalNotificationsType.INFO,
                message.toString()
            ).show((this as AppCompatActivity).supportFragmentManager, "LocalNotifications")
        }
        LocalNotificationType.WARNING -> {
            LocalNotificationsDialog(
                LocalNotificationsType.WARNING,
                message.toString()
            ).show((this as AppCompatActivity).supportFragmentManager, "LocalNotifications")
        }
        LocalNotificationType.ERROR -> {
            LocalNotificationsDialog(
                LocalNotificationsType.ERROR,
                message.toString()
            ).show((this as AppCompatActivity).supportFragmentManager, "LocalNotifications")
        }
    }
}


fun Context.createMaterialCard(): MaterialCardView {
    return MaterialCardView(this).apply {
        useCompatPadding = true
        radius = resources.getDimension(R.dimen._4sdp)
        elevation = resources.getDimension(R.dimen._2sdp)
    }
}

fun Context.createSpinner(items: List<Any>): Spinner {
    return Spinner(this).apply {
        adapter = ArrayAdapter(
            this@createSpinner,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        setPadding(32, 0, 32, 0)
    }
}


fun Context.getStringByLocale(@StringRes stringRes: Int, locale: Locale): String {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration).resources.getString(stringRes)
}


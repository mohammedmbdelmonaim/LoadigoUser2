package com.mywork.loadigouser.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

@Singleton
class NetworkInfo @Inject constructor(@ApplicationContext val context:Context) {
    fun getConnectionType(): Int{
        var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                val capabilities: NetworkCapabilities? =
                    cm.getNetworkCapabilities(cm.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = 2
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = 1
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            result = 3
                        }
                    }
                }
            }
        } else {
            if (cm != null) {
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                if (activeNetwork != null) {
                    // connected to the internet
                    when {
                        activeNetwork.type === ConnectivityManager.TYPE_WIFI -> {
                            result = 2
                        }
                        activeNetwork.type === ConnectivityManager.TYPE_MOBILE -> {
                            result = 1
                        }
                        activeNetwork.type === ConnectivityManager.TYPE_VPN -> {
                            result = 3
                        }
                    }
                }
            }
        }
        return result
    }
}
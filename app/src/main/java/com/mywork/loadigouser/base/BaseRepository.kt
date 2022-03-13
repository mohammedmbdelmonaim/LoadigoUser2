package com.mywork.loadigouser.base

import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mywork.loadigouser.MainApplication
import com.mywork.loadigouser.R
import com.mywork.loadigouser.util.HttpStatus
import com.mywork.loadigouser.util.extensions.getStringByLocale
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import java.util.*

open class BaseRepository {

    protected fun <T> handleResponse(response: Response<*>): BaseResponse<T> {
        return when (response.code()) {
            HttpStatus.SC_UNAUTHORIZED -> {
//                this.onUnauthorized()
                BaseResponse()
            }
            HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                if (response.errorBody()?.contentType()?.subtype == "html") {
                    val str: String = response.errorBody()?.string() ?: ""
                    val regex = Regex("(?<=<h1>)(.*)(?=</h1>)")
                    val message = if (regex.containsMatchIn(str)) {
                        regex.find(str)!!.value
                    } else {
                        ""
                    }
                    BaseResponse(message = message)
                } else {
                    response.errorBody()?.string().extractResponseFromServerError()
                }
            }
            HttpStatus.SC_BAD_REQUEST -> {
                response.errorBody()?.string().extractResponseFromServerError()
            }

            HttpStatus.VERIFYTHING -> {
                response.errorBody()?.string().extractResponseFromServerError()
            }
            HttpStatus.SC_NOT_ACCEPTABLE -> {
                BaseResponse(message = "Server not accepting the request")
            }
            else -> {
                if (response.body() != null) {
                    try {
                            response.body() as BaseResponse<T>
                    } catch(ex: Exception) {
                        FirebaseCrashlytics.getInstance().recordException(ex)
                        BaseResponse(response.message())
                    }
                } else {
                    BaseResponse(response.message())
                }
            }
        }
    }



    protected fun <T> handleErrors(e: Throwable): BaseResponse<T> {
        FirebaseCrashlytics.getInstance().recordException(e)
        return when (e) {
            is SocketTimeoutException -> {
                BaseResponse(
                    message = MainApplication.instance.getStringByLocale(
                        R.string.no_internet_connection,
                        Locale.getDefault()
                    ), 408
                )
            }
            is CertPathValidatorException -> {
                BaseResponse(message = "Not able to trust the server", 500  )
            }
            is kotlin.coroutines.cancellation.CancellationException -> BaseResponse(
                message = "",
                403
            )
            is UnknownHostException -> BaseResponse(
                message = MainApplication.instance.getStringByLocale(
                    R.string.no_internet_connection,
                    Locale.getDefault()
                ), 504
            )
            else -> {
                BaseResponse.from(e)
            }
        }
    }

    protected fun <T> handleErrors(e: Exception): BaseResponse<T> {
        return BaseResponse.from(e)
    }
}

// this method will extract errors from 500 internal server error code
private fun <T> String?.extractResponseFromServerError(): BaseResponse<T> {
    return this?.let {
        val response = JSONObject(it)
        var message = response.getString("message")
        if (message.isEmpty().or(message.isBlank())) {
            message = ""
        }
        val statusCode = response.getInt("statusCode")
        BaseResponse(message = message, statusCode = statusCode)
    } ?: BaseResponse(
        message = "",
        statusCode = 400
    )
}
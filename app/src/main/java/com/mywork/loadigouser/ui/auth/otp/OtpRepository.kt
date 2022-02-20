package com.mywork.loadigouser.ui.auth.otp

import com.mywork.loadigouser.base.BaseRepository
import com.mywork.loadigouser.base.BaseResponse
import com.mywork.loadigouser.model.remote.api.ApiService
import com.mywork.loadigouser.model.remote.request.auth.CompleteRequest
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.model.remote.response.auth.CompleteResponse
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.model.remote.response.auth.OtpResponse
import okhttp3.internal.http2.ErrorCode
import okhttp3.internal.http2.StreamResetException
import javax.inject.Inject

class OtpRepository @Inject constructor(private val apiService: ApiService): BaseRepository() {

    suspend fun checkOtp(request: OtpRequest): BaseResponse<OtpResponse> {
        return try {
            val response = apiService.checkOtp(request)
            this.handleResponse(response)
        } catch (e: Throwable) {
            if (e is StreamResetException) {
                if (e.errorCode == ErrorCode.CANCEL) {
                    checkOtp(request)
                } else {
                    this.handleErrors(e)
                }
            } else {
                this.handleErrors(e)
            }
        } catch (e: Exception) {
            this.handleErrors(e)
        }
    }
}
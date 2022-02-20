package com.mywork.loadigouser.ui.auth.register

import com.mywork.loadigouser.base.BaseRepository
import com.mywork.loadigouser.base.BaseResponse
import com.mywork.loadigouser.model.remote.api.ApiService
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import okhttp3.internal.http2.ErrorCode
import okhttp3.internal.http2.StreamResetException
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiService: ApiService): BaseRepository() {

    suspend fun registerUser(request: LoginRequest): BaseResponse<LoginResponse> {
        return try {
            val response = apiService.login(request)
            this.handleResponse(response)
        } catch (e: Throwable) {
            if (e is StreamResetException) {
                if (e.errorCode == ErrorCode.CANCEL) {
                    registerUser(request)
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
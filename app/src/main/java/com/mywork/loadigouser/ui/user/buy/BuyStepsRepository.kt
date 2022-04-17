package com.mywork.loadigouser.ui.user.buy

import com.mywork.loadigouser.base.BaseRepository
import com.mywork.loadigouser.base.BaseResponse
import com.mywork.loadigouser.model.remote.api.ApiService
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import okhttp3.internal.http2.ErrorCode
import okhttp3.internal.http2.StreamResetException
import javax.inject.Inject

class BuyStepsRepository @Inject constructor(private val apiService: ApiService): BaseRepository() {

    suspend fun getAllServices(): BaseResponse<ServiceResponse> {
        return try {
            val response = apiService.getAllServices()
            this.handleResponse(response)
        } catch (e: Throwable) {
            if (e is StreamResetException) {
                if (e.errorCode == ErrorCode.CANCEL) {
                    getAllServices()
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
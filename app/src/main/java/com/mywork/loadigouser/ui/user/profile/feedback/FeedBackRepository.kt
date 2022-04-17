package com.mywork.loadigouser.ui.user.profile.feedback

import com.mywork.loadigouser.base.BaseRepository
import com.mywork.loadigouser.base.BaseResponse
import com.mywork.loadigouser.model.remote.api.ApiService
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import okhttp3.internal.http2.ErrorCode
import okhttp3.internal.http2.StreamResetException
import javax.inject.Inject

class FeedBackRepository @Inject constructor(private val apiService: ApiService): BaseRepository() {

    suspend fun getCategoriesByServiceId(serviceId: Int): BaseResponse<CategoriesResponse> {
        return try {
            val response = apiService.getCategoriesByServiceId(serviceId)
            this.handleResponse(response)
        } catch (e: Throwable) {
            if (e is StreamResetException) {
                if (e.errorCode == ErrorCode.CANCEL) {
                    getCategoriesByServiceId(serviceId)
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
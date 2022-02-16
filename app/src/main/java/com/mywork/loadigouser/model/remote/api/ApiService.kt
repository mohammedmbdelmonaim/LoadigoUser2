package com.mywork.loadigouser.model.remote.api
import com.mywork.loadigouser.base.BaseResponse
import com.mywork.loadigouser.model.remote.request.LoginRequest
import com.mywork.loadigouser.model.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {
    // coroutine
    @GET("user/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse<LoginResponse>>

}
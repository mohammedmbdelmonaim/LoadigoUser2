package com.mywork.loadigouser.model.remote.api
import com.mywork.loadigouser.base.BaseResponse
import com.mywork.loadigouser.model.remote.request.auth.CompleteRequest
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.model.remote.response.auth.CompleteResponse
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.model.remote.response.auth.OtpResponse
import com.mywork.loadigouser.model.remote.response.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {
    // coroutine
    @GET("user/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse<LoginResponse>>

    @GET("user/login")
    suspend fun register(@Body request: LoginRequest): Response<BaseResponse<RegisterResponse>>

    @GET("user/login")
    suspend fun complete(@Body request: CompleteRequest): Response<BaseResponse<CompleteResponse>>

    @GET("user/login")
    suspend fun checkOtp(@Body request: OtpRequest): Response<BaseResponse<OtpResponse>>

}
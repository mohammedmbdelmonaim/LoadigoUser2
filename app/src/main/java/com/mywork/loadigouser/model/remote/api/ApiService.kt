package com.mywork.loadigouser.model.remote.api
import com.mywork.loadigouser.base.BaseResponse
import com.mywork.loadigouser.model.remote.request.auth.CompleteRequest
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.request.auth.OtpRequest
import com.mywork.loadigouser.model.remote.request.auth.RegisterRequest
import com.mywork.loadigouser.model.remote.response.auth.CompleteResponse
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.model.remote.response.auth.OtpResponse
import com.mywork.loadigouser.model.remote.response.auth.RegisterResponse
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse<LoginResponse>>

    @POST("user/register/first-step")
    suspend fun register(@Body request: RegisterRequest): Response<BaseResponse<RegisterResponse>>

    @POST("user/register/second-step")
    suspend fun complete(@Body request: CompleteRequest): Response<BaseResponse<CompleteResponse>>

    @POST("user/verify/otp")
    suspend fun checkOtp(@Body request: OtpRequest): Response<BaseResponse<OtpResponse>>

    @GET("service/all")
    suspend fun getAllServices(): Response<BaseResponse<ServiceResponse>>

    @GET("category/all")
    suspend fun getAllCategories(): Response<BaseResponse<CategoriesResponse>>

}
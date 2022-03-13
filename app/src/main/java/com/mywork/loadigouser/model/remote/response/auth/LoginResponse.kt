package com.mywork.loadigouser.model.remote.response.auth

import com.google.gson.annotations.SerializedName
import com.mywork.loadigouser.model.locale.User

class LoginResponse(
    @SerializedName("accessToken") val accessToken: String? = null,
    @SerializedName("refreshToken") val refreshToken: String? = null,
    @SerializedName("user") val user: User? = null
)
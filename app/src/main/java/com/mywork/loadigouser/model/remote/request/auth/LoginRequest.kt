package com.mywork.loadigouser.model.remote.request.auth

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("password")
    val password: String? = null
)
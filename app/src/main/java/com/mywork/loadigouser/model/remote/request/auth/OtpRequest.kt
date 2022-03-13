package com.mywork.loadigouser.model.remote.request.auth

import com.google.gson.annotations.SerializedName

class OtpRequest(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("otp")
    val otp: String? = null
)
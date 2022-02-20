package com.mywork.loadigouser.model.remote.request.auth

import com.google.gson.annotations.SerializedName

class OtpRequest(
    @SerializedName("otp")
    val otp: Int? = null
)
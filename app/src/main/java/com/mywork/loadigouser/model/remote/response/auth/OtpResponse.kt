package com.mywork.loadigouser.model.remote.response.auth

import com.google.gson.annotations.SerializedName
import com.mywork.loadigouser.model.locale.User

class OtpResponse {
    @SerializedName("user") val user: User? = null
}
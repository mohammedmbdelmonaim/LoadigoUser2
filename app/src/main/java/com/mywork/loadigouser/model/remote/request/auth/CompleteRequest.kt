package com.mywork.loadigouser.model.remote.request.auth

import com.google.gson.annotations.SerializedName

class CompleteRequest(
    @SerializedName("phone")
    val fullName: String? = null,

    @SerializedName("password")
    val email: String? = null
)
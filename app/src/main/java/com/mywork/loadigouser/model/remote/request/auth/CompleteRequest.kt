package com.mywork.loadigouser.model.remote.request.auth

import com.google.gson.annotations.SerializedName

class CompleteRequest(
    @SerializedName("full_name")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("picture_url")
    val picture_url: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("birth_date")
    val birth_date: String? = null
)
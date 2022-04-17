package com.mywork.loadigouser.model.remote.response

import com.google.gson.annotations.SerializedName

data class GetHelpModel(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Question")
    val question: String,
    @SerializedName("Answer")
    val answer: String,
    @SerializedName("visableNow")
    var visible: Boolean? = null
)
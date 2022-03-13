package com.mywork.loadigouser.model.remote.response.user

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class ServiceResponse(
    @SerializedName("services")
    val services: List<Service>? = null
)

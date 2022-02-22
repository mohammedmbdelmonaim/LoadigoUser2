package com.mywork.loadigouser.model.remote.response.user

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class ServiceResponse(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("image_url")
    val image_url: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("is_active")
    val is_active: Boolean? = null,

    @SerializedName("created_at")
    val created_at: String? = null
){
    companion object {
        var itemCallback: DiffUtil.ItemCallback<ServiceResponse> =
            object :
                DiffUtil.ItemCallback<ServiceResponse>() {
                override fun areItemsTheSame(
                    oldItem: ServiceResponse,
                    newItem: ServiceResponse
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ServiceResponse,
                    newItem: ServiceResponse
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
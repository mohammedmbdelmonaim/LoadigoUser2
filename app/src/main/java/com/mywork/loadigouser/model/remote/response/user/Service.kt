package com.mywork.loadigouser.model.remote.response.user

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class Service (
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
        var itemCallback: DiffUtil.ItemCallback<Service> =
            object :
                DiffUtil.ItemCallback<Service>() {
                override fun areItemsTheSame(
                    oldItem: Service,
                    newItem: Service
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: Service,
                    newItem: Service
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
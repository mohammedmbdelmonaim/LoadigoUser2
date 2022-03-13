package com.mywork.loadigouser.model.locale

import com.google.gson.annotations.SerializedName

class User(@SerializedName("id"                  ) var id                : Int?    = null,
           @SerializedName("full_name"           ) var fullName          : String? = null,
           @SerializedName("email"               ) var email             : String? = null,
           @SerializedName("picture_url"         ) var pictureUrl        : String? = null,
           @SerializedName("phone"               ) var phone             : String? = null,
           @SerializedName("gender"              ) var gender            : String? = null,
           @SerializedName("birth_date"          ) var birthDate         : String? = null,
           @SerializedName("social_access_token" ) var socialAccessToken : String? = null,
           @SerializedName("referral"            ) var referral          : String? = null,
           @SerializedName("provider"            ) var provider          : String? = null,
           @SerializedName("status"              ) var status            : String? = null,
           @SerializedName("created_at"          ) var createdAt         : String? = null,
           @SerializedName("updated_at"          ) var updatedAt         : String? = null,
           @SerializedName("admin_id"            ) var adminId           : String? = null

)
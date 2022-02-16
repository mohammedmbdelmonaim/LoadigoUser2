package com.mywork.loadigouser.model.locale
import android.content.SharedPreferences

import androidx.core.content.edit
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

class SharedPreferenceCache {
    @Singleton
    class SharedPreferenceCache @Inject constructor(private val sharedPreferences: SharedPreferences){

        fun saveLanguage(lang:String){
            sharedPreferences.edit{putString("lang" , lang)}
        }

        fun getLanguage() : String? = sharedPreferences.getString("lang" ,"ar")

        fun saveUser(user: User?){
            val userGson = Gson().toJson(user)
            sharedPreferences.edit{putString("user" , userGson)}

        }

        fun getUser() : String? = sharedPreferences.getString("user" , null)



    }
}
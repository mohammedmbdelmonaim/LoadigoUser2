package com.mywork.loadigouser.model.locale

import android.content.SharedPreferences

import androidx.core.content.edit
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceCache @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveHasSeenOnBoardingScreens(seen: Boolean) {
        sharedPreferences.edit { putBoolean("seen", seen) }
    }

    fun getHasSeenOnBoardingScreens(): Boolean? = sharedPreferences.getBoolean("seen", false)

    fun saveLanguage(lang: String) {
        sharedPreferences.edit { putString("lang", lang) }
    }

    fun getLanguage(): String? = sharedPreferences.getString("lang", "ar")

    fun saveAuthToken(token: String) {
        sharedPreferences.edit { putString("token", token) }
    }

    fun getAuthToken(): String? = sharedPreferences.getString("token", "")

    fun saveUser(user: User?) {
        val userGson = Gson().toJson(user)
        sharedPreferences.edit { putString("user", userGson) }

    }

    fun getUser(): String? = sharedPreferences.getString("user", null)


}

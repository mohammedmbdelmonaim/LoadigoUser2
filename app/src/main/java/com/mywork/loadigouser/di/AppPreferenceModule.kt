package com.mywork.loadigouser.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.mywork.loadigouser.model.locale.SharedPreferenceCache
import com.mywork.loadigouser.model.locale.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.jetbrains.annotations.Nullable
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppPreferenceModule {
    @Provides
    @Singleton
    fun providePreference(@ApplicationContext context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideUser(sharedPreferences: SharedPreferenceCache): User {
        val userGson = sharedPreferences.getUser()
        return Gson().fromJson(userGson, User::class.java)
    }
}
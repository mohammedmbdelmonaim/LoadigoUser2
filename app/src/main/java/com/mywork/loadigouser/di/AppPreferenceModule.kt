package com.mywork.loadigouser.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppPreferenceModule {
    @Provides
    @Singleton
    fun providePreference(@ApplicationContext context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}
package com.mywork.loadigouser.di

import com.mywork.loadigouser.model.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit
@InstallIn(ActivityRetainedComponent::class)
@Module
class ApiServiceModule {
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
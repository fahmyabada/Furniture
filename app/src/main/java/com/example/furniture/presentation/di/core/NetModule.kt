package com.example.furniture.presentation.di.core

import com.example.furniture.data.api.Api
import com.example.furniture.data.api.HeaderInterceptor
import com.example.furniture.data.api.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Singleton
    @Provides
    fun provideRetrofit(headerInterceptor: HeaderInterceptor,sessionManager: SessionManager): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://backend.forhomi.com/api/")
            .client(headerInterceptor.intercept(sessionManager))
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

}


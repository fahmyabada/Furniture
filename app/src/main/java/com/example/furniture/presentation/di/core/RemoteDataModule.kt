package com.example.furniture.presentation.di.core

import com.example.furniture.data.api.Api
import com.example.furniture.data.repository.login.LoginRemoteDataSource
import com.example.furniture.data.repository.login.LoginRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// for all data source remote
@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideGetLoginRemoteDataSource(api: Api): LoginRemoteDataSource {
        return LoginRemoteDataSourceImpl(api)
    }


}

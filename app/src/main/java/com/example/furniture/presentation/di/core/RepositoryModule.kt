package com.example.furniture.presentation.di.core


import com.example.furniture.data.repository.login.LoginRemoteDataSource
import com.example.furniture.data.repository.login.LoginRepositoryImpl
import com.example.furniture.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// for all main repository of data source

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository(loginRemoteDataSource: LoginRemoteDataSource): LoginRepository {
        return LoginRepositoryImpl(loginRemoteDataSource)
    }


}
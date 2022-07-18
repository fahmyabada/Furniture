package com.example.furniture.presentation.di.core

import android.app.Application
import com.example.furniture.domain.usecase.login.LoginUseCase
import com.example.furniture.presentation.CheckNetworkAvailable
import com.example.furniture.presentation.ui.login.LoginViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideLoginViewModelFactory(
        application: Application,
        loginUseCase: LoginUseCase,
        checkNetworkAvailable: CheckNetworkAvailable
    ): LoginViewModelFactory {
        return LoginViewModelFactory(
            application,
            loginUseCase,
            checkNetworkAvailable
        )
    }

}
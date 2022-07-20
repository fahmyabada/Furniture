package com.example.furniture.presentation.di.core

import android.app.Application
import com.example.furniture.domain.usecase.home.FurnitureNearbyUseCase
import com.example.furniture.domain.usecase.home.GetHomeUseCase
import com.example.furniture.domain.usecase.login.LoginUseCase
import com.example.furniture.presentation.CheckNetworkAvailable
import com.example.furniture.presentation.ui.home.HomeViewModelFactory
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


    @Singleton
    @Provides
    fun provideHomeViewModelFactory(
        application: Application,
        getHomeUseCase: GetHomeUseCase,
        furnitureNearbyUseCase: FurnitureNearbyUseCase,
        checkNetworkAvailable: CheckNetworkAvailable
    ): HomeViewModelFactory {
        return HomeViewModelFactory(
            application,
            getHomeUseCase,
            furnitureNearbyUseCase,
            checkNetworkAvailable
        )
    }
}
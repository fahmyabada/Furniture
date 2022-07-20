package com.example.furniture.presentation.di.core


import com.example.furniture.domain.repository.HomeRepository
import com.example.furniture.domain.repository.LoginRepository
import com.example.furniture.domain.usecase.home.FurnitureNearbyUseCase
import com.example.furniture.domain.usecase.home.GetHomeUseCase
import com.example.furniture.domain.usecase.login.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// for all use case

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepository): LoginUseCase {
        return LoginUseCase(loginRepository)
    }

    @Singleton
    @Provides
    fun provideGetHomeUseCase(homeRepository: HomeRepository): GetHomeUseCase {
        return GetHomeUseCase(homeRepository)
    }

    @Singleton
    @Provides
    fun provideFurnitureNearbyUseCase(homeRepository: HomeRepository): FurnitureNearbyUseCase {
        return FurnitureNearbyUseCase(homeRepository)
    }
}
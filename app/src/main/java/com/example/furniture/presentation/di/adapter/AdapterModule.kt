package com.example.furniture.presentation.di.adapter


import com.example.furniture.presentation.ui.home.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {


    @Singleton
    @Provides
    fun provideCustomHolderCategory(): CustomHolderCategory {
        return CustomHolderCategory()
    }

    @Singleton
    @Provides
    fun provideCustomHolderOffersSaves(): CustomHolderOffersSaves {
        return CustomHolderOffersSaves()
    }

    @Singleton
    @Provides
    fun provideCustomHolderSaves(): CustomHolderSaves {
        return CustomHolderSaves()
    }

    @Singleton
    @Provides
    fun provideCustomHolderDiscount(): CustomHolderDiscount {
        return CustomHolderDiscount()
    }

    @Singleton
    @Provides
    fun provideCustomHolderFurnitureNearby(): CustomHolderFurnitureNearby {
        return CustomHolderFurnitureNearby()
    }
}
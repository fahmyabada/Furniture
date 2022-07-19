package com.example.furniture.presentation.di.adapter


import com.example.furniture.presentation.ui.home.CustomHolderCategory
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

}
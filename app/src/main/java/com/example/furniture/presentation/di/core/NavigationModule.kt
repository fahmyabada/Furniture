package com.example.furniture.presentation.di.core

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.furniture.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class NavigationModule {

    @Singleton // single instance in application
    @Provides
    @Named("NavController") // name in case of conflict
    fun provideNavController(activity: Activity): NavController =
        activity.findNavController(R.id.nav_host_fragment_container_main)


}
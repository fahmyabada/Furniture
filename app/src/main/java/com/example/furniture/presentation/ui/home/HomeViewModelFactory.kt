package com.example.furniture.presentation.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.furniture.domain.usecase.home.FurnitureNearbyUseCase
import com.example.furniture.domain.usecase.home.GetHomeUseCase
import com.example.furniture.presentation.CheckNetworkAvailable

class HomeViewModelFactory(
    private val app: Application,
    private val getHomeUseCase: GetHomeUseCase,
    private val furnitureNearbyUseCase: FurnitureNearbyUseCase,
    private val checkNetworkAvailable: CheckNetworkAvailable
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            app,
            getHomeUseCase,
            furnitureNearbyUseCase,
            checkNetworkAvailable
        ) as T
    }
}
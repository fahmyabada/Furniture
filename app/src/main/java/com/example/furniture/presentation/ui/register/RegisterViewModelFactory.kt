package com.example.furniture.presentation.ui.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.furniture.domain.usecase.register.RegisterUseCase
import com.example.furniture.presentation.CheckNetworkAvailable

class RegisterViewModelFactory(
    private val app: Application,
    private val registerUseCase: RegisterUseCase,
    private val checkNetworkAvailable: CheckNetworkAvailable
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(app, registerUseCase,checkNetworkAvailable) as T
    }
}
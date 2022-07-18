package com.example.furniture.presentation.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.furniture.domain.usecase.login.LoginUseCase
import com.example.furniture.presentation.CheckNetworkAvailable

class LoginViewModelFactory(
    private val app: Application,
    private val loginUseCase: LoginUseCase,
    private val checkNetworkAvailable: CheckNetworkAvailable
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(app, loginUseCase,checkNetworkAvailable) as T
    }
}
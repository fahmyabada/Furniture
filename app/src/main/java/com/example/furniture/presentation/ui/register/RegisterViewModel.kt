package com.example.furniture.presentation.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furniture.data.model.login.Login
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.usecase.login.LoginUseCase
import com.example.furniture.domain.usecase.register.RegisterUseCase
import com.example.furniture.presentation.CheckNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(
    app: Application,
    private val registerUseCase: RegisterUseCase,
    private val checkNetworkAvailable: CheckNetworkAvailable
) : AndroidViewModel(app) {

    val registerItem: MutableLiveData<Resource<Login>> = MutableLiveData()

    fun getRegister(paramsMap: Map<String, String>) = viewModelScope.launch(Dispatchers.IO) {
        registerItem.postValue(Resource.Loading())
        try {
            if (checkNetworkAvailable.isNetworkAvailable()) {
                val apiResult = registerUseCase.makeRegister(paramsMap)
                registerItem.postValue(apiResult)
            } else {
                registerItem.postValue(Resource.Error("internet not available"))
            }
        }catch (e:Exception){
            registerItem.postValue(Resource.Error(e.message.toString()))
        }
    }

}
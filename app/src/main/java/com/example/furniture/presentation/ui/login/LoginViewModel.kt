package com.example.furniture.presentation.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.furniture.data.model.login.Login
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.usecase.login.LoginUseCase
import com.example.furniture.presentation.CheckNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    app: Application,
    private val loginUseCase: LoginUseCase,
    private val checkNetworkAvailable: CheckNetworkAvailable
) : AndroidViewModel(app) {

    val loginItem: MutableLiveData<Resource<Login>> = MutableLiveData()

    fun getLogin(paramsMap: Map<String, String>) = viewModelScope.launch(Dispatchers.IO) {
        loginItem.postValue(Resource.Loading())
        try {
            if (checkNetworkAvailable.isNetworkAvailable()) {
                val apiResult = loginUseCase.makeLogin(paramsMap)
                loginItem.postValue(apiResult)
            } else {
                loginItem.postValue(Resource.Error("internet not available"))
            }
        }catch (e:Exception){
            loginItem.postValue(Resource.Error(e.message.toString()))
        }
    }

}
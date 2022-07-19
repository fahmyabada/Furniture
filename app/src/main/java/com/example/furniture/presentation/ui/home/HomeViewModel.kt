package com.example.furniture.presentation.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.furniture.data.model.home.Home
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.usecase.home.GetHomeUseCase
import com.example.furniture.presentation.CheckNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    app: Application,
    private val getHomeUseCase: GetHomeUseCase,
    private val checkNetworkAvailable: CheckNetworkAvailable
) : AndroidViewModel(app) {

    val homeItem: MutableLiveData<Resource<Home>> = MutableLiveData()

    fun getHome() = viewModelScope.launch(Dispatchers.IO) {
        homeItem.postValue(Resource.Loading())
        try {
            if (checkNetworkAvailable.isNetworkAvailable()) {
                val apiResult = getHomeUseCase.getHome()
                homeItem.postValue(apiResult)
            } else {
                homeItem.postValue(Resource.Error("internet not available"))
            }
        } catch (e: Exception) {
            homeItem.postValue(Resource.Error(e.message.toString()))
        }
    }

}
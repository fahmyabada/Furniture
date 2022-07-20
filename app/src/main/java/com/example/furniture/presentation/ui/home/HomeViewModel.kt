package com.example.furniture.presentation.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.furniture.data.model.furniture_nearby.Data
import com.example.furniture.data.model.furniture_nearby.FurnitureNearby
import com.example.furniture.data.model.home.Home
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.usecase.home.FurnitureNearbyUseCase
import com.example.furniture.domain.usecase.home.GetHomeUseCase
import com.example.furniture.presentation.CheckNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    app: Application,
    private val getHomeUseCase: GetHomeUseCase,
    private val furnitureNearbyUseCase: FurnitureNearbyUseCase,
    private val checkNetworkAvailable: CheckNetworkAvailable
) : AndroidViewModel(app) {

    val homeItem: MutableLiveData<Resource<Home>> = MutableLiveData()
    val furnitureNearbyItem: MutableLiveData<Resource<FurnitureNearby>> = MutableLiveData()
    val numPageViewModel: MutableLiveData<String> = MutableLiveData()
    val setData: MutableLiveData<List<Data>> = MutableLiveData()

    init {
        numPageViewModel.value = "1"
    }

    fun getHome() = viewModelScope.launch(Dispatchers.IO) {
        homeItem.postValue(Resource.Loading())
        try {
            if (checkNetworkAvailable.isNetworkAvailable()) {
                val apiResult = getHomeUseCase.execute()
                homeItem.postValue(apiResult)
            } else {
                homeItem.postValue(Resource.Error("internet not available"))
            }
        } catch (e: Exception) {
            homeItem.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun getFurnitureNearby(paramsMap: Map<String, String>) = viewModelScope.launch(Dispatchers.IO) {
        furnitureNearbyItem.postValue(Resource.Loading())
        try {
            if (checkNetworkAvailable.isNetworkAvailable()) {
                val apiResult = furnitureNearbyUseCase.execute(paramsMap)
                furnitureNearbyItem.postValue(apiResult)
            } else {
                furnitureNearbyItem.postValue(Resource.Error("internet not available"))
            }
        } catch (e: Exception) {
            furnitureNearbyItem.postValue(Resource.Error(e.message.toString()))
        }
    }

}
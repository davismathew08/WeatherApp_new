package com.example.weatherappkotlin.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappkotlin.data.api.ApiRepositoryProvider
import com.example.weatherappkotlin.model.location_details.SelectedLocationResponse
import com.example.weatherappkotlin.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private val selectedLocationDetailsResponseLiveData=
            MutableLiveData<Resource<SelectedLocationResponse>>()

    fun fetchAgentAssignedPropertyList(lat : String,lon : String,appid : String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            selectedLocationDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getWeatherDetails(lat,lon,appid).let {
                    val response=it.body()
                    if(response!=null){
                        selectedLocationDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    else{
                        selectedLocationDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }

                }

            }catch (ex:Exception){
                selectedLocationDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getSelectedLocationDetailsResponse(): LiveData<Resource<SelectedLocationResponse>> {
        return selectedLocationDetailsResponseLiveData
    }
}
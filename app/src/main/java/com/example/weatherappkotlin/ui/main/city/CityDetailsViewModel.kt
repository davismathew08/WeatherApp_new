package com.example.weatherappkotlin.ui.main.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappkotlin.data.api.ApiRepositoryProvider
import com.example.weatherappkotlin.model.place_weather_details.PlaceWeatherDetails
import com.example.weatherappkotlin.utils.Resource
import kotlinx.coroutines.launch

class CityDetailsViewModel:ViewModel() {

    private val placeDetailsResponseLiveData=
            MutableLiveData<Resource<PlaceWeatherDetails>>()

    fun fetchPlaceDetails(lat : String,lon : String,appid : String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            placeDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getPlaceWeatherDetails(lat,lon,appid).let {
                    val response=it.body()
                    if(response!=null){
                        placeDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    else{
                        placeDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }

                }

            }catch (ex:Exception){
                placeDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPlaceDetailsResponse(): LiveData<Resource<PlaceWeatherDetails>> {
        return placeDetailsResponseLiveData
    }
}
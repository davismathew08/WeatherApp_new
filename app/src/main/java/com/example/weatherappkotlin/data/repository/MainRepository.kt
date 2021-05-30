package com.example.weatherappkotlin.data.repository


import com.example.weatherappkotlin.data.api.ApiService
import com.example.weatherappkotlin.model.location_details.SelectedLocationResponse
import com.example.weatherappkotlin.model.place_weather_details.PlaceWeatherDetails
import retrofit2.Response


class MainRepository(private val apiService: ApiService) {
    suspend fun getWeatherDetails(lat : String,lon : String,appid : String): Response<SelectedLocationResponse> =
        apiService.getWeatherDetails(lat,lon,appid)

    suspend fun getPlaceWeatherDetails(lat : String,lon : String,appid : String): Response<PlaceWeatherDetails> =
            apiService.getPlaceWeatherDetails(lat,lon,appid)
}

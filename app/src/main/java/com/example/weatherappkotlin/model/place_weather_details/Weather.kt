package com.example.weatherappkotlin.model.place_weather_details

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
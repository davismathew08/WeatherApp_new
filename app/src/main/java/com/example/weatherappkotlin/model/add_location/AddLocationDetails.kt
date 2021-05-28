package com.example.weatherappkotlin.model.location_details

data class AddLocationDetails(
        val location_name:String,
        val latitude:String,
        val longitude:String,
        var bookedmarked:Boolean
)
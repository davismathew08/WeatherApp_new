package com.example.weatherappkotlin.model.realm_db

import io.realm.RealmObject

open class BookedMarked(
    var lat: String?= null,
    var lon: String?= null,
    var location_name:String?=null,
    var bookedmarked:Boolean?=null
) : RealmObject(){}
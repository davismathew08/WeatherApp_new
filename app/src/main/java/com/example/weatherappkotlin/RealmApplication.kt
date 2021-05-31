package com.example.weatherappkotlin

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import io.realm.Realm

class RealmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Realm.init(this)
    }
}
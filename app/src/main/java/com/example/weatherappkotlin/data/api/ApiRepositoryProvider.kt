package com.example.weatherappkotlin.data.api

import com.example.weatherappkotlin.data.repository.MainRepository


object ApiRepositoryProvider {
    fun providerApiRepository() : MainRepository {
        return MainRepository(ApiService.create())
    }
}
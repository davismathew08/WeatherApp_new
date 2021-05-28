package com.example.weatherappkotlin.data.api

import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.example.weatherappkotlin.model.location_details.SelectedLocationResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("onecall")
    suspend fun getWeatherDetails(@Query("lat") lat : String,
                                  @Query("lon") lon : String,
                                  @Query("appid") appid : String) :
            Response<SelectedLocationResponse>

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiService::class.java)

        }

        private fun initializeClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            System.setProperty("http.keepAlive", "false");
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .addInterceptor(SupportInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)


            return builder.build()
        }


    }
}
// ApiClient.kt
package com.example.ridesharing.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
   private const val BASE_URL = "https://geocode-maps.yandex.ru/1.x/"
   
   private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
   private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    
   val yandexGeocodingApi: YandexGeocodingApi by lazy {
       Retrofit.Builder()
           .baseUrl(BASE_URL)
           .client(okHttpClient)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
           .create(YandexGeocodingApi::class.java)
    }
}
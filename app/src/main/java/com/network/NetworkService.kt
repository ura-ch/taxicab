package com.network
import com.squareup.retrofit2.Retrofit
import com.squareup.retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
     private const val BASE_URL = "https://api.example.com/"
     private val retrofit = Retrofit.Builder()
       .baseUrl(BASE_URL)
       .addConverterFactory(GsonConverterFactory.create())
       .build()

   val api: ApiClient = retrofit.create(ApiClient::class.java)
}
package com.example.uploadimageusingretrofit.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    private val baseUrl = "http://192.168.0.38/upload-img/"

    val httpClient by lazy {
        OkHttpClient.Builder().build()
    }

    val retroInstance by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val apiService by lazy {
        retroInstance.create(ApiService::class.java)
    }
}
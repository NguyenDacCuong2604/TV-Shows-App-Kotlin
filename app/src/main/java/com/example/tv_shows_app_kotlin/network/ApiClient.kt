package com.example.tv_shows_app_kotlin.network

import com.example.tv_shows_app_kotlin.utilities.Constant.domain
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
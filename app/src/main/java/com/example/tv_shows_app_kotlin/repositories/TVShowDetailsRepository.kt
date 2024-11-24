package com.example.tv_shows_app_kotlin.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tv_shows_app_kotlin.network.APIService
import com.example.tv_shows_app_kotlin.network.ApiClient
import com.example.tv_shows_app_kotlin.responses.TVShowDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVShowDetailsRepository {

    private val apiService: APIService = ApiClient.retrofit.create(APIService::class.java)

    fun getTVShowDetails(tvShowId: String): LiveData<TVShowDetailsResponse> {
        val data = MutableLiveData<TVShowDetailsResponse>()
        apiService.getTVShowDetails(tvShowId).enqueue(object : Callback<TVShowDetailsResponse> {
            override fun onResponse(call: Call<TVShowDetailsResponse>, response: Response<TVShowDetailsResponse>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<TVShowDetailsResponse>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }
}
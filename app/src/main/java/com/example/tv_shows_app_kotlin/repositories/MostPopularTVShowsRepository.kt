package com.example.tv_shows_app_kotlin.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tv_shows_app_kotlin.network.APIService
import com.example.tv_shows_app_kotlin.network.ApiClient
import com.example.tv_shows_app_kotlin.responses.TVShowsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MostPopularTVShowsRepository {

    private val apiService: APIService = ApiClient.retrofit.create(APIService::class.java)

    fun getMostPopularTVShows(page: Int): LiveData<TVShowsResponse> {
        val data = MutableLiveData<TVShowsResponse>()
        apiService.getMostPopularTVShows(page).enqueue(object : Callback<TVShowsResponse> {
            override fun onResponse(call: Call<TVShowsResponse>, response: Response<TVShowsResponse>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<TVShowsResponse>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }
}
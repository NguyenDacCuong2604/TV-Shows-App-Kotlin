package com.example.tv_shows_app_kotlin.network

import com.example.tv_shows_app_kotlin.responses.TVShowDetailsResponse
import com.example.tv_shows_app_kotlin.responses.TVShowsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("most-popular")
    fun getMostPopularTVShows(@Query("page") page: Int): Call<TVShowsResponse>

    @GET("show-details")
    fun getTVShowDetails(@Query("q") tvShowId: String): Call<TVShowDetailsResponse>

    @GET("search")
    fun searchTVShow(@Query("q") query: String, @Query("page") page: Int): Call<TVShowsResponse>
}
package com.example.tv_shows_app_kotlin.responses

import com.example.tv_shows_app_kotlin.models.TVShow
import com.google.gson.annotations.SerializedName

data class TVShowsResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("pages")
    val totalPages: Int,

    @SerializedName("tv_shows")
    val tvShows: List<TVShow>
)
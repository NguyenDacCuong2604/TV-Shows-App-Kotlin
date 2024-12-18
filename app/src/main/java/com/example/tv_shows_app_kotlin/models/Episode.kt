package com.example.tv_shows_app_kotlin.models

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("season")
    val season: String,

    @SerializedName("episode")
    val episode: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("air_date")
    val airDate: String
)
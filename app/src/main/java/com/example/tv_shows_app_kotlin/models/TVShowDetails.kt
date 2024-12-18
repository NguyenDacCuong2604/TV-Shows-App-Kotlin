package com.example.tv_shows_app_kotlin.models

import com.google.gson.annotations.SerializedName

data class TVShowDetails(
    @SerializedName("url")
    val url: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("runtime")
    val runtime: String,

    @SerializedName("image_path")
    val imagePath: String,

    @SerializedName("rating")
    val rating: String,

    @SerializedName("genres")
    val genres: Array<String>,

    @SerializedName("pictures")
    val pictures: Array<String>,

    @SerializedName("episodes")
    val episodes: List<Episode>
)
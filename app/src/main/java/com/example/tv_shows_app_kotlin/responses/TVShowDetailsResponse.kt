package com.example.tv_shows_app_kotlin.responses

import com.example.tv_shows_app_kotlin.models.TVShowDetails
import com.google.gson.annotations.SerializedName

data class TVShowDetailsResponse(
    @SerializedName("tvShow")
    val tvShowDetails: TVShowDetails
)
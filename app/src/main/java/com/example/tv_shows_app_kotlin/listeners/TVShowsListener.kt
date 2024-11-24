package com.example.tv_shows_app_kotlin.listeners

import com.example.tv_shows_app_kotlin.models.TVShow

interface TVShowsListener {
    fun onTVShowClicked(tvShow: TVShow)
}
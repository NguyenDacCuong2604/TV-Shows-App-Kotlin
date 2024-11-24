package com.example.tv_shows_app_kotlin.listeners

import com.example.tv_shows_app_kotlin.models.TVShow

interface WatchlistListener {
    fun onTVShowClicked(tvShow: TVShow)
    fun removeTVShowFromWatchlist(tvShow: TVShow, position: Int)
}
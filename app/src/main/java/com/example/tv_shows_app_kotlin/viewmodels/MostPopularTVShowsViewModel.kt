package com.example.tv_shows_app_kotlin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tv_shows_app_kotlin.repositories.MostPopularTVShowsRepository
import com.example.tv_shows_app_kotlin.responses.TVShowsResponse

class MostPopularTVShowsViewModel : ViewModel() {

    private val mostPopularTVShowsRepository = MostPopularTVShowsRepository()

    fun getMostPopularTVShows(page: Int): LiveData<TVShowsResponse> {
        return mostPopularTVShowsRepository.getMostPopularTVShows(page)
    }
}
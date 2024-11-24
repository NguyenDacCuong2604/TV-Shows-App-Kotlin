package com.example.tv_shows_app_kotlin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tv_shows_app_kotlin.repositories.SearchTVShowRepository
import com.example.tv_shows_app_kotlin.responses.TVShowsResponse

class SearchViewModel : ViewModel() {

    private val searchTVShowRepository = SearchTVShowRepository()

    fun searchTVShow(query: String, page: Int): LiveData<TVShowsResponse> {
        return searchTVShowRepository.searchTVShow(query, page)
    }
}
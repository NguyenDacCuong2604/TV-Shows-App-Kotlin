package com.example.tv_shows_app_kotlin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tv_shows_app_kotlin.dao.TVShowsDatabase
import com.example.tv_shows_app_kotlin.models.TVShow
import com.example.tv_shows_app_kotlin.repositories.TVShowDetailsRepository
import com.example.tv_shows_app_kotlin.responses.TVShowDetailsResponse
import io.reactivex.Completable
import io.reactivex.Flowable

class TVShowDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val tvShowDetailsRepository: TVShowDetailsRepository = TVShowDetailsRepository()
    private val tvShowsDatabase: TVShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application)

    fun getTVShowDetails(tvShowId: String): LiveData<TVShowDetailsResponse> {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId)
    }

    fun addToWatchlist(tvShow: TVShow): Completable {
        return tvShowsDatabase.tvShowDao().addToWatchlist(tvShow)
    }

    fun getTVShowFromWatchlist(tvShowId: String): Flowable<TVShow> {
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId)
    }

    fun removeTVShowFromWatchlist(tvShow: TVShow): Completable {
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvShow)
    }
}
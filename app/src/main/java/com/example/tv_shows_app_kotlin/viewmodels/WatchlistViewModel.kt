package com.example.tv_shows_app_kotlin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tv_shows_app_kotlin.dao.TVShowsDatabase
import com.example.tv_shows_app_kotlin.models.TVShow
import io.reactivex.Completable
import io.reactivex.Flowable

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {
    private val tvShowsDatabase: TVShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application)

    fun loadWatchlist(): Flowable<List<TVShow>> {
        return tvShowsDatabase.tvShowDao().getWatchlist()
    }

    fun removeTVShowFromWatchlist(tvShow: TVShow): Completable {
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvShow)
    }
}
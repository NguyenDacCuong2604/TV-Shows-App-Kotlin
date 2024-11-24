package com.example.tv_shows_app_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.adapters.WatchlistAdapter
import com.example.tv_shows_app_kotlin.databinding.ActivityWatchlistBinding
import com.example.tv_shows_app_kotlin.listeners.WatchlistListener
import com.example.tv_shows_app_kotlin.models.TVShow
import com.example.tv_shows_app_kotlin.utilities.TempDataHolder
import com.example.tv_shows_app_kotlin.viewmodels.WatchlistViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WatchlistActivity : AppCompatActivity(), WatchlistListener {

    private lateinit var activityWatchlistBinding: ActivityWatchlistBinding
    private lateinit var viewModel: WatchlistViewModel
    private lateinit var watchlistAdapter: WatchlistAdapter
    private val watchlist = mutableListOf<TVShow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWatchlistBinding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist)
        doInitialization()
    }

    private fun doInitialization() {
        viewModel = ViewModelProvider(this).get(WatchlistViewModel::class.java)
        activityWatchlistBinding.imageBack.setOnClickListener { onBackPressed() }
        loadWatchlist()
    }

    private fun loadWatchlist() {
        activityWatchlistBinding.setIsLoading(true)
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(viewModel.loadWatchlist()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tvShows ->
                activityWatchlistBinding.setIsLoading(false)
                watchlist.clear()
                watchlist.addAll(tvShows)
                watchlistAdapter = WatchlistAdapter(watchlist, this)
                activityWatchlistBinding.watchlistRecyclerView.adapter = watchlistAdapter
                activityWatchlistBinding.watchlistRecyclerView.visibility = View.VISIBLE
                compositeDisposable.dispose()
            }, { throwable ->
                // Handle error
                activityWatchlistBinding.setIsLoading(false)
                compositeDisposable.dispose()
            })
        )
    }

    override fun onResume() {
        super.onResume()
        if (TempDataHolder.isWatchlistUpdated) {
            loadWatchlist()
            TempDataHolder.isWatchlistUpdated = false
        }
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }

    override fun removeTVShowFromWatchlist(tvShow: TVShow, position: Int) {
        val compositeDisposableForDelete = CompositeDisposable()
        compositeDisposableForDelete.add(viewModel.removeTVShowFromWatchlist(tvShow)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                watchlist.removeAt(position)
                watchlistAdapter.notifyItemRemoved(position)
                watchlistAdapter.notifyItemChanged(position, watchlistAdapter.itemCount)
                compositeDisposableForDelete.dispose()
            }, { throwable ->
                // Handle error
                compositeDisposableForDelete.dispose()
            })
        )
    }
}
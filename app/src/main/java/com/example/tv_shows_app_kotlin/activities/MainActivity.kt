package com.example.tv_shows_app_kotlin.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.adapters.TVShowAdapter
import com.example.tv_shows_app_kotlin.databinding.ActivityMainBinding
import com.example.tv_shows_app_kotlin.listeners.TVShowsListener
import com.example.tv_shows_app_kotlin.models.TVShow
import com.example.tv_shows_app_kotlin.viewmodels.MostPopularTVShowsViewModel

class MainActivity : AppCompatActivity(), TVShowsListener {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val viewModel: MostPopularTVShowsViewModel by viewModels()
    private val tvShows = mutableListOf<TVShow>()
    private lateinit var tvShowAdapter: TVShowAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        doInitialization()
    }

    private fun doInitialization() {
        activityMainBinding.tvShowsRecyclerView.setHasFixedSize(true)
        tvShowAdapter = TVShowAdapter(tvShows, this)
        activityMainBinding.tvShowsRecyclerView.adapter = tvShowAdapter
        activityMainBinding.tvShowsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!activityMainBinding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1
                        getMostPopularTVShows()
                    }
                }
            }
        })

        activityMainBinding.imageWatchlist.setOnClickListener {
            startActivity(Intent(applicationContext, WatchlistActivity::class.java))
        }

        activityMainBinding.imageSearch.setOnClickListener {
            startActivity(Intent(applicationContext, SearchActivity::class.java))
        }

        getMostPopularTVShows()
    }

    private fun getMostPopularTVShows() {
        toggleLoading()
        viewModel.getMostPopularTVShows(currentPage).observe(this) { mostPopularTVShowsResponse ->
            toggleLoading()
            mostPopularTVShowsResponse?.let {
                totalAvailablePages = it.totalPages
                it.tvShows?.let { shows ->
                    val oldCount = tvShows.size
                    tvShows.addAll(shows)
                    tvShowAdapter.notifyItemRangeInserted(oldCount, tvShows.size)
                }
            }
        }
    }

    private fun toggleLoading() {
        if (currentPage == 1) {
            activityMainBinding.isLoading = !(activityMainBinding.isLoading ?: false)
        } else {
            activityMainBinding.isLoadingMore = !(activityMainBinding.isLoadingMore ?: false)
        }
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }
}
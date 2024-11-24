package com.example.tv_shows_app_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.adapters.TVShowAdapter
import com.example.tv_shows_app_kotlin.databinding.ActivitySearchBinding
import com.example.tv_shows_app_kotlin.listeners.TVShowsListener
import com.example.tv_shows_app_kotlin.models.TVShow
import com.example.tv_shows_app_kotlin.viewmodels.SearchViewModel
import java.util.*

class SearchActivity : AppCompatActivity(), TVShowsListener {

    private lateinit var activitySearchBinding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private var tvShows = mutableListOf<TVShow>()
    private lateinit var tvShowAdapter: TVShowAdapter
    private var currentPage = 1
    private var totaleAvailablePages = 1
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        doInitialization()
    }

    private fun doInitialization() {
        activitySearchBinding.imageBack.setOnClickListener { onBackPressed() }
        activitySearchBinding.tvShowsRecyclerView.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        tvShowAdapter = TVShowAdapter(tvShows, this)
        activitySearchBinding.tvShowsRecyclerView.adapter = tvShowAdapter
        activitySearchBinding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                timer?.cancel()
            }

            override fun afterTextChanged(editable: Editable?) {
                if (!editable.isNullOrEmpty()) {
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            Handler(Looper.getMainLooper()).post {
                                currentPage = 1
                                totaleAvailablePages = 1
                                searchTVShow(editable.toString())
                            }
                        }
                    }, 800)
                } else {
                    tvShows.clear()
                    tvShowAdapter.notifyDataSetChanged()
                }
            }
        })
        activitySearchBinding.tvShowsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!activitySearchBinding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (activitySearchBinding.inputSearch.text.toString().isNotEmpty() && currentPage < totaleAvailablePages) {
                        currentPage += 1
                        searchTVShow(activitySearchBinding.inputSearch.text.toString())
                    }
                }
            }
        })
        activitySearchBinding.inputSearch.requestFocus()
    }

    private fun searchTVShow(query: String) {
        toggleLoading()
        viewModel.searchTVShow(query, currentPage).observe(this) { tvShowsResponse ->
            toggleLoading()
            tvShowsResponse?.let {
                totaleAvailablePages = it.totalPages
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
            activitySearchBinding.isLoading = activitySearchBinding.isLoading != true
        } else {
            activitySearchBinding.isLoadingMore = activitySearchBinding.isLoadingMore != true
        }
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java).apply {
            putExtra("tvShow", tvShow)
        }
        startActivity(intent)
    }
}
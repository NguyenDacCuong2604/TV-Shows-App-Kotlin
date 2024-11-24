package com.example.tv_shows_app_kotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.databinding.ItemContainerTvShowBinding
import com.example.tv_shows_app_kotlin.listeners.WatchlistListener
import com.example.tv_shows_app_kotlin.models.TVShow

class WatchlistAdapter(
    private val tvShows: List<TVShow>,
    private val watchlistListener: WatchlistListener
) : RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder>() {

    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val tvShowBinding = DataBindingUtil.inflate<ItemContainerTvShowBinding>(
            layoutInflater!!, R.layout.item_container_tv_show, parent, false
        )
        return TVShowViewHolder(tvShowBinding)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        holder.bindTVShow(tvShows[position])
    }

    override fun getItemCount(): Int = tvShows.size

    inner class TVShowViewHolder(private val itemContainerTvShowBinding: ItemContainerTvShowBinding) :
        RecyclerView.ViewHolder(itemContainerTvShowBinding.root) {

        fun bindTVShow(tvShow: TVShow) {
            itemContainerTvShowBinding.tvShow = tvShow
            itemContainerTvShowBinding.executePendingBindings()
            itemContainerTvShowBinding.root.setOnClickListener {
                watchlistListener.onTVShowClicked(tvShow)
            }
            itemContainerTvShowBinding.imageDelete.setOnClickListener {
                watchlistListener.removeTVShowFromWatchlist(tvShow, adapterPosition)
            }
            itemContainerTvShowBinding.imageDelete.visibility = View.VISIBLE
        }
    }
}
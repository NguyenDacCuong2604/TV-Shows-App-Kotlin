package com.example.tv_shows_app_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.databinding.ItemContainerTvShowBinding
import com.example.tv_shows_app_kotlin.listeners.TVShowsListener
import com.example.tv_shows_app_kotlin.models.TVShow

class TVShowAdapter(
    private val tvShows: List<TVShow>,
    private val tvShowsListener: TVShowsListener
) : RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder>() {

    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val tvShowBinding: ItemContainerTvShowBinding = DataBindingUtil.inflate(
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
                tvShowsListener.onTVShowClicked(tvShow)
            }
        }
    }
}
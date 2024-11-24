package com.example.tv_shows_app_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.databinding.ItemContainerEpisodeBinding
import com.example.tv_shows_app_kotlin.models.Episode

class EpisodesAdapter(private val episodes: List<Episode>) : RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ItemContainerEpisodeBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.item_container_episode, parent, false
        )
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bindEpisode(episodes[position])
    }

    override fun getItemCount(): Int = episodes.size

    inner class EpisodeViewHolder(private val binding: ItemContainerEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindEpisode(episode: Episode) {
            var title = "S"
            var season = episode.season
            if (season.length == 1) {
                season = "0$season"
            }
            var episodeNumber = episode.episode
            if (episodeNumber.length == 1) {
                episodeNumber = "0$episodeNumber"
            }
            episodeNumber = "E$episodeNumber"
            title = title.plus(season).plus(episodeNumber)

            binding.apply {
                this.title = title
                this.name = episode.name
                this.airDate = episode.airDate
            }
        }
    }
}
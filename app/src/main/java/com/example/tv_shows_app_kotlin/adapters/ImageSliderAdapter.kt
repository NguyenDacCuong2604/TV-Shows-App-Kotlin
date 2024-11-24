package com.example.tv_shows_app_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.databinding.ItemContainerSliderImageBinding

class ImageSliderAdapter(private val sliderImages: Array<String>) : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ItemContainerSliderImageBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.item_container_slider_image, parent, false
        )
        return ImageSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bindSliderImage(sliderImages[position])
    }

    override fun getItemCount(): Int = sliderImages.size

    inner class ImageSliderViewHolder(private val binding: ItemContainerSliderImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindSliderImage(imageURL: String) {
            binding.imageURL = imageURL
        }
    }
}
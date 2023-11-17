package com.example.project9

import androidx.recyclerview.widget.DiffUtil

class ImageDiffItemCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image)
            = (oldItem.imageUrl == newItem.imageUrl)
    override fun areContentsTheSame(oldItem: Image, newItem: Image) = (oldItem == newItem)
}
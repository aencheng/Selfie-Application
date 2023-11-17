package com.example.project9

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project9.databinding.ImageItemBinding

class ImagesAdapter(val context: Context)
    : ListAdapter<Image, ImagesAdapter.ImageItemViewHolder>(ImageDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ImageItemViewHolder = ImageItemViewHolder.inflateFrom(parent)
    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
    }

    class ImageItemViewHolder(val binding: ImageItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ImageItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ImageItemBinding.inflate(layoutInflater, parent, false)
                return ImageItemViewHolder(binding)
            }
        }
        // takes image url and sets it to be passed to full screen to be accessed if the image card was clicked
        var imageUrl: String = ""

        fun bind(image: Image, context: Context) {
            Glide.with(context).load(image.imageUrl).into(binding.imageView)
            binding.imageCard.setOnClickListener{
                imageUrl = image.imageUrl
                goToFullscreen()
            }
        }
        fun goToFullscreen(){
            val action = HomeFragmentDirections.actionHomeFragmentToFullscreenFragment(imageUrl)
            binding.root.findNavController().navigate(action)
        }
    }
}
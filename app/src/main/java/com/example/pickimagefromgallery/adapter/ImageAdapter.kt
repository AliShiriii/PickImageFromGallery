package com.example.pickimagefromgallery.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pickimagefromgallery.databinding.ItemImageBinding
import com.example.pickimagefromgallery.model.ImageModel

class ImageAdapter(private val list: List<ImageModel>, private val onClickListener: SetOnClickListener) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    interface SetOnClickListener {

        fun deleteImage(bitmap: Bitmap)

    }

    inner class ImageViewHolder(private val binding: ItemImageBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageModel: ImageModel) {

            binding.apply {

                pickImage.setImageBitmap(imageModel.bitmap)

                deleteImage.setOnClickListener {

                    onClickListener.deleteImage(imageModel.bitmap)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val model = list[position]

        if (model != null) {

            holder.bind(model)

        }

    }

    override fun getItemCount(): Int {

        return list.size

    }

}
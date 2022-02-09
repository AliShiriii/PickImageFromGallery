package com.example.featuremygallery.myGallery

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.featuremygallery.databinding.ItemImageBinding
import com.example.repository.model.ImageModel

class MyGalleryAdapter(private val list: List<ImageModel>, private val onClickListener: SetOnClickListener) :
    RecyclerView.Adapter<MyGalleryAdapter.ImageViewHolder>() {

    interface SetOnClickListener {

        fun deleteImage(bitmap: Bitmap)

    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
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
//
//    private val diffCallBack = object : DiffUtil.ItemCallback<List<ImageModel>>() {
//        override fun areItemsTheSame(oldItem: List<ImageModel>, newItem: List<ImageModel>): Boolean {
//
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: List<ImageModel>, newItem: List<ImageModel>): Boolean {
//
//            return oldItem == newItem
//        }
//
//
//    }
//
//    val differ = AsyncListDiffer(this, diffCallBack)

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
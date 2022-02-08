package com.example.pickimagefromgallery.view

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pickimagefromgallery.adapter.ImageAdapter
import com.example.pickimagefromgallery.databinding.FragmentPickImageBinding
import com.example.pickimagefromgallery.model.ImageModel
import com.example.pickimagefromgallery.utils.Contracts.IMAGE_CAMERA_CODE
import com.example.pickimagefromgallery.utils.Contracts.IMAGE_DIRECTORY
import com.example.pickimagefromgallery.utils.Contracts.IMAGE_GALLERY_CODE
import com.example.pickimagefromgallery.utils.Contracts.PERMISSION_CODE
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
@AndroidEntryPoint
class PickImageFragment : Fragment() {

    private var _binding: FragmentPickImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageAdapter: ImageAdapter
    private var imageModel: ImageModel? = null
    private lateinit var listImageModel: ArrayList<ImageModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentPickImageBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkRuntimePermission()
        addPhoto()
        setOnClickOnGallery()
        setOnClickOnCamera()

    }

    private fun addPhoto() {

        binding.pickImageRecyclerView.visibility = View.GONE
        binding.actions.visibility = View.GONE
        binding.checkImages.visibility = View.VISIBLE
        binding.addPhotos.visibility = View.VISIBLE

        binding.apply {

            addPhotos.setOnClickListener {

                addPhotos.visibility = View.GONE
                checkImages.visibility = View.GONE
                actions.visibility = View.VISIBLE

            }
        }

    }

    private fun setOnClickOnGallery() {

        binding.apply {

            pickImageFromGallery.setOnClickListener {
                choosePhotoFromGallery()

            }

        }
    }

    private fun setOnClickOnCamera() {

        binding.apply {

            pickImageFromCamera.setOnClickListener {

                takePhotoFromCamera()

            }

        }
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        galleryIntent.type = "image/*"

        startActivityForResult(galleryIntent, IMAGE_GALLERY_CODE)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, IMAGE_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_GALLERY_CODE) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val imageFromGallery = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        contentURI
                    )

//                    val path = saveImage(imageFromGallery)
                    Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()

                    imageModel = ImageModel(imageFromGallery)
                    listImageModel = ArrayList()

                    listImageModel.add(imageModel!!)

                    imageAdapter = ImageAdapter(listImageModel)
                    binding.pickImageRecyclerView.adapter = imageAdapter

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == IMAGE_CAMERA_CODE) {
            val imageFromCamera = data!!.extras!!.get("data") as Bitmap

            imageModel = ImageModel(imageFromCamera)
            listImageModel = ArrayList()

            listImageModel.add(imageModel!!)

            imageAdapter = ImageAdapter(listImageModel)
            binding.pickImageRecyclerView.adapter = imageAdapter

            Toast.makeText(requireActivity(), "Image Saved!", Toast.LENGTH_SHORT).show()
        }

        checkVisibility()
    }

    private fun checkVisibility() {

        if (imageModel != null) {

            binding.apply {
                pickImageRecyclerView.visibility = View.VISIBLE
                addPhotos.visibility = View.VISIBLE

                actions.visibility = View.GONE
                checkImages.visibility = View.GONE
            }

        } else if (imageModel == null) {

            binding.apply {
                pickImageRecyclerView.visibility = View.GONE
                actions.visibility = View.GONE

                checkImages.visibility = View.VISIBLE
                addPhotos.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkRuntimePermission() {

        //Check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(permission.READ_EXTERNAL_STORAGE)

                //Show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)

            }
        }
    }
}
package com.example.featuremygallery.myGallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.featuremygallery.databinding.FragmentMyGalleryBinding
import com.example.featuremygallery.utils.Contracts.IMAGE_CAMERA_CODE
import com.example.featuremygallery.utils.Contracts.IMAGE_GALLERY_CODE
import com.example.featuremygallery.utils.Contracts.PERMISSION_CODE
import com.example.repository.model.ImageModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.lang.String
import java.util.ArrayList

@AndroidEntryPoint
class MyGalleryFragment : Fragment(), MyGalleryAdapter.SetOnClickListener {

    private var _binding: FragmentMyGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var myGalleryAdapter: MyGalleryAdapter
    private var imageModel: ImageModel? = null
    private lateinit var listImageModel: ArrayList<ImageModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentMyGalleryBinding.inflate(inflater, container, false)

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
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, IMAGE_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

//        val selectedI: Uri? = data?.data
//
//        val file = File(String.valueOf(selectedI))
//        file.delete()

//        deleteImage()

        if (requestCode == IMAGE_GALLERY_CODE) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val imageFromGallery = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        contentURI
                    )

                    deleteImage(imageFromGallery)

                    imageModel = ImageModel(imageFromGallery)
                    listImageModel = ArrayList()

                    listImageModel.add(imageModel!!)

                    myGalleryAdapter = MyGalleryAdapter(listImageModel, this)
                    binding.pickImageRecyclerView.adapter = myGalleryAdapter

                    Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == IMAGE_CAMERA_CODE) {

            val imageFromCamera = data!!.extras!!.get("data") as Bitmap

            deleteImage(imageFromCamera)
            imageModel = ImageModel(imageFromCamera)
            listImageModel = ArrayList()

            listImageModel.add(imageModel!!)

            myGalleryAdapter = MyGalleryAdapter(listImageModel, this)
            binding.pickImageRecyclerView.adapter = myGalleryAdapter

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
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                //Show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)

            }
        }
    }

    override fun deleteImage(bitmap: Bitmap) {

        val file = File(String.valueOf(bitmap))
        file.delete()

        Toast.makeText(requireContext(), "image deleted", Toast.LENGTH_LONG).show()
    }
}
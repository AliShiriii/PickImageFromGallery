package com.example.pickimagefromgallery.view

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pickimagefromgallery.adapter.ImageAdapter
import com.example.pickimagefromgallery.databinding.FragmentPickImageBinding
import com.example.pickimagefromgallery.databinding.FragmentShowImageBinding
import com.example.pickimagefromgallery.model.ImageModel
import com.example.pickimagefromgallery.utils.Contracts.IMAGE_PICK_CODE
import com.example.pickimagefromgallery.utils.Contracts.PERMISSION_CODE
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PickImageFragment : Fragment() {

    private var _binding: FragmentPickImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageModel: ImageModel
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

        addPhoto()
//        addImageFromGallery()
//        addImageFromCamera()

    }

    private fun addPhoto() {

        binding.apply {

            addPhotos.setOnClickListener {
                actions.visibility = View.VISIBLE
//                addImageFromCamera()
                addImageFromGallery()
            }
        }

    }

    private fun addImageFromGallery() {

        binding.apply {

            pickImageFromGallery.setOnClickListener {

                checkRuntimePermission()

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

            } else {

                //Permission already granted
                pickImageFromGallery()
//                pickImageFromCamera()
            }

        } else {

            //System os is < Marshmallow
            pickImageFromGallery()
//            pickImageFromCamera()

        }
    }

    private fun pickImageFromGallery() {

        val pickImage = Intent(Intent.ACTION_PICK)
        pickImage.type = "Image/*"

        startActivityForResult(pickImage, IMAGE_PICK_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            imageModel = ImageModel(0, data?.data)
            listImageModel = ArrayList()

            listImageModel.add(imageModel)
            imageAdapter = ImageAdapter(listImageModel)
            binding.pickImageRecyclerView.adapter = imageAdapter

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            PERMISSION_CODE -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission popup from granted
                    pickImageFromGallery()

                } else {

                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()

                }
            }
        }

    }

//    private fun addImageFromCamera() {
//
//        binding.apply {
//
//            pickImageFromCamera.setOnClickListener {
//
//                pickImageFromCamera()
//
//            }
//
//        }
//    }
//
//    private fun pickImageFromCamera() {
//
//
//    }
}
package com.example.featureaddphoto.addPhoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.featureaddphoto.databinding.FragmentAddPhotoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPhotoFragment : Fragment() {

    private var _binding: FragmentAddPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddPhotoBinding.inflate(inflater, container, false)

        return binding.root

    }

}
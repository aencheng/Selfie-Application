package com.example.project9

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.project9.databinding.FragmentFullscreenBinding

class FullscreenFragment : Fragment() {
    val viewModel : ImageViewModel by activityViewModels()
    private var _binding: FragmentFullscreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFullscreenBinding.inflate(inflater, container, false)
        val view = binding.root
        val imageUrl = FullscreenFragmentArgs.fromBundle(requireArguments()).imageUrl

        // takes image url from frag args and uses it to upload the image in full screen.
        Glide.with(this.requireContext()).load(imageUrl).into(binding.imageView)

        // back button to navigate back
        binding.backButton.setOnClickListener{
            this.findNavController().navigate(R.id.action_fullscreenFragment_to_homeFragment)
        }
        return view
    }
}
package com.example.project9

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.project9.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    val viewModel : ImageViewModel by activityViewModels()
    val sensorViewModel : SensorViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser == null) {
            // User is signed out, clear the data
            viewModel.clearImages()
        } else {
            // User is signed in, re-initialize the data
            viewModel.initializeStorage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        sensorViewModel.initializeSensors(AccelerometerSensor(this.requireContext()))
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // observer for shake and navigates to takePhotoFragment if detected
        sensorViewModel.shaken.observe(viewLifecycleOwner, Observer {
            it?.let{
                if(it){
                    if(findNavController().currentDestination?.id != R.id.takePhotoFragment){
                        val action = HomeFragmentDirections.actionHomeFragmentToTakePhotoFragment()
                        view.findNavController().navigate(action)
                    }
                }
            }
        })

        // adapter and submits out list to it when updated
        val adapter = ImagesAdapter(this.requireContext())
        binding.rvImages.adapter = adapter
        viewModel.images.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        // sign out button
        binding.signoutButton.setOnClickListener{
            viewModel.signOut()
            goToLoginScreen()
        }
        return view
    }


    fun goToLoginScreen(){
        this.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }
}
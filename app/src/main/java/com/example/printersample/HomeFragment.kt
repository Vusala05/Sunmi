package com.example.printersample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.printersample.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home) {
  private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.printCheck.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTicketFragment())
        }

        binding.printPhoto.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPhotoFragment())
        }
    }

}
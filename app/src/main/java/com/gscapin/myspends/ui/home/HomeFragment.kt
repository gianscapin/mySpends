package com.gscapin.myspends.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gscapin.myspends.R
import com.gscapin.myspends.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)


        binding.addSpend.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_spendFragment)
        }

        binding.addEarn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_earnFragment)
        }
    }
}
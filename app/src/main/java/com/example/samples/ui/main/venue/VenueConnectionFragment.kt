package com.example.samples.ui.main.venue

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.samples.R
import com.example.samples.databinding.FragmentVenueConnectionBinding
import com.example.samples.ui.base.DataBindingFragment

class VenueConnectionFragment: DataBindingFragment<FragmentVenueConnectionBinding>(R.layout.fragment_venue_connection) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.toWifiBtn.setOnClickListener {
            findNavController().navigate(VenueConnectionFragmentDirections.actionToWifi())
        }

    }
}
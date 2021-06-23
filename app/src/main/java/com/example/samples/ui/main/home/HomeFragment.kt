package com.example.samples.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.samples.R
import com.example.samples.util.callbacks.MyMockLocationListener
import com.example.samples.ui.onboard.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var locationListener: MyMockLocationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationListener = MyMockLocationListener(requireContext(), lifecycle) { location ->
            Log.d("Home","location: $location")
        }
    }

}
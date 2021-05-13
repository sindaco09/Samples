package com.example.newandroidxcomponentsdemo.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.newandroidxcomponentsdemo.R
import com.example.newandroidxcomponentsdemo.ui.customviews.MyMockLocationListener
import com.example.newandroidxcomponentsdemo.ui.onboard.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val onboardViewModel: OnboardViewModel by activityViewModels()

    private lateinit var locationListener: MyMockLocationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationListener = MyMockLocationListener(requireContext(), lifecycle) { location ->
            Log.d("Home","location: $location")
        }
    }

}
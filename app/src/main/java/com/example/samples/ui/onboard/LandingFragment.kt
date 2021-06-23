package com.example.samples.ui.onboard

import androidx.fragment.app.activityViewModels
import com.example.samples.R
import com.example.samples.databinding.FragmentLandingBinding
import com.example.samples.databinding.FragmentLoginBinding
import com.example.samples.ui.base.DataBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingFragment : DataBindingFragment<FragmentLandingBinding>(R.layout.fragment_landing) {

    private val onboardViewModel: OnboardViewModel by activityViewModels()


}
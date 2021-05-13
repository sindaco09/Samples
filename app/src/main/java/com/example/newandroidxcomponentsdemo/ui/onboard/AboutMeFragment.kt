package com.example.newandroidxcomponentsdemo.ui.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.newandroidxcomponentsdemo.databinding.FragmentAboutMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutMeFragment : Fragment() {

    private lateinit var binding: FragmentAboutMeBinding
    private val onboardViewModel: OnboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutMeBinding.inflate(inflater).apply {
            viewModel = onboardViewModel
            lifecycleOwner = viewLifecycleOwner
            user = onboardViewModel.signUpUser
        }
        return binding.root
    }
}
package com.example.samples.ui.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.samples.databinding.FragmentUsernameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsernameFragment : Fragment() {

    private val onboardViewModel: OnboardViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            FragmentUsernameBinding.inflate(inflater).apply {
                viewModel = onboardViewModel
                lifecycleOwner = viewLifecycleOwner
                user = onboardViewModel.signUpUser
            }.root
}
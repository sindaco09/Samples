package com.indaco.auth.ui.screens.landing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.indaco.auth.R
import com.indaco.auth.databinding.FragmentLandingBinding
import com.indaco.samples.util.viewBinding

class LandingFragment: Fragment(R.layout.fragment_landing) {

    private val binding by viewBinding(FragmentLandingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.signUp.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.actionToSignup())
        }

        binding.login.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.actionToLogin())
        }
    }
}
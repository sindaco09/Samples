package com.example.samples.ui.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.samples.R
import com.example.samples.databinding.FragmentLoginBinding
import com.example.samples.ui.base.DataBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : DataBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val onboardViewModel: OnboardViewModel by activityViewModels()


}
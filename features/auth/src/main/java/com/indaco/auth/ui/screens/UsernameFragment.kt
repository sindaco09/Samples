package com.indaco.auth.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentUsernameBinding
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

class UsernameFragment : Fragment(R.layout.fragment_username) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by activityViewModels { viewModelFactory }
    private val binding by viewBinding(FragmentUsernameBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        init()
    }

    private fun init() {
        val signUpUser = authViewModel.signUpUser

        binding.usernameEt.setText(signUpUser.usernameET)

        binding.createUser.setOnClickListener {
            authViewModel.signUpUser = signUpUser.apply {
                usernameET = binding.usernameEt.text.toString()
            }
            authViewModel.onSubmitUsername()
        }
    }
}
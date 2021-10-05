package com.indaco.auth.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentPasswordBinding
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

class PasswordFragment : Fragment(R.layout.fragment_password) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by activityViewModels {viewModelFactory}
    private val binding by viewBinding(FragmentPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        init()
    }

    private fun init() {
        binding.setPassword.setOnClickListener {
            authViewModel.signUpUser = authViewModel.signUpUser.apply {
                passwordET = binding.password.text.toString()
                confirmPasswordET = binding.confirmPassword.text.toString()
            }
            authViewModel.setPasswords()
        }
    }
}
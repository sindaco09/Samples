package com.indaco.auth.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentLoginBinding
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by activityViewModels {viewModelFactory}
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        init()
    }

    private fun init() {
        val signUpUser = authViewModel.signUpUser

        with(binding) {
            username.setText(signUpUser.usernameET)

            login.setOnClickListener {
                authViewModel.signUpUser = signUpUser.apply { usernameET == username.text.toString() }
                authViewModel.onLoginClicked()
            }

            createUser.setOnClickListener {
                authViewModel.signUpUser = signUpUser.apply { usernameET == username.text.toString() }
                authViewModel.beginRegistration()
            }
        }
    }
}
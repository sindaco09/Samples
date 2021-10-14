package com.indaco.auth.ui.screens.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentLoginBinding
import com.indaco.auth.utils.google.GoogleAccountUtil
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels{viewModelFactory}
    private val binding by viewBinding(FragmentLoginBinding::bind)

    private lateinit var googleAccountUtil: GoogleAccountUtil<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.from(requireContext()).inject(this)

        googleAccountUtil = GoogleAccountUtil(this, requireContext()) {
            viewModel.signInGoogleUser(this)
//            goToHomeScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.login.setOnClickListener { viewModel.login() }

        observeData()
    }

//    private fun goToHomeScreen() =
//        findNavController().navigate(R.id.action_to_home_clear_stack)

    private fun observeData() {
        viewModel.onGoogleClicked.observe(viewLifecycleOwner) {
            if (it) googleAccountUtil.oneTapGoogleSignIn()
        }

        viewModel.loginResult.observe(viewLifecycleOwner) {
            if (it) {
//                goToHomeScreen()
                Toast.makeText(requireContext(), "go to home", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Error logging in", Toast.LENGTH_LONG).show()
            }
        }
    }
}
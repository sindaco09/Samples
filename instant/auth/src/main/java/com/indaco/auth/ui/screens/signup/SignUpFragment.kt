package com.indaco.auth.ui.screens.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentSignUpBinding
import com.indaco.auth.utils.google.GoogleAccountUtil
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

typealias State = SignUpViewModel.SignUpState

class SignUpFragment: Fragment(R.layout.fragment_sign_up) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SignUpViewModel by viewModels{viewModelFactory}
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private lateinit var googleAccountUtil: GoogleAccountUtil<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.from(requireContext()).inject(this)

        googleAccountUtil = GoogleAccountUtil(this, requireContext()) {
            viewModel.signUpGoogleUser(this)
            viewModel.user = SignUpViewModel.SignUpUser(this)
            Log.d("TAG","google user: ${this.givenName} ${this.displayName}")
        }
    }

//    private fun goToHomeScreen() =
//        findNavController().navigate(SignUpFragmentDirections.actionAuthToHome())

    private fun observeData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, observer)

        viewModel.usingGmailAccount.observe(viewLifecycleOwner, Observer(::signUpWithGoogle))

        viewModel.onGoogleClicked.observe(viewLifecycleOwner, Observer(::signUpWithGoogle))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.proceed.setOnClickListener { viewModel.proceed() }

        binding.googleSignUp.setOnClickListener { viewModel.onGoogleSignUpClicked() }

        observeData()
    }

    private fun signUpWithGoogle(value: Boolean) {
        if (value) googleAccountUtil.oneTapGoogleSignUp()
    }

    private fun stopObserving() { viewModel.stateLiveData.removeObserver(observer) }

    private val observer = Observer<SignUpViewModel.SignUpState> {
        when(it) {
            SignUpViewModel.SignUpState.EMAIL -> displayFragment(R.id.email_fragment_holder, it.fragment!!)
            SignUpViewModel.SignUpState.PASSWORD -> displayFragment(R.id.password_fragment_holder, it.fragment!!)
            SignUpViewModel.SignUpState.ACCOUNT -> displayFragment(R.id.account_fragment_holder, it.fragment!!)
            SignUpViewModel.SignUpState.COMPLETE -> stopObserving()
            SignUpViewModel.SignUpState.PROCEED -> {
//                goToHomeScreen()
                Toast.makeText(requireContext(), "Go Home", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayFragment(id: Int, fragment: Class<out Fragment>) =
        childFragmentManager.commit { replace(id, fragment.newInstance()) }
}


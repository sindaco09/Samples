package com.indaco.auth.ui.screens.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentSignUpBinding
import com.indaco.auth.utils.google.GoogleAccountUtil
import com.indaco.samples.MainGraphDirections
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
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
        }
    }

    private fun goToHomeScreen() =
        findNavController().navigate(MainGraphDirections.goToHome())

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
            State.EMAIL -> displayFragment(R.id.email_fragment_holder, it.fragment!!)
            State.PASSWORD -> displayFragment(R.id.password_fragment_holder, it.fragment!!)
            State.ACCOUNT -> displayFragment(R.id.account_fragment_holder, it.fragment!!)
            State.COMPLETE -> stopObserving()
            State.PROCEED -> goToHomeScreen()
        }
    }

    private fun displayFragment(id: Int, fragment: Class<out Fragment>) =
        childFragmentManager.commit { replace(id, fragment.newInstance()) }
}


package com.indaco.auth.ui.screens.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentPasswordBinding
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class PasswordFragment: Fragment(R.layout.fragment_password) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() }, {viewModelFactory})
    private val binding by viewBinding(FragmentPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        init()
    }

    private fun init() {
        with(binding.fieldInput) {
            addTextChangedListener(afterTextChanged = { viewModel.user.password = it.toString()})
        }

        with(binding.fieldInputTwo) {
            addTextChangedListener(afterTextChanged = { viewModel.user.confirmPassword = it.toString()})
            setOnEditorActionListener { _, _, _ ->
                viewModel.verifyField(SignUpViewModel.SignUpState.PASSWORD)
                false
            }
        }
    }
}
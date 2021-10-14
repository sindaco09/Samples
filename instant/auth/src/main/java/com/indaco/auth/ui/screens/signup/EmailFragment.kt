package com.indaco.auth.ui.screens.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentEmailBinding
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class EmailFragment: Fragment(R.layout.fragment_email) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() }, {viewModelFactory})
    private val binding by viewBinding(FragmentEmailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        init()
    }

    private fun init() {
        viewModel.fieldErrorLiveData.observe(viewLifecycleOwner,
            Observer(binding.fieldLayout::setError))

        with(binding.fieldInput) {
            setText(viewModel.user.email)
            setOnEditorActionListener { _, _, _ ->
                viewModel.verifyField(SignUpViewModel.SignUpState.EMAIL)
                false
            }
        }
    }
}
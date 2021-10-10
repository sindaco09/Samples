package com.indaco.auth.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.indaco.auth.R
import com.indaco.auth.core.hilt.Injector
import com.indaco.auth.databinding.FragmentAboutMeBinding
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import javax.inject.Inject
import kotlin.math.sign

class AboutMeFragment : Fragment(R.layout.fragment_about_me) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val binding by viewBinding(FragmentAboutMeBinding::bind)
    private val authViewModel: AuthViewModel by activityViewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        init()
    }

    private fun init() {
        val signUpUser = authViewModel.signUpUser

        with(binding) {
            age.setText(signUpUser.ageET)

            email.setText(signUpUser.emailET)

            radioGroup.setOnCheckedChangeListener { _, i ->
                signUpUser.male = i == 0
                signUpUser.female = i != 0
            }

            registerUser.setOnClickListener {
                authViewModel.signUpUser = signUpUser.apply {
                    ageET = this@with.age.text.toString()
                    emailET = email.text.toString()
                }
                authViewModel.onCreateUser()
            }
        }
    }
}
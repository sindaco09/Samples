package com.example.samples.ui.onboard.login

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.samples.R
import com.example.samples.databinding.FragmentLoginBinding
import com.example.samples.ui.base.DataBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: DataBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private lateinit var loginViewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = loginViewModel

        init()
    }

    private fun init() {
        initViews()

        observeData()
    }

    private fun initViews() {
        binding.submit.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (fieldsAreValid(email, password))
                login(email, password)
        }
    }

    private fun fieldsAreValid(email: String?, password: String?): Boolean {
        val isEmailValid = isEmailEntryValid(email)
        val isPasswordValid = isPasswordValid(password)
        return isEmailValid && isPasswordValid
    }

    private fun isEmailEntryValid(email: String?): Boolean {
        val error =  when {
            email.isNullOrBlank() -> getString(R.string.error_email_blank)
            email.isEmailInvalid() -> getString(R.string.error_email_not_email_pattern)
            else ->  null
        }
        binding.passwordLayout.error = error
        return error == null
    }

    private fun String.isEmailInvalid(): Boolean {
        return TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isPasswordValid(password: String?): Boolean {
        val error =  when {
            password.isNullOrBlank() -> getString(R.string.error_password_blank)
            password.length < 3 -> getString(R.string.error_password_short)
            password.length > 10 -> getString(R.string.error_password_long)
            password.all { it.isLetterOrDigit() } -> getString(R.string.error_password_not_alphanumeric)
            else ->  null
        }
        binding.passwordLayout.error = error
        return error == null
    }

    private fun login(email: String, password: String) {
        loginViewModel.login(email, password)
    }

    private fun observeData() {
        loginViewModel.loginResult.observe(viewLifecycleOwner) {
            if (it)
                goToMainScreen()
            else
                showError()
        }
    }

    private fun showError() {

    }

    private fun goToMainScreen() {

    }
}
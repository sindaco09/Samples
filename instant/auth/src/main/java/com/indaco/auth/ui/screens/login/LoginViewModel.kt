package com.indaco.auth.ui.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInCredential
import com.indaco.auth.data.repository.AuthRepository
import com.indaco.core.models.user.User
import com.indaco.samples.util.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val defaultDispatcher = Dispatchers.Default

    private val user: User = User()

    val onGoogleClicked = MutableLiveData(false)

    private var _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    // android:onClick functions
    fun onGoogleSignInClicked() {
        if (onGoogleClicked.value == false)
            onGoogleClicked.postValue(true)
    }

    fun login() {
        val email = user.email
        val password = user.password
        val fieldsAreValid = fieldsAreValid(email, password)
        if (fieldsAreValid) {
            viewModelScope.launch(defaultDispatcher) {
                _loginResult.postValue(repository.login(email!!, password!!))
            }
        }
    }

    private fun fieldsAreValid(email: String?, password: String?): Boolean {
        val emailIsValid = Validator.fieldIsValid(email, Validator.FieldType.EMAIL)
        val passwordIsValid = Validator.fieldIsValid(password, Validator.FieldType.PASSWORD)
        return emailIsValid.first && passwordIsValid.first
    }

    // separate viewmodel logic functions
    fun signInGoogleUser(credential: SignInCredential) {
        viewModelScope.launch (defaultDispatcher) {
            repository.setCurrentUser(credential)
        }
    }
}
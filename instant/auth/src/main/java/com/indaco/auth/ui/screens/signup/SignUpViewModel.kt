package com.indaco.auth.ui.screens.signup

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInCredential
import com.indaco.auth.data.repository.AuthRepository
import com.indaco.samples.data.models.user.User
import com.indaco.samples.util.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    enum class SignUpState(val fragment: Class<out Fragment>? = null) {
        EMAIL(EmailFragment::class.java),
        PASSWORD(PasswordFragment::class.java),
        ACCOUNT(AccountDetailsFragment::class.java),
        COMPLETE,
        PROCEED;

        fun nextState(): SignUpState {
            val nextOrdinal = ordinal + 1
            return if (nextOrdinal > COMPLETE.ordinal)
                COMPLETE
            else
                values()[nextOrdinal]
        }
    }

    private val defaultDispatcher = Dispatchers.Default

    val signUpStateFlow: StateFlow<SignUpState> = MutableStateFlow(SignUpState.EMAIL)

    val onGoogleClicked = MutableLiveData(false)

    var user: SignUpUser = SignUpUser()

    private var _fieldErrorLiveData = MutableLiveData("")
    val fieldErrorLiveData: LiveData<String> get() = _fieldErrorLiveData

    private var _typeLiveData = MutableLiveData(SignUpState.EMAIL)
    val stateLiveData: LiveData<SignUpState> get() = _typeLiveData

    private val _usingGmailAccount = MutableLiveData<Boolean>(false)
    val usingGmailAccount: LiveData<Boolean> get() = _usingGmailAccount

    fun verifyField(state: SignUpState) {
        val response = when (state) {
            SignUpState.EMAIL -> {
                val isValid = Validator.emailIsValid(user.email)
                if (isValid.first && Validator.isGmailAccount(user.email!!))
                    _usingGmailAccount.postValue(true)
                isValid
            }
            SignUpState.PASSWORD -> Validator.passwordsAreValid(user.password, user.confirmPassword)
            else -> Pair(true, null)
        }
        handleValidatorResponse(response, state.nextState())
    }

    private fun handleValidatorResponse(response: Pair<Boolean, String?>, nextState: SignUpState) {
        if (response.first) {
            _typeLiveData.postValue(nextState)
            _fieldErrorLiveData.postValue("")
        } else
            _fieldErrorLiveData.postValue(response.second)
    }

    fun proceed() {
        viewModelScope.launch(defaultDispatcher) {
            repository.registerUser(user)
            _typeLiveData.postValue(SignUpState.PROCEED)
        }
    }

    fun onGoogleSignUpClicked() {
        if (onGoogleClicked.value == false)
            onGoogleClicked.postValue(true)
    }

    fun signUpGoogleUser(credential: SignInCredential) {
        viewModelScope.launch (defaultDispatcher) {
            repository.registerUser(credential)
        }
    }

    data class SignUpUser(
        var email: String? = null,
        var password: String? = null,
        var confirmPassword: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var token: String? = null
    ) {
        fun toUser(): User =
            User(
                username = "$firstName $lastName",
                email = email,
                password = password,
                token = token,
            )

        constructor(credential: SignInCredential): this(
            email = credential.id,
            firstName = credential.givenName,
            lastName = credential.familyName,
            token = credential.googleIdToken
        )
    }
}
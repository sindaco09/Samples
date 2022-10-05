package com.indaco.samples.ui.onboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indaco.samples.data.repository.UserRepository
import com.indaco.samples.data.models.user.SignUpUser
import com.indaco.samples.data.models.user.User
import com.indaco.samples.ui.main.HomeFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    enum class State(val fragment: Class<out Fragment>) {
        HOME(HomeFragment::class.java),
        LOGIN (LoginFragment::class.java),
        REGISTER_USERNAME(UsernameFragment::class.java),
        REGISTER_PASSWORD(PasswordFragment::class.java),
        REGISTER_ABOUT_ME(AboutMeFragment::class.java)
    }

    var fragmentState: MutableLiveData<State> = MutableLiveData<State>().apply { value =
        State.LOGIN
    }

    inner class Result(var success: Boolean, var error: String?) {
        constructor(): this (true, null)
        constructor(error: String): this(false, error)
    }

    var signUpUser: SignUpUser = SignUpUser()

    private val _onError = MutableLiveData<String?>()
    val onError: LiveData<String?> get() = _onError

    private suspend fun userExists(username: String) = userRepository.getUser(username) != null

    fun onLoginClicked() {
        viewModelScope.launch {
            val user = userRepository.getUser(signUpUser.usernameET)
            if (user != null) {
                userRepository.loginUser(user)
                changeFragmentState(State.HOME)
            } else
                _onError.value = "Failed to login. Check Spelling."
        }
    }

    private suspend fun login(username: String): Boolean {
        val user = userRepository.getUser(username)
        return if (user == null) {
            false
        } else {
            userRepository.loginUser(user)
            true
        }
    }

    fun onBackPressed(fm: FragmentManager) {
        if (fm.backStackEntryCount > 0)
            fm.popBackStack()
        else
            changeFragmentState(State.LOGIN)
    }

    fun beginRegistration() { changeFragmentState(State.REGISTER_USERNAME) }

    private fun changeFragmentState(state: State) {
        when (state) {
            State.REGISTER_USERNAME -> { }
            State.REGISTER_PASSWORD -> { }
            State.REGISTER_ABOUT_ME -> { }
            State.LOGIN -> { signUpUser = SignUpUser() }
            State.HOME -> { }
        }
        fragmentState.value = state
    }

    fun onSubmitUsername() {
        viewModelScope.launch {
            val result = usernameIsValid()
            if (result.success)
                changeFragmentState(State.REGISTER_PASSWORD)
            else
                _onError.value = "Failed to register user. ${result.error}"
        }
    }

    private suspend fun usernameIsValid(): Result {
        return when {
            signUpUser.usernameET.isEmpty() -> Result("username is empty")
            userExists(signUpUser.usernameET) -> Result("username in use.")
            else -> Result()
        }
    }

    fun setPasswords() {
        val passwordsAreValid = passwordsAreValid(signUpUser.passwordET, signUpUser.confirmPasswordET)
        if (passwordsAreValid.success) {
            changeFragmentState(State.REGISTER_ABOUT_ME)
        } else
            _onError.value = passwordsAreValid.error

    }

    private fun passwordsAreValid(password: String?, confirmPassword: String?): Result {
        return when {
            password.isNullOrEmpty() -> Result("Must enter a password")
            confirmPassword.isNullOrEmpty() -> Result( "Must confirm password")
            password != confirmPassword -> Result( "Passwords must match")
            else -> Result()
        }
    }

    fun onCreateUser() {
        viewModelScope.launch {
            val result = userDetailsValid()
            if (result.success) {
                userRepository.addUser(User(signUpUser))
                changeFragmentState(State.HOME)
            } else
                _onError.value = result.error
        }
    }

    private fun userDetailsValid(): Result {
        with(signUpUser) {
            return when {
                age == null -> Result("Must enter an age")
                gender == com.indaco.samples.data.models.user.Gender.NONE -> Result("Must select a gender")
                emailET.isNullOrEmpty() -> Result("Must enter email")
                else -> Result()
            }
        }
    }
}

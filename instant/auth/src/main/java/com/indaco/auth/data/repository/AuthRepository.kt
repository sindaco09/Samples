package com.indaco.auth.data.repository

import com.google.android.gms.auth.api.identity.SignInCredential
import com.indaco.samples.CurrentUser
import com.indaco.auth.data.storage.UserCache
import com.indaco.auth.ui.screens.signup.SignUpViewModel
import com.indaco.core.models.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userCache: UserCache
) {

    suspend fun registerUser(signUpUser: SignUpViewModel.SignUpUser) {
        val user = signUpUser.toUser()
        userCache.setCurrentUser(user)
        addUser(user)
    }

    suspend fun registerUser(credential: SignInCredential) {
        val user = credentialToUser(credential)
        userCache.setCurrentUser(user)
        addUser(user)
    }

    suspend fun login(email: String, password: String): Boolean {
        val user = userCache.getUser(email)
        return if (user != null) {
            userCache.setCurrentUser(user)
            true
        } else
            false
    }

    private val TAG: String = "UserRepository"

    // get users from DB
    suspend fun getUsers() = userCache.getUsers()

    // get user from DB
    suspend fun getUser(userName: String): User? = userCache.getUser(userName)

    // get user from DataStore
    fun getCurrentUser(): Flow<CurrentUser> = userCache.getCurrentUser()

    suspend fun setCurrentUser(user: User) = userCache.setCurrentUser(user)

    // clear user from DataStore
    suspend fun clearCurrentUser() = userCache.clearCurrentUser()

    suspend fun loginUser(user: User) = userCache.setCurrentUser(user)

    suspend fun addUser(user: User) = userCache.addUser(user)

    suspend fun setCurrentUser(user: SignInCredential) {
        setCurrentUser(credentialToUser(user))
    }

    private fun credentialToUser(user: SignInCredential) = User().apply {
        email = user.id
        username = user.displayName?: user.givenName?:"no name given"
        token = user.googleIdToken
    }
}
package com.indaco.samples.data.repository

import com.indaco.samples.CurrentUser
import com.indaco.samples.data.models.user.User
import com.indaco.samples.data.storage.user.UserCache
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userCache: UserCache
) {

    private val TAG: String = "UserRepository"

    // get users from DB
    suspend fun getUsers() = userCache.getUsers()

    // get user from DB
    suspend fun getUser(userName: String): User? = userCache.getUser(userName)

    // get user from DataStore
    fun getCurrentUser(): Flow<com.indaco.samples.CurrentUser> = userCache.getCurrentUser()

    // clear user from DataStore
    suspend fun clearCurrentUser() = userCache.clearCurrentUser()

    suspend fun loginUser(user: User) = userCache.setCurrentUser(user)

    suspend fun addUser(user: User) = userCache.addUser(user)

    suspend fun processQRCode(code: String?): Flow<String?> {
        return flow<String?> {
            delay(4_000)

            emit(code)
        }
    }

}
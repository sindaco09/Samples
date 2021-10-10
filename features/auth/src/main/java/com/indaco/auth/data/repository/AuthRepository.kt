package com.indaco.auth.data.repository

import com.indaco.samples.CurrentUser
import com.indaco.samples.data.models.user.UserDbo
import com.indaco.auth.data.storage.UserCache
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userCache: UserCache
) {

    private val TAG: String = "UserRepository"

    // get users from DB
    suspend fun getUsers() = userCache.getUsers()

    // get user from DB
    suspend fun getUser(userName: String): UserDbo? = userCache.getUser(userName)

    // get user from DataStore
    fun getCurrentUser(): Flow<CurrentUser> = userCache.getCurrentUser()

    // clear user from DataStore
    suspend fun clearCurrentUser() = userCache.clearCurrentUser()

    suspend fun loginUser(userDbo: UserDbo) = userCache.setCurrentUser(userDbo)

    suspend fun addUser(userDbo: UserDbo) = userCache.addUser(userDbo)
}
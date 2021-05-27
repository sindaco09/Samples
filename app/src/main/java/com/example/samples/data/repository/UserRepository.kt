package com.example.samples.data.repository

import com.example.samples.CurrentUser
import com.example.samples.data.models.user.User
import com.example.samples.data.storage.user.UserCache
import kotlinx.coroutines.flow.Flow
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
    fun getCurrentUser(): Flow<CurrentUser> = userCache.getCurrentUser()

    // clear user from DataStore
    suspend fun clearCurrentUser() = userCache.clearCurrentUser()

    suspend fun loginUser(user: User) = userCache.setCurrentUser(user)

    suspend fun addUser(user: User) = userCache.addUser(user)

}
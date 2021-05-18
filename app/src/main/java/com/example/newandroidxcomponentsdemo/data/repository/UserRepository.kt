package com.example.newandroidxcomponentsdemo.data.repository

import com.example.newandroidxcomponentsdemo.CurrentUser
import com.example.newandroidxcomponentsdemo.data.models.user.User
import com.example.newandroidxcomponentsdemo.data.storage.user.UserCache
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
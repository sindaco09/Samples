package com.example.newandroidxcomponentsdemo.data.storage.user

import androidx.datastore.core.DataStore
import com.example.newandroidxcomponentsdemo.CurrentUser
import com.example.newandroidxcomponentsdemo.data.models.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserCache @Inject constructor(
    private val userDataStore: DataStore<CurrentUser>,
    private val userDao: UserDao
) {

    suspend fun addUser(user: User) = userDao.addUser(user)

    suspend fun getUsers() = userDao.getAllUsers()

    suspend fun getUser(username: String) = userDao.getUser(username)

    fun getCurrentUser(): Flow<CurrentUser> = userDataStore.data

    suspend fun setCurrentUser(user: User) = userDataStore.updateData {
        it.toBuilder()
            .setFirstname(user.username)
            .setAge(user.age)
            .build()
    }

    suspend fun clearCurrentUser() = userDataStore.updateData {
        it.toBuilder().clear().build()
    }

}
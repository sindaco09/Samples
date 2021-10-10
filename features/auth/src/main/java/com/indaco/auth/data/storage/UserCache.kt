package com.indaco.auth.data.storage

import androidx.datastore.core.DataStore
import com.indaco.samples.CurrentUser
import com.indaco.samples.data.models.user.UserDbo
import com.indaco.samples.data.storage.user.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCache @Inject constructor(
    private val userDataStore: DataStore<CurrentUser>,
    private val userDao: UserDao
) {

    suspend fun addUser(userDbo: UserDbo) = userDao.addUser(userDbo)

    suspend fun getUsers() = userDao.getAllUsers()

    suspend fun getUser(username: String) = userDao.getUser(username)

    fun getCurrentUser(): Flow<CurrentUser> = userDataStore.data

    suspend fun setCurrentUser(userDbo: UserDbo) = userDataStore.updateData {
        it.toBuilder()
            .setFirstname(userDbo.username)
            .setAge(userDbo.age)
            .build()
    }

    suspend fun clearCurrentUser() = userDataStore.updateData {
        it.toBuilder().clear().build()
    }

}
package com.indaco.samples.core.hilt.dependencies

import androidx.datastore.core.DataStore
import com.indaco.samples.CurrentUser
import com.indaco.samples.core.hilt.IODispatcher
import com.indaco.samples.data.storage.user.UserDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CameraDependencies {

    fun datastoreCurrentUser(): DataStore<CurrentUser>

    fun userDao(): UserDao

    @IODispatcher
    fun dispatcher(): CoroutineDispatcher
}
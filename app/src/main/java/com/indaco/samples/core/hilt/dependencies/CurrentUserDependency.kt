package com.indaco.samples.core.hilt.dependencies

import androidx.datastore.core.DataStore
import com.indaco.samples.CurrentUser
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CurrentUserDependency {

    fun datastoreCurrentUser(): DataStore<CurrentUser>
}
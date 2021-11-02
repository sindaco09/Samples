package com.indaco.core.core.hilt.dependencies

import com.indaco.core.dao.UserDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserDaoDependency {
    fun userDao(): UserDao
}
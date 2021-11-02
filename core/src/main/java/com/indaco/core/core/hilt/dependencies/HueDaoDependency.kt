package com.indaco.core.core.hilt.dependencies

import com.indaco.core.dao.HueDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HueDaoDependency {
    fun hueDao(): HueDao
}
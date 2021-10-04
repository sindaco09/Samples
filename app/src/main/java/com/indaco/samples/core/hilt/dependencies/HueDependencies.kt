package com.indaco.samples.core.hilt.dependencies

import com.indaco.samples.data.storage.hue.HueDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HueDependencies {
    fun hueDao(): HueDao
}
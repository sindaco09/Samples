package com.indaco.samples.core.hilt.dependencies

import com.indaco.samples.core.hilt.IODispatcher
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DispatcherDependency {
    @IODispatcher
    fun dispatcher(): CoroutineDispatcher
}
package com.indaco.samples.core.hilt.dependencies

import com.indaco.samples.core.hilt.IODispatcher
import com.indaco.samples.core.room.AppDatabase
import com.indaco.samples.data.storage.bart.BartDao
import com.indaco.samples.data.storage.goal.GoalDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BartDependencies {

    fun retrofit(): Retrofit

    @IODispatcher
    fun dispatcher(): CoroutineDispatcher

    fun bartDao(): BartDao

    fun db(): AppDatabase
}
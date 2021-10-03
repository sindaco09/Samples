package com.indaco.samples.core.hilt.dependencies

import com.indaco.samples.core.room.AppDatabase
import com.indaco.samples.data.storage.goal.GoalDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GoalsDependencies {

    fun goalDao(): GoalDao

    fun db(): AppDatabase
}
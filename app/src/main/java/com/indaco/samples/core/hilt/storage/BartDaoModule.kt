package com.indaco.samples.core.hilt.storage

import com.indaco.samples.core.room.AppDatabase
import com.indaco.samples.data.storage.bart.BartDao
import com.indaco.samples.data.storage.goal.GoalDao
import com.indaco.samples.data.storage.hue.HueDao
import com.indaco.samples.data.storage.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BartDaoModule {
    @Provides
    @Singleton
    fun provideBartDao(db: AppDatabase): BartDao =
        db.provideBartDao()
}
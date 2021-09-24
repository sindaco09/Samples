package com.example.samples.core.hilt.storage

import com.example.samples.core.room.AppDatabase
import com.example.samples.data.storage.bart.BartDao
import com.example.samples.data.storage.goal.GoalDao
import com.example.samples.data.storage.hue.HueDao
import com.example.samples.data.storage.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao =
        db.provideUserDao()

    @Provides
    @Singleton
    fun provideBartDao(db: AppDatabase): BartDao =
        db.provideBartDao()

    @Provides
    @Singleton
    fun provideHueDao(db: AppDatabase): HueDao =
        db.provideHueDao()

    @Provides
    @Singleton
    fun provideGoalsDao(db: AppDatabase): GoalDao =
        db.provideGoalsDao()
}
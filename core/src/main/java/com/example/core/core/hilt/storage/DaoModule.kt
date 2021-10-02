package com.example.core.core.hilt.storage

import com.example.core.core.room.AppDatabase
import com.example.core.data.storage.dao.BartDao
import com.example.core.data.storage.dao.GoalDao
import com.example.core.data.storage.dao.HueDao
import com.example.core.data.storage.dao.UserDao
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
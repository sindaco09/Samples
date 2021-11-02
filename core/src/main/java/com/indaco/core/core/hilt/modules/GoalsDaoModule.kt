package com.indaco.core.core.hilt.modules

import com.indaco.core.core.room.AppDatabase
import com.indaco.core.dao.GoalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GoalsDaoModule {
    @Provides
    @Singleton
    fun provideGoalsDao(db: AppDatabase): GoalDao =
        db.provideGoalsDao()
}
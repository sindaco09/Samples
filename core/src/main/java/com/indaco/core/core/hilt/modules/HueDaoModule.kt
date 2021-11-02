package com.indaco.core.core.hilt.modules

import com.indaco.core.core.room.AppDatabase
import com.indaco.core.dao.HueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HueDaoModule {
    @Provides
    @Singleton
    fun provideHueDao(db: AppDatabase): HueDao =
        db.provideHueDao()
}
package com.indaco.corebart.core.hilt.modules

import com.indaco.corebart.core.room.BartAppDatabase
import com.indaco.corebart.dao.BartDao
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
    fun provideBartDao(db: BartAppDatabase): BartDao =
        db.provideBartDao()
}
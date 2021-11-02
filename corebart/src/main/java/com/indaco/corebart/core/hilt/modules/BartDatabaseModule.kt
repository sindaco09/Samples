package com.indaco.corebart.core.hilt.modules

import android.app.Application
import androidx.room.Room
import com.indaco.corebart.core.room.BartAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BartDatabaseModule {
    @Provides
    @Singleton
    fun provideBartDB(application: Application) =
        Room.databaseBuilder(application, BartAppDatabase::class.java, "bartDatabase")
            .fallbackToDestructiveMigration() //clear db on version increase
            .build()

}
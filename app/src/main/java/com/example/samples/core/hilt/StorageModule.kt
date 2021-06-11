package com.example.samples.core.hilt

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.samples.CurrentUser
import com.example.samples.core.room.AppDatabase
import com.example.samples.core.datastore.DataStorePrefs.dataStorePreferences
import com.example.samples.core.datastore.SyncPrefDataStore
import com.example.samples.data.storage.bart.BartDao
import com.example.samples.data.storage.hue.HueDao
import com.example.samples.data.storage.goal.GoalDao
import com.example.samples.data.storage.user.CurrentUserSerializer.userDataStore
import com.example.samples.data.storage.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideDB(application: Application) =
        Room.databaseBuilder(application, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration() //clear db on version increase
            .build()

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

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<CurrentUser> =
        context.userDataStore

    @Provides
    fun provideDataStorePreferences(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStorePreferences

    @Provides
    fun provideSyncPrefsDataStore(dataStore: DataStore<Preferences>) =
        SyncPrefDataStore(dataStore)
}
package com.example.newandroidxcomponentsdemo.hilt

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.newandroidxcomponentsdemo.CurrentUser
import com.example.newandroidxcomponentsdemo.data.storage.AppDatabase
import com.example.newandroidxcomponentsdemo.data.storage.DataStorePrefs.dataStorePreferences
import com.example.newandroidxcomponentsdemo.data.storage.SyncPrefDataStore
import com.example.newandroidxcomponentsdemo.data.user.CurrentUserSerializer.userDataStore
import com.example.newandroidxcomponentsdemo.data.user.UserDao
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
    fun provideDataStore(@ApplicationContext context: Context): DataStore<CurrentUser> =
        context.userDataStore

    @Provides
    fun provideDataStorePreferences(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStorePreferences

    @Provides
    fun provideSyncPrefsDataStore(dataStore: DataStore<Preferences>) =
        SyncPrefDataStore(dataStore)
}
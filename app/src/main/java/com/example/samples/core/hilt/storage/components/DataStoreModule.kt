package com.example.samples.core.hilt.storage.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.samples.CurrentUser
import com.example.samples.core.datastore.DataStorePrefs.dataStorePreferences
import com.example.samples.core.datastore.SyncPrefDataStore
import com.example.samples.data.storage.user.CurrentUserSerializer.userDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
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
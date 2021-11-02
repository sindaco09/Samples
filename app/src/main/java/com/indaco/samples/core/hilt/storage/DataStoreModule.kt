package com.indaco.samples.core.hilt.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.indaco.samples.CurrentUser
import com.indaco.samples.core.datastore.DataStorePrefs.dataStorePreferences
import com.indaco.samples.core.datastore.SyncPrefDataStore
import com.indaco.samples.data.storage.user.CurrentUserSerializer.userDataStore
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
package com.indaco.samples.core.hilt.dependencies

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DatastorePreferencesDependency {

    @ApplicationContext
    fun provideApp(): Context

    fun providePreferencesDataStore(): DataStore<Preferences>
}
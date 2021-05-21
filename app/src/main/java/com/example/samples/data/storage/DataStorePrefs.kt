package com.example.samples.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStorePrefs {

    val Context.dataStorePreferences: DataStore<Preferences> by preferencesDataStore(name = "userPrefs")

    val EMAIL_KEY = stringPreferencesKey("email")

    fun getEmail(context: Context): Flow<String?> =
        context.dataStorePreferences.data.map { it[EMAIL_KEY] }

    suspend fun setEmail(context: Context, email: String) {
        context.dataStorePreferences.edit { it[EMAIL_KEY] = email }
    }
}
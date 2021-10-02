package com.example.core.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.preference.PreferenceDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
 * Preferences Data Store is setup to return the values on synchronous
 */
class SyncPrefDataStore(private val dataStore: DataStore<Preferences>): PreferenceDataStore() {

    private val dispatcher = Dispatchers.Default

    override fun putString(key: String, value: String?) {
        CoroutineScope(dispatcher).launch {
            dataStore.edit {  it[stringPreferencesKey(key)] = value!! }
        }
    }

    override fun getString(key: String, defValue: String?): String? {
        return runBlocking { dataStore.data.map { it[stringPreferencesKey(key)] ?: defValue }.first() }
    }

    override fun putStringSet(key: String, values: MutableSet<String>?) {
        CoroutineScope(dispatcher).launch {
            dataStore.edit { it[stringSetPreferencesKey(key)] = values?: emptySet() }
        }
    }

    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String> {
        return runBlocking {
            dataStore.data
                .map { it[stringSetPreferencesKey(key)] ?: defValues }
                .first()?.toMutableSet() ?: emptySet<String>().toMutableSet()
        }
    }

    override fun putInt(key: String, value: Int) {
        CoroutineScope(dispatcher).launch {
            dataStore.edit {  it[intPreferencesKey(key)] = value }
        }
    }

    override fun getInt(key: String, defValue: Int): Int {
        return runBlocking { dataStore.data.map { it[intPreferencesKey(key)] ?: defValue }.first() }
    }

    override fun putBoolean(key: String, value: Boolean) {
        CoroutineScope(dispatcher).launch {
            dataStore.edit {  it[booleanPreferencesKey(key)] = value }
        }
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return runBlocking { dataStore.data.map { it[booleanPreferencesKey(key)] ?: defValue }.first() }
    }
}
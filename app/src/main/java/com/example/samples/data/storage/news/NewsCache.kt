package com.example.samples.data.storage.news

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.samples.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsCache @Inject constructor(
    @ApplicationContext val app: Context,
    private val dataStore: DataStore<Preferences>) {

    fun getInAppNotificationsEnabledMap(): Flow<HashMap<String, Boolean>> {
        val key = app.resources.getString(R.string.enable_in_app_notifications)
        val stringArray = app.resources.getStringArray(R.array.news_type_in_app_values)

        return dataStore.data.map {
            val set = it[stringSetPreferencesKey(key)] ?: emptySet()
            set.toBooleanHashMap(stringArray)
        }
    }

    fun getPushNotificationsEnabledMap(): Flow<HashMap<String, Boolean>> {
        val key = app.resources.getString(R.string.enable_push_notifications)
        val stringArray = app.resources.getStringArray(R.array.news_type_push_values)

        return dataStore.data.map {
            val set = it[stringSetPreferencesKey(key)] ?: emptySet()
            set.toBooleanHashMap(stringArray)
        }
    }

    companion object {
        fun Set<String>.toBooleanHashMap(stringArray: Array<String>): HashMap<String, Boolean> {
            return HashMap<String, Boolean>(stringArray.size).apply {
                stringArray.forEach {
                    this[it] = contains(it)
                }
            }
        }
    }
}
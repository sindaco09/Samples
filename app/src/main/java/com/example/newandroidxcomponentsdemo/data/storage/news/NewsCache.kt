package com.example.newandroidxcomponentsdemo.data.storage.news

import android.content.Context
import com.example.newandroidxcomponentsdemo.R
import com.example.newandroidxcomponentsdemo.data.storage.SyncPrefDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsCache @Inject constructor(
    @ApplicationContext val app: Context,
    private val dataStore: SyncPrefDataStore) {

    fun getInAppNotificationsEnabledSet(): HashMap<String, Boolean> {
        val setKey = app.resources.getString(R.string.enable_in_app_notifications)
        val stringSet = dataStore.getStringSet(setKey, null)
        val stringArray = app.resources.getStringArray(R.array.news_type_in_app_values)

        return HashMap<String, Boolean>(stringArray.size).apply {
            stringArray.forEach {
                this[it] = stringSet.contains(it)
            }
        }
    }
}
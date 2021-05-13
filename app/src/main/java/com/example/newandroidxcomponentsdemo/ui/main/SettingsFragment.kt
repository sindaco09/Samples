package com.example.newandroidxcomponentsdemo.ui.main

import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.preference.PreferenceFragmentCompat
import com.example.newandroidxcomponentsdemo.R
import com.example.newandroidxcomponentsdemo.data.storage.SyncPrefDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = SyncPrefDataStore(dataStore)
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
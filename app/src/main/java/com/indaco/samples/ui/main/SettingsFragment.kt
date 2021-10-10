package com.indaco.samples.ui.main

import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.preference.PreferenceFragmentCompat
import com.indaco.samples.R
import com.indaco.samples.core.datastore.SyncPrefDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
 * Goal is to test Preferences Fragment used to automatically get/save SharedPreferences
 * using the DataStore Preferences file vs the usual SharedPreferences location
 */
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = SyncPrefDataStore(dataStore)
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
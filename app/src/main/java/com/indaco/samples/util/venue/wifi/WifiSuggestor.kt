package com.indaco.samples.util.venue.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi

class WifiSuggestor(val context: Context) {

    companion object {
        private const val TAG = "Wifi"
        private const val SSID = "Hodor"
        private const val PASSWORD = "St3fwiFi"
    }

    interface SuggestionMadeListener {
        fun onSuggestionMade(success: Boolean)
    }

    fun suggestWifi(listener: SuggestionMadeListener) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> suggestWifiQ(listener)
        }
    }

    @MainThread
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun suggestWifiQ(listener: SuggestionMadeListener) {
        val suggestion = buildSuggestion(SSID, PASSWORD)

        val suggestionsList = listOf(suggestion)

        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val status = wifiManager.addNetworkSuggestions(suggestionsList)

        when (status) {
            WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS -> {
                Log.d(TAG, "wifi suggestion success!")

            }
            else -> {
                Log.e(TAG, "error with suggestions: $status")
            }
        }

        // Optional (Wait for post connection broadcast to one of your suggestions)
        val intentFilter = IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!intent.action.equals(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                    listener.onSuggestionMade(false)
                    return
                }
                listener.onSuggestionMade(true)
                // do post connect processing here

                unregisterReceiver()
            }
        }
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(null)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun buildSuggestion(ssid: String, password: String) =
        WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password)
            .setIsAppInteractionRequired(true)
            .build()
}
package com.indaco.samples.util.venue.wifi

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.indaco.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_COMPLETE_FAIL
import com.indaco.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_COMPLETE_SUCCESS
import com.indaco.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_PROGRESS_FINISH
import com.indaco.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_PROGRESS_START
import com.indaco.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_WIFI_MANUAL_ENABLE_REQUIRED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class WifiConnectorQ (val context: Context, val scope: CoroutineScope, override val listener: WifiStateChangeListener): IWifiConnector {

    companion object {
        private const val TAG = "WifiConnector"
        private const val SSID = "Hodor"
        private const val PASSWORD = "St3fwiFi"
    }

    private val main = Dispatchers.Main

    private val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var connectivityManager: ConnectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var receivers: MutableList<BroadcastReceiver>

    private val askedToEnableWiFI = AtomicBoolean(false)
    private var attemptedConnection = false
    private var startedProgress = false

    private var wifiActiveListener: BroadcastReceiver? = null

    override fun connect() {
        attemptedConnection = true
        receivers = emptyList<BroadcastReceiver>().toMutableList()

        if (wifiDisabled()) {
            promptToEnableWifi()
        } else {
            request()
        }
    }

    private fun wifiDisabled() = !wifiManager.isWifiEnabled

    private fun promptToEnableWifi() {
        wifiActiveListener = wifiActiveBroadcastReceiver()

        Log.d(TAG, "connect: Registering callbacks for wifi activation")

        registerReceiver(wifiActiveListener!!, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))

        askedToEnableWiFI.set(true)
        requestWifiEnabledManually()
    }

    private fun requestWifiEnabledManually() {
        onStateChange(STATE_WIFI_MANUAL_ENABLE_REQUIRED)
    }

    private fun wifiActiveBroadcastReceiver(): BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            Log.d(TAG, "connect: onReceive: $state")
            if (state == WifiManager.WIFI_STATE_ENABLED) {
                Log.d(TAG, "connect: Wifi hardware has been activated: ${askedToEnableWiFI.get()}")

                if (askedToEnableWiFI.get())
                    onStateChange(STATE_PROGRESS_START)
                unregisterReceiver(wifiActiveListener!!)
                wifiActiveListener = null

                // Continue with requesting the Wifi
                request()
            } else if (state == WifiManager.WIFI_STATE_DISABLING) {
                Log.e(TAG, "connect: Failed to activate Wifi hardware")
            }
        }
    }

    private fun registerReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter) {
        context.registerReceiver(receiver, intentFilter)
        receivers.add(receiver)
    }

    private fun unregisterReceiver(receiver: BroadcastReceiver) {
        context.unregisterReceiver(receiver)
        receivers.remove(receiver)
    }

    override fun unregisterReceivers() {
        if (attemptedConnection) receivers.forEach { unregisterReceiver(it) }
    }

    override fun removeWifiRegisterReceiver() {
        wifiActiveListener?.let { unregisterReceiver(it) }
    }

    override fun addWifiRegisterReceiver() {
        wifiActiveListener?.let { registerReceiver(it, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)) }
    }

    override fun hasWifiConfiguration(): Boolean = false

    // NETWORK ROUTING
    ////////////////////////////////////////////

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    @SuppressLint("NewApi")
    private fun request() {
        if (!startedProgress){
            startedProgress = true
            onStateChange(STATE_PROGRESS_START)
        }

        val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
            .setSsid(SSID)
            .setWpa2Passphrase(PASSWORD)
            .build()

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
            .setNetworkSpecifier(wifiNetworkSpecifier)
            .build()

        val handler = Handler(Looper.getMainLooper())

        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object: ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d(TAG, "onAvailable: "+network.networkHandle)
            }

            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                super.onBlockedStatusChanged(network, blocked)
                Log.d(TAG, "onBlockedStatusChanged: ${network.networkHandle} blocked: $blocked")
                if(!blocked) {
                    connectivityManager.bindProcessToNetwork(network)
                    Log.d(TAG,"onBlockedStatusChanged: connected!")
                    scope.launch(main) {
                        onStateChange(STATE_PROGRESS_FINISH)
                        delay(250)
                        onStateChange(STATE_COMPLETE_SUCCESS)
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d(TAG, "onLost: "+network.networkHandle)
                unregisterNetworkCallback()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d(TAG, "onUnavailable!")
                onStateChange(STATE_COMPLETE_FAIL)
                unregisterNetworkCallback()
            }
        }

        Log.d(TAG, "Requesting network from connectivityManager.requestNetwork")
        connectivityManager.requestNetwork(networkRequest, networkCallback!!, handler, 60000)
    }

    private fun unregisterNetworkCallback() {
        if (networkCallback != null) {
            try {
                Log.d(TAG, "Unregistering network callback")
                connectivityManager.unregisterNetworkCallback(networkCallback!!)
            } finally {
                networkCallback = null
            }
        }
    }

    private fun onStateChange(state: Int) = scope.launch(main) { listener.onStateChange(state) }

}
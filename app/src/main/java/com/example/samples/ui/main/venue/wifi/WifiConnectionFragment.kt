package com.example.samples.ui.main.venue.wifi

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.samples.R
import com.example.samples.databinding.FragmentConnectWifiBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.util.common.PermissionsUtil
import com.example.samples.util.venue.wifi.WifiConnectorQ
import com.example.samples.util.venue.wifi.WifiStateChangeListener
import com.example.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_COMPLETE_FAIL
import com.example.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_COMPLETE_SUCCESS
import com.example.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_PROGRESS_FINISH
import com.example.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_PROGRESS_START
import com.example.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_WIFI_MANUAL_ENABLE_REQUIRED
import com.example.samples.util.venue.wifi.WifiStateChangeListener.Companion.STATE_WIFI_UNAVAILABLE
import com.example.samples.util.venue.wifi.WifiSuggestor

class WifiConnectionFragment: DataBindingFragment<FragmentConnectWifiBinding>(R.layout.fragment_connect_wifi), WifiStateChangeListener {

    companion object {
        private const val TAG = "WifiFragment"
    }
    private lateinit var permissionsUtil: PermissionsUtil
    private lateinit var wifiSuggestor: WifiSuggestor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        wifiSuggestor = WifiSuggestor(requireContext())

        permissionsUtil = PermissionsUtil(this, PermissionsUtil.Type.LOCATION) { granted ->
            if (granted) {
                connectToWifi()
            } else {
                permissionsUtil.shouldOpenSettingsDialog { intent ->
                    intent?.let { i -> startActivity(i) }
                }
            }
        }

        binding.wifiBtn.setOnClickListener {

        }

        binding.wifiSuggestionsBtn.setOnClickListener {
            checkPermissions()
        }
    }

    private fun connectToWifi() {
        wifiSuggestor.suggestWifi(object : WifiSuggestor.SuggestionMadeListener {
            override fun onSuggestionMade(success: Boolean) {
                if (success) {
                    requestUserJoinNetwork()
                }
            }
        })
    }

    private fun requestUserJoinNetwork() {
        Log.d("TAG","requestUserJoinNetwork: ${context != null}")
        context?.let { c ->
            val wifiConnector = WifiConnectorQ(c, viewLifecycleOwner.lifecycleScope, this)
            wifiConnector.connect()
        }
    }

    private fun checkPermissions() {
        permissionsUtil.allPermissionsGranted { connectToWifi() }
    }

    override fun onStateChange(state: Int) {
        Log.d(TAG,"onStateChange: $state")
        when (state) {
            STATE_PROGRESS_START -> {

            }
            STATE_WIFI_MANUAL_ENABLE_REQUIRED -> {
                Log.d(TAG, "WifiRequired, enable it through settings")
            }
            STATE_PROGRESS_FINISH -> finishProgress()

            STATE_COMPLETE_SUCCESS -> stateComplete(true)

            STATE_COMPLETE_FAIL -> stateComplete(false)

            STATE_WIFI_UNAVAILABLE -> wifiIsUnavailable()
        }
    }

    private fun finishProgress() {
        Log.d(TAG, "finished!")
    }

    private fun stateComplete(success: Boolean) {
        Log.d(TAG,"+++++++++> stateComplete: $success")
        if (success) {
            Log.d(TAG,"+++++++++> stateComplete: success: ")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val data = Intent()
                val cm = requireContext().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val network = cm.boundNetworkForProcess
//                if (network != null)
//                    data.putExtra(Const.NETWORK, network)
//                setResult(RESULT_OK, data)
            } else {
//                setResult(RESULT_OK)
            }


        } else {
//            wifiConnector.unregisterReceivers()
        }
    }

    private fun wifiIsUnavailable() {
        TODO("Not yet implemented")
    }

}
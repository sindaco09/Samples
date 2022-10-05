package com.indaco.samples.util.venue.wifi

interface WifiStateChangeListener {
    fun onStateChange(state: Int)
    companion object {
        const val STATE_INIT_NO_SSID = 1
        const val STATE_INIT_HAS_SSID = 2
        const val STATE_WIFI_DISABLED = 3
        const val STATE_WIFI_MANUAL_ENABLE_REQUIRED = 4
        const val STATE_WIFI_ENABLED = 5
        const val STATE_PROGRESS_START = 6
        const val STATE_PROGRESS_FINISH = 7
        const val STATE_COMPLETE_SUCCESS = 8
        const val STATE_COMPLETE_FAIL = 9
        const val STATE_WIFI_UNAVAILABLE = 10
    }
}
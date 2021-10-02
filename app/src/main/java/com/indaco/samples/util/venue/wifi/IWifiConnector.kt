package com.indaco.samples.util.venue.wifi

interface IWifiConnector {
    val listener: WifiStateChangeListener
    fun connect()
    fun unregisterReceivers()
    fun removeWifiRegisterReceiver()
    fun addWifiRegisterReceiver()
    fun hasWifiConfiguration(): Boolean
}

package com.example.core.data.network.result.bart

import com.example.core.data.models.bart.BartStation


class GetStationsResult(val root: Root) {
    inner class Root(val stations: Station)
    inner class Station(val station: List<BartStation>)

    fun getStations(): List<BartStation> = root.stations.station
}
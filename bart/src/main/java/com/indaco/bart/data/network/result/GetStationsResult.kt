package com.indaco.bart.data.network.result

import com.indaco.bart.data.models.BartStation

class GetStationsResult(val root: Root) {
    inner class Root(val stations: Station)
    inner class Station(val station: List<BartStation>)

    fun getStations(): List<BartStation> = root.stations.station
}
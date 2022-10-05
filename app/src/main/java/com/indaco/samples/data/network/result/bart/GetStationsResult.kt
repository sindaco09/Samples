package com.indaco.samples.data.network.result.bart

import com.indaco.samples.data.models.bart.BartStation


class GetStationsResult(val root: Root) {
    inner class Root(val stations: Station)
    inner class Station(val station: List<BartStation>)

    fun getStations(): List<BartStation> = root.stations.station
}
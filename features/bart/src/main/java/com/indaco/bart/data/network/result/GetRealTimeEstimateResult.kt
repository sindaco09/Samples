package com.indaco.bart.data.network.result

import com.indaco.bart.data.models.BartStation

class GetRealTimeEstimateResult(val root: Root) {
    class Root(
            val time: String,
            val station: List<Station>)
    class Station(val etd: List<ETD>?)
    class ETD(
            val destination: String,
            val abbreviation: String,
            val estimate: List<Estimate>) {
        var direction: BartStation.Direction? = null
            get() {
                if (field == null)
                    field = BartStation.Direction.getDirectionByString(estimate.first().direction)
                return field
            }
    }
    class Estimate(
            val minutes: String,
            val platform: Int,
            val color: String, // Use to identify train
            val direction: String,
            val delay: Int) {  // Delay is in seconds off of original scheduled time
        var lastUpdated: String? = null
    }

    fun getETDs() = root.station.first().etd
}
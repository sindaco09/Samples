package com.indaco.bart.data.models

import com.indaco.samples.data.models.bart.BartTripDbo
import com.indaco.bart.data.network.result.GetTripResult

data class BartTrip (
    override val primaryAbbr: String,
    override val secondaryAbbr: String
): BartTripDbo(makeKey(primaryAbbr, secondaryAbbr), primaryAbbr, secondaryAbbr) {

    constructor(station1: BartStation, station2: BartStation): this (station1.abbr, station2.abbr)
    constructor(dbo: BartTripDbo): this (dbo.primaryAbbr, dbo.secondaryAbbr)

    var trips: List<GetTripResult.Trip>? = null
    var flippedTrips: List<GetTripResult.Trip>? = null
    var primaryStation: BartStation? = null
    var secondaryStation: BartStation? = null

    companion object {
        fun makeKey(primaryStation: BartStation, secondaryStation: BartStation) =
                primaryStation.abbr + secondaryStation.abbr

        fun makeKey(primaryAbbr: String, secondaryAbbr: String) =
            primaryAbbr + secondaryAbbr
    }

    fun toBartTripDbo(): BartTripDbo = BartTripDbo(makeKey(primaryAbbr, secondaryAbbr), primaryAbbr, secondaryAbbr)
}
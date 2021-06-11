package com.example.samples.data.models.bart

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.samples.data.network.result.bart.GetTripResult

@Entity(tableName = "bart_commute")
data class BartTrip (
        @PrimaryKey val key: String,
        val primaryAbbr: String,
        val secondaryAbbr: String
): BartObject() {
    constructor(station1: BartStation, station2: BartStation): this (
            makeKey(station1, station2),
            station1.abbr,
            station2.abbr
    )

    @Ignore var trips: List<GetTripResult.Trip>? = null
    @Ignore var flippedTrips: List<GetTripResult.Trip>? = null
    @Ignore var primaryStation: BartStation? = null
    @Ignore var secondaryStation: BartStation? = null


    companion object {
        fun makeKey(primaryStation: BartStation, secondaryStation: BartStation) =
                primaryStation.abbr + secondaryStation.abbr
    }
}
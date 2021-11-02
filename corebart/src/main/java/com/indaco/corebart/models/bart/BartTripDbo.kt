package com.indaco.corebart.models.bart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bart_commute")
open class BartTripDbo (
    @PrimaryKey open val key: String,
    open val primaryAbbr: String,
    open val secondaryAbbr: String
): BartObject() {
    constructor(station1: BartStationDbo, station2: BartStationDbo): this (
            makeKey(station1, station2),
            station1.abbr,
            station2.abbr
    )

    companion object {
        fun makeKey(primaryStation: BartStationDbo, secondaryStation: BartStationDbo) =
                primaryStation.abbr + secondaryStation.abbr
    }
}
package com.indaco.bart.data.network.result

import com.google.gson.annotations.SerializedName

class GetTripResult(val root: Root) {
    class Root(val schedule: Schedule)
    class Schedule(val request: Request)
    class Request(val trip: List<Trip>?)
    class Trip(
            @SerializedName("@origTimeMin") val origTimeMin: String,
            @SerializedName("@destTimeMin") val destTimeMin: String,
            @SerializedName("@tripTime") val tripTime: Int,
            @SerializedName("@clipper") val fare: Float
    )

    fun getTrips() = root.schedule.request.trip
}
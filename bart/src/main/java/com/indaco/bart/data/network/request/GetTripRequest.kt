package com.indaco.bart.data.network.request

class GetTripRequest(
        var isDeparting: Boolean = false,
        var time: String = "now",
        var orig: String,
        var dest: String
)
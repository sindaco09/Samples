package com.example.core.data.network.request.bart

class GetTripRequest(
        var isDeparting: Boolean = false,
        var time: String = "now",
        var orig: String,
        var dest: String
)
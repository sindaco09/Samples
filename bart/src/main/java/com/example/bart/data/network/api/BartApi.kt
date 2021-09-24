package com.example.bart.data.network.api

import com.example.bart.data.network.request.GetRealTimeEstimateRequest
import com.example.bart.data.network.request.GetTripRequest
import com.example.bart.data.network.result.GetRealTimeEstimateResult
import com.example.bart.data.network.result.GetStationsResult
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BartApi @Inject constructor(
    private val bartService: BartService
) {
    fun getStations(): Response<GetStationsResult> = bartService.getStations().execute()

    fun getStationSchedule(stationId: String) =
            bartService.getStationSchedule(stationId = stationId).execute()

    fun getPlanTrip(requestForm: GetTripRequest) =
            bartService.getPlanTrip(requestForm.orig, requestForm.dest, requestForm.time).execute()

    fun getRealTimeEstimate(orig: String, dir: String? = null, plat: String? = null): Response<GetRealTimeEstimateResult> =
            bartService.getRealTimeEstimate(plat, orig, dir).execute()

    fun getRealTimeEstimate(request: GetRealTimeEstimateRequest): Response<GetRealTimeEstimateResult> =
        bartService.getRealTimeEstimate(request.plat, request.orig, request.dir).execute()
}
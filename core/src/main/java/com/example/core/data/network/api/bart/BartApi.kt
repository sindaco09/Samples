package com.example.core.data.network.api.bart

import com.example.core.data.network.request.bart.GetRealTimeEstimateRequest
import com.example.core.data.network.request.bart.GetTripRequest
import com.example.core.data.network.result.bart.GetRealTimeEstimateResult
import com.example.core.data.network.result.bart.GetStationScheduleResult
import com.example.core.data.network.result.bart.GetStationsResult
import com.example.core.data.network.result.bart.GetTripResult
import com.example.samples.data.network.api.bart.BartService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BartApi @Inject constructor(
    private val bartService: BartService
) {
    fun getStations(): Response<GetStationsResult> = bartService.getStations().execute()

    fun getStationSchedule(stationId: String): Response<GetStationScheduleResult> =
            bartService.getStationSchedule(stationId = stationId).execute()

    fun getPlanTrip(requestForm: GetTripRequest): Response<GetTripResult> =
            bartService.getPlanTrip(requestForm.orig, requestForm.dest, requestForm.time).execute()

    fun getRealTimeEstimate(orig: String, dir: String? = null, plat: String? = null): Response<GetRealTimeEstimateResult> =
            bartService.getRealTimeEstimate(plat, orig, dir).execute()

    fun getRealTimeEstimate(request: GetRealTimeEstimateRequest): Response<GetRealTimeEstimateResult> =
        bartService.getRealTimeEstimate(request.plat, request.orig, request.dir).execute()
}
package com.indaco.samples.data.network.api.bart

import com.indaco.samples.data.network.result.bart.GetRealTimeEstimateResult
import com.indaco.samples.data.network.request.bart.GetTripRequest
import com.indaco.samples.data.network.request.bart.GetRealTimeEstimateRequest
import com.indaco.samples.data.network.result.bart.GetStationsResult
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
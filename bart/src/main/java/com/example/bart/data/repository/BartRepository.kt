package com.example.bart.data.repository

import com.example.bart.data.storage.BartCache
import com.example.bart.ui.bart.ETD
import com.example.core.data.models.bart.BartTrip
import com.example.core.data.network.api.bart.BartApi
import com.example.core.data.network.request.bart.GetTripRequest
import com.example.core.data.network.result.bart.GetRealTimeEstimateResult
import com.example.core.data.models.bart.BartStation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BartRepository @Inject constructor(
    private val bartApi: BartApi,
    private val bartCache: BartCache
) {

    fun getStoredBartStations(): Flow<Response<List<BartStation>?>> {
        val response = bartCache.getBartStationsFlow()
            .map {
                Response.success(it)
            }

        return response
    }

    var continueRealTimeEstimates = false

    fun getRealTimeEstimateFlow(orig: String, dir: String? = null, plat: String? = null): Flow<Response<List<GetRealTimeEstimateResult.ETD>?>> {
        continueRealTimeEstimates = true
        return flow<Response<List<ETD>?>> {
            while(continueRealTimeEstimates) {
                val response = bartApi.getRealTimeEstimate(orig, dir, plat)
                if (response.isSuccessful) {
                    val list = response.body()?.getETDs()
                    emit(Response.success(list))
                } else {
                    emit(Response.error(-1, response.errorBody()!!))
                }
                delay(15_000)
            }
        }
    }


    suspend fun getBartStations(): Flow<Response<List<BartStation>?>> {
        val storedStations = bartCache.getBartStations()
        return if (!storedStations.isNullOrEmpty())
            flowOf(Response.success(storedStations))
        else {
            flowOf(bartApi.getStations()).map { response ->
                if (response.isSuccessful) {
                    val stations = response.body()?.getStations()
                    stations?.let { bartCache.updateStations(stations) }
                    Response.success(stations)
                } else
                    Response.error(response.errorBody()!!, response.raw())
            }
        }
    }

    fun planTrip(requestForm: GetTripRequest) = bartApi.getPlanTrip(requestForm)

    fun getBartStationSchedule(stationId: String) = bartApi.getStationSchedule(stationId)

    fun getRealTimeEstimate(orig: String, dir: String? = null, plat: String? = null) =
            bartApi.getRealTimeEstimate(orig, dir, plat)

    fun addFavoriteBartStation(station: BartStation) = bartCache.addFavoriteBartStation(station)

    fun deleteFavoriteBartStation(station: BartStation) = bartCache.deleteFavoriteBartStation(station)

    fun getFavoriteBartStationsFlow(): Flow<List<BartStation>?> = bartCache.getFavoriteBartStationsFlow()

    fun getBartStationFlow(stationId: String) = bartCache.getBartStationFlow(stationId)

    fun addFavoriteTrip(primaryStation: BartStation, secondaryStation: BartStation) =
            bartCache.addFavoriteBartTrip(BartTrip(primaryStation, secondaryStation))

    fun getFavoriteBartTrips(): Flow<List<BartTrip>?> = bartCache.getFavoriteBartTrips()

    fun removeFavoriteBartTrip(trip: BartTrip) =
            bartCache.removeFavoriteBartCommute(trip)

    fun getBartTrip(origStationId: String, destStationId: String) = bartCache.getBartTrip(origStationId, destStationId)

}
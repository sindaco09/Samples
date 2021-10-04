package com.indaco.bart.data.storage

import com.indaco.samples.data.models.bart.BartStation
import com.indaco.samples.data.models.bart.BartTrip
import com.indaco.samples.data.storage.bart.BartDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BartCache @Inject constructor(private val bartDao: BartDao) {

    suspend fun getBartStations() = bartDao.getBartStations()

    fun getBartStationsFlow() = bartDao.getBartStationsFlowTwo()

    fun addFavoriteBartStation(station: BartStation) = bartDao.updateBartStation(station)

    fun deleteFavoriteBartStation(station: BartStation) = bartDao.removeBartStation(station)

    fun getFavoriteBartStationsFlow() = bartDao.getFavoriteBartStationsFlow()

    fun updateStations(stations: List<BartStation>) = bartDao.updateBartStations(stations)

    fun getBartStationFlow(stationId: String): Flow<BartStation> = bartDao.getBartStationFlow(stationId)

    fun addFavoriteBartTrip(trip: BartTrip) = bartDao.updateBartCommute(trip)

    fun getFavoriteBartTrips() = bartDao.getFavoriteBartCommuteFlow().map { bartTrips ->
        bartTrips?.forEach { bartTrip ->
            bartTrip.primaryStation = bartDao.getBartStation(bartTrip.primaryAbbr)
            bartTrip.secondaryStation = bartDao.getBartStation(bartTrip.secondaryAbbr)
        }
        return@map bartTrips
    }

    fun removeFavoriteBartCommute(trip: BartTrip) = bartDao.removeBartCommute(trip)

    fun getBartTrip(origStationId: String, destStationId: String): Flow<BartTrip> =
            bartDao.getBartTripFlow(origStationId, destStationId).map {
                it.apply {
                    primaryStation = bartDao.getBartStation(it.primaryAbbr)
                    secondaryStation = bartDao.getBartStation(it.secondaryAbbr)
                }
            }
}
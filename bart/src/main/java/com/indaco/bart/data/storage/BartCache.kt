package com.indaco.bart.data.storage

import com.indaco.bart.data.models.BartStation
import com.indaco.bart.data.models.BartTrip
import com.indaco.samples.data.models.bart.BartStationDbo
import com.indaco.samples.data.models.bart.BartTripDbo
import com.indaco.samples.data.storage.bart.BartDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BartCache @Inject constructor(private val bartDao: BartDao) {

    suspend fun getBartStations(): List<BartStation>? =
        bartDao.getBartStations()?.mapTo(ArrayList()){ BartStation(it) }

    fun getBartStationsFlow(): Flow<List<BartStation>?> =
        bartDao.getBartStationsFlowTwo()
            .map {  dbo -> dbo?.mapTo(ArrayList()) { BartStation(it) } }

    fun addFavoriteBartStation(station: BartStation) = bartDao.updateBartStation(station.toBartStationDbo())

    fun deleteFavoriteBartStation(station: BartStation) = bartDao.removeBartStation(station.toBartStationDbo())

    fun getFavoriteBartStationsFlow(): Flow<List<BartStation>?> =
        bartDao.getFavoriteBartStationsFlow().map { dbo: List<BartStationDbo>? ->
            dbo?.mapTo(ArrayList()) { BartStation(it) }
        }

    fun updateStations(stations: List<BartStation>) =
        bartDao.updateBartStations(stations.mapTo(ArrayList()) { it.toBartStationDbo() })

    fun getBartStationFlow(stationId: String): Flow<BartStation> =
        bartDao.getBartStationFlow(stationId).map { BartStation(it) }

    fun addFavoriteBartTrip(trip: BartTrip) = bartDao.updateBartCommute(trip.toBartTripDbo())

    fun getFavoriteBartTrips(): Flow<List<BartTrip>?> =
        bartDao.getFavoriteBartCommuteFlow()
            .map { bartTripsDbo: List<BartTripDbo>? ->
                bartTripsDbo?.mapTo(ArrayList()) {
                    BartTrip(it).apply {
                        primaryStation = BartStation(bartDao.getBartStation(primaryAbbr))
                        secondaryStation = BartStation(bartDao.getBartStation(secondaryAbbr))
                    }
                }
            }

    fun removeFavoriteBartCommute(trip: BartTrip) = bartDao.removeBartCommute(trip.toBartTripDbo())

    fun getBartTrip(origStationId: String, destStationId: String): Flow<BartTrip> =
            bartDao.getBartTripFlow(origStationId, destStationId)
                .map {
                    BartTrip(it).apply {
                        primaryStation = BartStation(bartDao.getBartStation(it.primaryAbbr))
                        secondaryStation = BartStation(bartDao.getBartStation(it.secondaryAbbr))
                    }
                }
}
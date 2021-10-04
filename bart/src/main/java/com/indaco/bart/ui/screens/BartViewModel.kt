package com.indaco.bart.ui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indaco.bart.data.network.request.GetTripRequest
import com.indaco.bart.data.repository.BartRepository
import com.indaco.samples.core.hilt.IODispatcher
import com.indaco.samples.data.models.bart.BartObject
import com.indaco.samples.data.models.bart.BartStation
import com.indaco.samples.data.models.bart.BartTrip
import com.indaco.samples.data.network.result.bart.GetRealTimeEstimateResult
import com.indaco.samples.data.network.result.bart.GetStationScheduleResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

typealias Schedule = GetStationScheduleResult.Item
typealias ETD = GetRealTimeEstimateResult.ETD

class BartViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val repo: BartRepository
): ViewModel() {

    val allStations = MutableLiveData<List<BartStation>>()
    val favoriteStationsLiveData = MutableLiveData<List<BartStation>>(emptyList())
    val favoriteTripsLiveData = MutableLiveData<List<BartTrip>?>(emptyList())
    val errorLiveData = MutableLiveData<String>()
    var bartItem = MutableLiveData<BartObject>()

    fun updateBartItem(_bartItem: BartObject) = bartItem.postValue(_bartItem)

    fun getStoredBartStations() {
        viewModelScope.launch(dispatcher) {
            repo.getStoredBartStations().collect {
                if (it.isSuccessful)
                    allStations.postValue(it.body())
                else
                    errorLiveData.postValue(it.errorBody().toString())
            }
        }
    }

    fun getStations() =
            viewModelScope.launch(dispatcher) {
                repo.getBartStations().collect {
                    if (it.isSuccessful)
                        allStations.postValue(it.body())
                    else
                        errorLiveData.postValue(it.errorBody().toString())
                }
            }

    fun getFavoriteStations() =
            viewModelScope.launch(dispatcher) {
                repo.getFavoriteBartStationsFlow().collect { favoriteStations ->
                    if (!favoriteStations.isNullOrEmpty()) {
                        favoriteStations.forEach { station ->
                            station.schedules = getStationSchedule(station.abbr)
                            station.etds = getStationRealTimeEstimates(station.abbr)
                        }
                    }
                    favoriteStationsLiveData.postValue(favoriteStations)
                }
            }

    private fun getStationSchedule(stationId: String): List<Schedule>? {
        val now = ZonedDateTime.now()
        val twoHoursFromNow = ZonedDateTime.now().plusHours(2)
        val response = repo.getBartStationSchedule(stationId)
        return when {
            response.isSuccessful -> response.body()?.getSchedules()
                    ?.filter {
                        it.zonedDateTime!!.isAfter(now) && it.zonedDateTime!!.isBefore(twoHoursFromNow)
                    }
            else -> null
        }
    }

    private fun getStationRealTimeEstimates(stationId: String): List<ETD>? {
        val response = repo.getRealTimeEstimate(stationId)
        return when {
            response.isSuccessful -> response.body()?.getETDs()
            else -> null
        }
    }

    fun saveFavoriteStation(station: BartStation) =
            viewModelScope.launch(dispatcher) { repo.addFavoriteBartStation(station) }

    fun removeFavoriteStation(station: BartStation) =
            viewModelScope.launch(dispatcher) { repo.deleteFavoriteBartStation(station) }

    fun saveFavoriteTrip(primaryStation: BartStation, secondaryStation: BartStation) =
            viewModelScope.launch(dispatcher) { repo.addFavoriteTrip(primaryStation, secondaryStation) }

    fun removeFavoriteTrip(trip: BartTrip) =
            viewModelScope.launch(dispatcher) { repo.removeFavoriteBartTrip(trip) }

    fun getFavoriteTrips() =
            viewModelScope.launch(dispatcher) {
                repo.getFavoriteBartTrips().collect { commutes ->
                    if (!commutes.isNullOrEmpty()) {
                        commutes.forEach { commute ->
                            // request commute data
                            commute.trips = repo.planTrip(
                                GetTripRequest(
                                    orig = commute.primaryAbbr,
                                    dest = commute.secondaryAbbr)
                            )
                                    .body()?.getTrips()

                            commute.flippedTrips = repo.planTrip(
                                GetTripRequest(
                                    orig = commute.secondaryAbbr,
                                    dest = commute.primaryAbbr)
                            )
                                    .body()?.getTrips()
                        }
                    }
                    favoriteTripsLiveData.postValue(commutes)
                }
            }

    fun removeFavoriteBartObject(bartItem: BartObject) {
        when(bartItem) {
            is BartStation -> removeFavoriteStation(bartItem)
            is BartTrip -> removeFavoriteTrip(bartItem)
        }
    }
}
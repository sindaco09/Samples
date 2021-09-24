package com.example.bart.ui.bart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bart.data.repository.BartRepository
import com.example.core.data.models.bart.BartObject
import com.example.core.data.models.bart.BartTrip
import com.example.core.data.network.request.bart.GetTripRequest
import com.example.core.data.network.result.bart.GetRealTimeEstimateResult
import com.example.core.data.network.result.bart.GetStationScheduleResult
import com.example.samples.data.models.bart.BartStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

typealias Schedule = GetStationScheduleResult.Item
typealias ETD = GetRealTimeEstimateResult.ETD

@HiltViewModel
class BartViewModel @Inject constructor(
    private val repo: BartRepository
): ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    val allStations = MutableLiveData<List<BartStation>>()
    val favoriteStationsLiveData = MutableLiveData<List<BartStation>>(emptyList())
    val favoriteTripsLiveData = MutableLiveData<List<BartTrip>?>()
    val errorLiveData = MutableLiveData<String>()
    var bartItem = MutableLiveData<BartObject>()

    fun updateBartItem(_bartItem: BartObject) = bartItem.postValue(_bartItem)

    fun getStoredBartStations() {
        viewModelScope.launch(ioDispatcher) {
            repo.getStoredBartStations().collect {
                if (it.isSuccessful)
                    allStations.postValue(it.body())
                else
                    errorLiveData.postValue(it.errorBody().toString())
            }
        }
    }

    fun getStations() =
            viewModelScope.launch(ioDispatcher) {
                repo.getBartStations().collect {
                    if (it.isSuccessful)
                        allStations.postValue(it.body())
                    else
                        errorLiveData.postValue(it.errorBody().toString())
                }
            }

    fun getFavoriteStations() =
            viewModelScope.launch(ioDispatcher) {
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
            viewModelScope.launch(ioDispatcher) { repo.addFavoriteBartStation(station) }

    fun removeFavoriteStation(station: BartStation) =
            viewModelScope.launch(ioDispatcher) { repo.deleteFavoriteBartStation(station) }

    fun saveFavoriteTrip(primaryStation: BartStation, secondaryStation: BartStation) =
            viewModelScope.launch(ioDispatcher) { repo.addFavoriteTrip(primaryStation, secondaryStation) }

    fun removeFavoriteTrip(trip: BartTrip) =
            viewModelScope.launch(ioDispatcher) { repo.removeFavoriteBartTrip(trip) }

    fun getFavoriteTrips() =
            viewModelScope.launch(ioDispatcher) {
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
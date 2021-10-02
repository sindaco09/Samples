package com.example.bart.ui.bart.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.data.models.bart.BartStation
import com.example.samples.data.models.bart.BartTrip
import com.example.samples.data.models.bart.BartType
import com.example.samples.data.network.request.bart.GetTripRequest
import com.example.samples.data.network.result.bart.GetRealTimeEstimateResult
import com.example.samples.data.repository.BartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

typealias Estimate = GetRealTimeEstimateResult.Estimate

@HiltViewModel
class BartDetailsViewModel @Inject constructor(
        private val repo: BartRepository
): ViewModel() {

    companion object {
        private const val S = "s"
        private const val N = "n"
    }

    private val ioDispatcher = Dispatchers.IO
    private val scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    var keepGettingLiveReports = AtomicBoolean(false)
    private var liveReportDirectionIsSouth = AtomicBoolean(true)

    var realTimeEtaLiveData = MutableLiveData<Estimate>()
    var bartStationLiveData = MutableLiveData<BartStation>()
    var bartTripLiveData = MutableLiveData<BartTrip>()
    var networkErrorLiveData = MutableLiveData<String>()

    private lateinit var bartType: BartType
    private lateinit var origStationId: String
    private var destStationId: String? = null

    fun init(args: BartMapsFragmentArgs) {
        origStationId = args.stationId
        destStationId = args.destStationId
        bartType = args.bartType
    }

    fun fetchData() {
        when(bartType) {
            BartType.STATION -> fetchStationData()
            BartType.TRIP -> fetchTripData()
        }
    }

    private fun fetchStationData() {
        Log.d("TAG","fetchStationData")
        scope.launch(ioDispatcher) {
            repo.getBartStationFlow(origStationId).collect {
                val scheduleResponse = repo.getBartStationSchedule(origStationId)
                if (scheduleResponse.isSuccessful) {
                    bartStationLiveData.postValue(
                            it.apply { schedules = scheduleResponse.body()?.getSchedules()?.filter { schedule ->
                                schedule.zonedDateTime!!.isAfter(ZonedDateTime.now())
                            }
                        }
                    )
                } else {
                    val error = "${scheduleResponse.code()}: ${scheduleResponse.message()}"
                    networkErrorLiveData.postValue(error)
                }
            }
        }

        scope.launch { startLiveReports() }
    }

    fun setLiveReportDirection(direction: BartStation.Direction) {
        liveReportDirectionIsSouth.set(direction == BartStation.Direction.SOUTH)
    }

    private fun startLiveReports() {
        keepGettingLiveReports.set(true)
        liveReportsJob =
                scope.launch(ioDispatcher) {
                    while (keepGettingLiveReports.get()) {
                        val dir = if (liveReportDirectionIsSouth.get()) S else N
//                        val response = repo.getRealTimeEstimate(
//                                orig = origStationId,
//                                dir = dir
//                        )
//                        if (response.isSuccessful)
//                            realTimeEtaLiveData.postValue(response.body()?.getNextEstimate()!!)
//                        else
//                            networkErrorLiveData.postValue(response.message())
                        Log.d("TAG","do job: $dir")
                        delay(1_000)
                    }
                }
    }
    private var liveReportsJob: Job? = null

    override fun onCleared() {
        liveReportsJob?.cancel()
        keepGettingLiveReports.set(false)
        super.onCleared()
    }

    private fun fetchTripData() {
        scope.launch(ioDispatcher) {
            repo.getBartTrip(origStationId, destStationId!!).collect {
                val tripsResponse = repo.planTrip(generateRequestForm(origStationId, destStationId!!))
                val flippedTripsResponse = repo.planTrip(generateRequestForm(destStationId!!, origStationId))
                if (tripsResponse.isSuccessful && flippedTripsResponse.isSuccessful) {
                    bartTripLiveData.postValue(
                            it.apply {
                                trips = tripsResponse.body()?.getTrips()
                                flippedTrips = flippedTripsResponse.body()?.getTrips()
                            })
                } else {
                    val error = "${tripsResponse.code()}: ${tripsResponse.message()}, " +
                            "${flippedTripsResponse.code()}: ${flippedTripsResponse.message()}, "
                    networkErrorLiveData.postValue(error)
                }
            }
        }
    }

    private fun generateRequestForm(orig: String, dest: String) =
            GetTripRequest(
                    isDeparting = true,
                    time = "now",
                    orig = orig,
                    dest = dest
            )
}
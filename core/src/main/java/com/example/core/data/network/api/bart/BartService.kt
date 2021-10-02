package com.example.core.data.network.api.bart

import com.example.core.BuildConfig
import com.example.core.data.network.result.bart.GetRealTimeEstimateResult
import com.example.core.data.network.result.bart.GetStationScheduleResult
import com.example.core.data.network.result.bart.GetStationsResult
import com.example.core.data.network.result.bart.GetTripResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BartService {

    companion object {
        private const val BASE_URL = "https://api.bart.gov/api/"
        private const val SCHED = "sched.aspx"
        private const val ETD = "etd.aspx"
    }

    @GET(BASE_URL + "stn.aspx?cmd=stns&key=${BuildConfig.BART_KEY}&json=y")
    fun getStations(): Call<GetStationsResult>

    @GET(BASE_URL + SCHED)
    fun getStationSchedule(
        @Query("cmd") cmd: String = "stnsched",
        @Query("orig") stationId: String,
        @Query("key") key: String = BuildConfig.BART_KEY,
        @Query("json") json: String = "y"
    ): Call<GetStationScheduleResult>

    // cmd=depart&orig=WCRK&dest=MONT&date=now&json=y&key=MW9S-E7SL-26DU-VV8V
    // https://api.bart.gov/docs/sched/depart.aspx
    @GET(BASE_URL + SCHED)
    fun getPlanTrip(
            @Query("orig") orig: String,
            @Query("dest") dest: String,
            @Query("time") date: String = "now", // or h:mm+am/pm
            @Query("cmd") cmd: String = "depart", // or arrive
            @Query("json") json: String = "y",
            @Query("key") key: String = BuildConfig.BART_KEY
    ): Call<GetTripResult>

    @GET(BASE_URL + ETD)
    fun getRealTimeEstimate(
            @Query("plat") plat: String? = null, // platform for multi-platform stations (1-4)
            @Query("orig") orig: String, // station
            @Query("dir") dir: String?, // "s" or "n"
            @Query("cmd") cmd: String = "etd",
            @Query("json") json: String = "y",
            @Query("key") key: String = BuildConfig.BART_KEY
    ): Call<GetRealTimeEstimateResult>
}
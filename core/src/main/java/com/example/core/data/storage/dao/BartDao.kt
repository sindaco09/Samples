package com.example.core.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.data.models.bart.BartTrip
import com.example.core.data.models.bart.BartStation
import kotlinx.coroutines.flow.Flow

@Dao
interface BartDao {

    @Query("SELECT * FROM bart_stations")
    suspend fun getBartStations(): List<BartStation>?

    @Query("SELECT * FROM bart_stations")
    fun getBartStationsFlowTwo(): Flow<List<BartStation>?>

    @Query("SELECT * FROM bart_stations WHERE favorite = 1")
    fun getFavoriteBartStations(): List<BartStation>?

    @Query("SELECT * FROM bart_stations WHERE favorite = 1")
    fun getFavoriteBartStationsFlow(): Flow<List<BartStation>?>

    @Insert(onConflict = REPLACE)
    fun updateBartStation(station: BartStation)

    @Delete
    fun removeBartStation(station: BartStation)

    @Insert(onConflict = REPLACE)
    fun updateBartStations(stations: List<BartStation>)

    @Query("SELECT * FROM bart_stations WHERE abbr == :stationId LIMIT 1")
    fun getBartStationFlow(stationId: String): Flow<BartStation>

    @Query("SELECT * FROM bart_stations WHERE abbr == :stationId LIMIT 1")
    fun getBartStation(stationId: String): BartStation

    @Insert(onConflict = REPLACE)
    fun updateBartCommute(trip: BartTrip)

    @Query("SELECT * FROM bart_commute")
    fun getFavoriteBartCommuteFlow(): Flow<List<BartTrip>?>

    @Delete
    fun removeBartCommute(trip: BartTrip)

    @Query("SELECT * FROM bart_commute WHERE primaryAbbr == :origStationId AND secondaryAbbr == :destStationId OR primaryAbbr == :destStationId AND secondaryAbbr == :origStationId")
    fun getBartTripFlow(origStationId: String, destStationId: String): Flow<BartTrip>
}
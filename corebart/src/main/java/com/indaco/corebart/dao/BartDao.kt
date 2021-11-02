package com.indaco.corebart.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.indaco.corebart.models.bart.BartStationDbo
import com.indaco.corebart.models.bart.BartTripDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface BartDao {

    @Query("SELECT * FROM bart_stations")
    suspend fun getBartStations(): List<BartStationDbo>?

    @Query("SELECT * FROM bart_stations")
    fun getBartStationsFlowTwo(): Flow<List<BartStationDbo>?>

    @Query("SELECT * FROM bart_stations WHERE favorite = 1")
    fun getFavoriteBartStations(): List<BartStationDbo>?

    @Query("SELECT * FROM bart_stations WHERE favorite = 1")
    fun getFavoriteBartStationsFlow(): Flow<List<BartStationDbo>?>

    @Insert(onConflict = REPLACE)
    fun updateBartStation(station: BartStationDbo)

    @Delete
    fun removeBartStation(station: BartStationDbo)

    @Insert(onConflict = REPLACE)
    fun updateBartStations(stations: List<BartStationDbo>)

    @Query("SELECT * FROM bart_stations WHERE abbr == :stationId LIMIT 1")
    fun getBartStationFlow(stationId: String): Flow<BartStationDbo>

    @Query("SELECT * FROM bart_stations WHERE abbr == :stationId LIMIT 1")
    fun getBartStation(stationId: String): BartStationDbo

    @Insert(onConflict = REPLACE)
    fun updateBartCommute(trip: BartTripDbo)

    @Query("SELECT * FROM bart_commute")
    fun getFavoriteBartCommuteFlow(): Flow<List<BartTripDbo>?>

    @Delete
    fun removeBartCommute(trip: BartTripDbo)

    @Query("SELECT * FROM bart_commute WHERE primaryAbbr == :origStationId AND secondaryAbbr == :destStationId OR primaryAbbr == :destStationId AND secondaryAbbr == :origStationId")
    fun getBartTripFlow(origStationId: String, destStationId: String): Flow<BartTripDbo>
}
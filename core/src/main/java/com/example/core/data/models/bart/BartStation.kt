package com.example.core.data.models.bart

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.core.data.network.result.bart.GetRealTimeEstimateResult
import com.example.core.data.network.result.bart.GetStationScheduleResult
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

typealias Schedule = GetStationScheduleResult.Item
typealias ETD = GetRealTimeEstimateResult.ETD

@Parcelize
@Entity(tableName = "bart_stations")
data class BartStation(
    val name: String,
    @PrimaryKey val abbr: String,
    @SerializedName("gtfs_latitude") val latitude: Double,
    @SerializedName("gtfs_longitude") val longitude: Double,
    var favorite: Boolean = false
): BartObject(), Parcelable {

    @IgnoredOnParcel
    @Ignore
    var schedules: List<Schedule>? = null

    @IgnoredOnParcel
    @Ignore
    var etds: List<ETD>? = null

    override fun equals(other: Any?) =
            when (other) {
                null -> false
                !is BartStation -> false
                else -> other.abbr == abbr
            }

    override fun hashCode(): Int {
        return abbr.hashCode()
    }

    enum class Direction {SOUTH, NORTH;
        companion object {
            fun getDirectionByString(dir: String) =
                    when {
                        dir.equals("s",true) || dir.equals("south",true) -> SOUTH
                        dir.equals("n",true) || dir.equals("north",true) -> NORTH
                        else -> throw RuntimeException("invalid direction: $dir")
                    }
        }
    }
}
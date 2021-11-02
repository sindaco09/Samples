package com.indaco.bart.data.models

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.indaco.bart.data.network.result.GetRealTimeEstimateResult
import com.indaco.bart.data.network.result.GetStationScheduleResult
import com.indaco.corebart.models.bart.BartObject
import com.indaco.corebart.models.bart.BartStationDbo
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.lang.RuntimeException

typealias Schedule = GetStationScheduleResult.Item
typealias ETD = GetRealTimeEstimateResult.ETD

data class BartStation(
    val name: String,
    val abbr: String,
    @SerializedName("gtfs_latitude") val latitude: Double,
    @SerializedName("gtfs_longitude") val longitude: Double,
    var favorite: Boolean = false
): BartObject() {

    constructor(dbo: BartStationDbo):this(dbo.name, dbo.abbr, dbo.latitude, dbo.longitude, dbo.favorite)

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

    fun toBartStationDbo() = BartStationDbo(name, abbr, latitude, longitude, favorite)

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
package com.indaco.samples.data.models.bart

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "bart_stations")
open class BartStationDbo(
    open val name: String,
    @PrimaryKey open val abbr: String,
    @SerializedName("gtfs_latitude") open val latitude: Double,
    @SerializedName("gtfs_longitude") open val longitude: Double,
    open var favorite: Boolean = false
): BartObject(), Parcelable {

    override fun equals(other: Any?) =
            when (other) {
                null -> false
                !is BartStationDbo -> false
                else -> other.abbr == abbr
            }

    override fun hashCode(): Int = abbr.hashCode()
}
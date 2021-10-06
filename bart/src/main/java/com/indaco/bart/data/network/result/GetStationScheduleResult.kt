package com.indaco.bart.data.network.result

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.indaco.bart.data.models.BartRoute
import com.indaco.bart.data.models.BartStation
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.*
import java.time.format.DateTimeFormatter

class GetStationScheduleResult(val root: Root) {
    class Root(val station: Station)
    class Station(val item: List<Item>)

    @Parcelize
    class Item(
        @SerializedName("@line") val routeId: String,
        @SerializedName("@origTime") val time: String,
        @SerializedName("@trainHeadStation") val destination: String
    ): Parcelable {

        @IgnoredOnParcel
        var zonedDateTime: ZonedDateTime? = null
            get() {
                if (field == null) {
                    val itemTime = time
                    val lt = LocalTime.parse(itemTime, DateTimeFormatter.ofPattern("hh:mm a"))
                    val ldt = LocalDateTime.of(LocalDate.now(),lt)
                    val ztd = ZonedDateTime.of(ldt, ZoneId.of("PST"))
                    field = ztd
                }
                return field
            }

        @IgnoredOnParcel
        var route: BartRoute? = null
            get() {
                if (field == null)
                    field = BartRoute.getRouteById(routeId)
                return field
            }

        @IgnoredOnParcel
        var direction: BartStation.Direction? = null
            get() {
                if (field == null)
                    field = route?.direction
                return field
            }
    }

    fun getSchedules() = root.station.item
}
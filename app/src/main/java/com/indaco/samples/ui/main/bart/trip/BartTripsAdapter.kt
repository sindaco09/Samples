package com.indaco.samples.ui.main.bart.trip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indaco.samples.data.models.bart.BartTrip
import com.indaco.samples.databinding.RowItemCommuteBinding
import com.indaco.samples.util.common.BindableAdapter

class BartTripsAdapter(
    private val context: Context,
    private var trips: MutableList<BartTrip> = emptyList<BartTrip>().toMutableList(),
    private val callback: (action: Action, station: BartTrip, i: Int) -> Unit
): RecyclerView.Adapter<BartTripsAdapter.CommuteVH>(), BindableAdapter<BartTrip> {

    enum class Action{ REMOVE, DETAILS }

    class CommuteVH(val binding: RowItemCommuteBinding): RecyclerView.ViewHolder(binding.root)

    override fun setData(data: List<BartTrip>) {
        trips = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CommuteVH(RowItemCommuteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))

    override fun onBindViewHolder(holder: CommuteVH, position: Int) {
        val commute = trips[position]

        val station1 = commute.primaryStation!!
        val station2 = commute.secondaryStation!!

        with(holder.binding) {
            primaryStation.text = "From: ${station1.name}"
            secondaryStation.text = "To: ${station2.name}"

            icSwitch.setOnClickListener {
                if (tripsFlipper.displayedChild == MAIN) {
                    tripsFlipper.displayedChild = FLIPPED
                    primaryStation.text = "From: ${station2.name}"
                    secondaryStation.text = "To: ${station1.name}"
                } else {
                    tripsFlipper.displayedChild = MAIN
                    primaryStation.text = "From: ${station1.name}"
                    secondaryStation.text = "To: ${station2.name}"
                }
            }

            delete.setOnClickListener { callback(Action.REMOVE, commute, position) }
            container.setOnClickListener { callback(Action.DETAILS, commute, position) }

            addSchedulesToList(this, commute)
        }
    }

    private fun addSchedulesToList(binding: RowItemCommuteBinding, trip: BartTrip) {
        with(binding) {
            trainTimesMain.removeAllViews()
            trainTimesFlipped.removeAllViews()
            trip.trips?.forEach { trip ->
                val value = "${trip.origTimeMin} - ${trip.destTimeMin}| ${trip.tripTime} min"
                trainTimesMain.addView(createTimeTV(value))
            }
            trip.flippedTrips?.forEach { trip ->
                val value = "${trip.origTimeMin} - ${trip.destTimeMin}| ${trip.tripTime} min"
                trainTimesFlipped.addView(createTimeTV(value))
            }
        }
    }

    companion object {
        private const val FLIPPED = 1
        private const val MAIN = 0
    }

    private fun createTimeTV(value: String): TextView {
        return TextView(context).apply {
            text = value
        }
    }

    override fun getItemCount() = trips.size
}
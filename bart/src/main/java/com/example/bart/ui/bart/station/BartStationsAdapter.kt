package com.example.bart.ui.bart.station

import android.content.Context
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.models.bart.BartStation
import com.example.samples.databinding.RowItemStationNewBinding
import com.example.samples.ui.main.bart.details.Estimate
import com.example.samples.util.common.BindableAdapter
import com.example.samples.util.common.Ext.toPx
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BartStationsAdapter(
    private val context: Context,
    private var stations: MutableList<BartStation> = emptyList<BartStation>().toMutableList(),
    private val callback: (action: Action, station: BartStation, i: Int) -> Unit
): RecyclerView.Adapter<BartStationsAdapter.StationsVH>(), BindableAdapter<BartStation> {

    enum class Action{ REMOVE, DETAILS }

    class StationsVH(val binding: RowItemStationNewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            StationsVH(RowItemStationNewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))

    override fun onBindViewHolder(holder: StationsVH, position: Int) {
        val station = stations[position]
        val southDestinations = mutableListOf(RESET)
        val northDestinations = mutableListOf(RESET)
        var filteredSouthDestination: String? = null
        var filteredNorthDestination: String? = null

        with(holder.binding) {
            title.text = station.name
            delete.setOnClickListener { callback(Action.REMOVE, station, position) }
            container.setOnClickListener { callback(Action.DETAILS, station, position) }

            direction.setOnCheckedChangeListener { switch, isChecked ->
                directionFlipper.displayedChild = if (isChecked) NORTH else SOUTH
                switch.text = if (isChecked) "North" else "South"
            }

            filter.setOnClickListener {
                AlertDialog.Builder(context)
                        .setTitle("Filter by Station")
                        .setItems( (if (directionFlipper.displayedChild == NORTH)
                            northDestinations.toTypedArray()
                        else
                            southDestinations.toTypedArray())) { _, which ->
                            if (directionFlipper.displayedChild == NORTH) {
                                val value = northDestinations [which]
                                filteredNorthDestination = if (value == RESET) null else value
                            } else {
                                val value = southDestinations [which]
                                filteredSouthDestination = if (value == RESET) null else value
                            }

                            addEstimatesToList(
                                    holder.binding,
                                    station,
                                    filteredSouthDestination,
                                    filteredNorthDestination
                            )
                        }
                        .show()
            }

            station.etds?.forEach {
                when(it.direction!!) {
                    BartStation.Direction.SOUTH -> {
                        if (!southDestinations.contains(it.destination))
                            southDestinations.add(it.destination)
                    }
                    BartStation.Direction.NORTH -> {
                        if (!northDestinations.contains(it.destination))
                            northDestinations.add(it.destination)
                    }
                }
            }

            addEstimatesToList(this, station, filteredSouthDestination, filteredNorthDestination)

        }
    }

    private fun addEstimatesToList(
        binding: RowItemStationNewBinding,
        station: BartStation,
        filteredSouthDestination: String?,
        filteredNorthDestination: String?
    ) {
        with(binding) {
            trainTimesContainerSouth.removeAllViews()
            trainTimesContainerNorth.removeAllViews()
            station.etds?.forEach { etd ->
                var text = "<u><b>${etd.destination}:</b></u>\t\t"
                etd.estimate.forEachIndexed { i, estimate ->
                    text += getEstimateETA(estimate)
                    if (i != etd.estimate.lastIndex)
                        text += "\t\t"
                }
                when (etd.direction!!) {
                    BartStation.Direction.SOUTH -> {
                        if (filteredSouthDestination == null || etd.destination == filteredSouthDestination) {
                            val tv = createTimeTV(Html.fromHtml(text))
                            trainTimesContainerSouth.addView(tv)
                            tv.updateLayoutParams<LinearLayout.LayoutParams> { topMargin = 5.toPx() }
                        }
                    }
                    BartStation.Direction.NORTH -> {
                        if (filteredNorthDestination == null || etd.destination == filteredNorthDestination) {
                            val tv = createTimeTV(Html.fromHtml(text))
                            trainTimesContainerNorth.addView(tv)
                            tv.updateLayoutParams<LinearLayout.LayoutParams> { topMargin = 5.toPx() }
                        }
                    }
                }
            }
        }
    }

    private fun getEstimateETA(estimate: Estimate): String {
        val minutes = (estimate.minutes.toIntOrNull() ?: 0) + estimate.delay
        val localTime = LocalTime.now().plusMinutes(minutes.toLong())
        return localTime.format(DateTimeFormatter.ofPattern("h:mm a"))
    }


    companion object {
        private const val NORTH = 1
        private const val SOUTH = 0
        private const val RESET = "RESET"
    }

    private fun createTimeTV(value: Spanned): TextView = TextView(context).apply { text = value }

    override fun getItemCount() = stations.size

    override fun setData(data: List<BartStation>) {
        stations = data.toMutableList()
        notifyDataSetChanged()
    }
}

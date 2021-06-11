package com.example.samples.ui.main.bart.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samples.R
import com.example.samples.data.models.bart.BartStation
import com.example.samples.data.models.bart.BartType
import com.example.samples.databinding.FragmentBartItemListDialogBinding
import com.example.samples.databinding.RowItemBartObjectBinding
import com.example.samples.ui.main.bart.BartViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BartItemListDialogFragment() : BottomSheetDialogFragment() {

    private var _binding: FragmentBartItemListDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BartViewModel by viewModels()

    private val args by navArgs<BartItemListDialogFragmentArgs>()
    private lateinit var type: BartType
    private var primaryStation: BartStation? = null
    private var secondaryStation: BartStation? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_bart_item_list_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentBartItemListDialogBinding.bind(view)

        init()
    }

    private fun init() {
        initUi()

        observeData()

        fetchData()
    }

    private fun initUi() {
        type = args.type

        binding.save.setOnClickListener { saveAndQuit() }
    }

    private fun fetchData() = viewModel.getStations()

    private fun observeData() = viewModel.allStations.observe(this, ::setStations)

    private fun setStations(stations: List<BartStation>?) {
        if (stations == null)
            return

        with(binding.primaryStationRv) {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemAdapter(stations) { primaryStation = it }
        }

        if (type == BartType.TRIP) {
            with(binding.secondaryStationRv) {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(context)
                adapter = ItemAdapter(stations) { secondaryStation = it }
            }
        }
    }

    private fun saveAndQuit() {
        when (type) {
            BartType.STATION -> {
                if (primaryStation != null) {
                    primaryStation!!.favorite = true
                    viewModel.saveFavoriteStation(primaryStation!!)
                    dismiss()
                } else {
                    Toast.makeText(context, "Select a Station", Toast.LENGTH_SHORT).show()
                }
            }
            BartType.TRIP -> {
                if (primaryStation != null && secondaryStation != null) {
                    viewModel.saveFavoriteTrip(primaryStation!!, secondaryStation!!)
                    dismiss()
                } else {
                    Toast.makeText(context, "Select 2 Stations", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private inner class ViewHolder(val binding: RowItemBartObjectBinding) : RecyclerView.ViewHolder(binding.root)

    private inner class ItemAdapter(
        private val list: List<BartStation>,
        private val callback: (station: BartStation) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {

        var selectedItem: Pair<Int?, BartStation>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(RowItemBartObjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val station = list[position]
            with(holder.binding) {
                text.text = station.name
                item.setOnClickListener {
                    updateSelectedItem(position, station)
                    callback(station)
                }

                if (selectedItem != null && selectedItem!!.second == station) {
                    item.setBackgroundColor(context!!.getColor(R.color.design_default_color_primary))
                    text.setTextColor(context!!.getColor(R.color.white))
                } else {
                    item.setBackgroundColor(context!!.getColor(R.color.white))
                    text.setTextColor(context!!.getColor(R.color.black))
                }
            }
        }

        private fun updateSelectedItem(i: Int, station: BartStation) {
            val prevPosition = selectedItem?.first
            selectedItem = Pair(i, station)
            notifyItemChanged(i)
            if (prevPosition!=null)
                notifyItemChanged(prevPosition)
        }

        override fun getItemCount() = list.size
    }
}
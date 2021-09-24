package com.example.bart.ui.bart.details

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.bart.R
import com.example.samples.databinding.FragmentBartMapsBinding

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class BartMapsFragment : Fragment(R.layout.fragment_bart_maps) {

    private val viewModel: BartDetailsViewModel by viewModels()
    private val args by navArgs<BartMapsFragmentArgs>()
    private var _binding: FragmentBartMapsBinding? = null
    private val binding get() = _binding!!
    private val bottomBinding get() = binding.bottomSheet
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>

//    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBartMapsBinding.bind(view)

        init()
    }

    private fun init() {
        observeData()

        initUi()

//        initMap()

        fetchData()
    }

    private fun initBottomView() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomBinding.root)
        with(bottomSheetBehavior) {
            isFitToContents = false
        }
    }

//    private fun initMap() {
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync {
//            googleMap = it
//
//            fetchData()
//        }
//    }

    private fun fetchData() {
        with(viewModel) {
            init(args)

            fetchData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModel.networkErrorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.realTimeEtaLiveData.observe(viewLifecycleOwner) {
            updateRealTimeDisplay(it)

        }
        viewModel.bartStationLiveData.observe(viewLifecycleOwner) {
//            setMapMarker(listOf(it))
            setListViewsForStation(it)
        }
        viewModel.bartTripLiveData.observe(viewLifecycleOwner) {
//            setMapMarker(listOf(it.primaryStation!!,it.secondaryStation!!))
            setListViewsForTrip(it)
        }
    }

    private fun updateRealTimeDisplay(estimate: Estimate?) {
        if (estimate == null || estimate.delay == 0) {
            bottomBinding.latestReport.visibility = View.GONE
        } else {
            bottomBinding.latestReport.visibility = View.VISIBLE
            val delay = estimate.delay
            var delayString = if (delay > 0)
                String.format("%d min late", TimeUnit.SECONDS.toMinutes(delay.toLong()))
            else
                "no delays"
            delayString += "\nLast Updated: ${estimate.lastUpdated}"
        }
    }

//    private fun setMapMarker(bartStations: List<BartStation>) {
//        bartStations.forEach {
//            val latLng = LatLng(it.latitude, it.longitude)
//            googleMap.addMarker(MarkerOptions().position(latLng).title(it.name))
//        }
//        if (bartStations.size == 1) {
//            val station = bartStations.first()
//            val latLng = LatLng(station.latitude, station.longitude)
//            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15F)
//            googleMap.moveCamera(cameraUpdate)
//        } else {
//            val station1 = bartStations.first()
//            val station2 = bartStations.last()
//
//            val lowerLatLng = LatLng(
//                    maxOf(station1.latitude, station2.latitude),
//                    maxOf(station1.longitude, station2.longitude))
//            val upperLatLng = LatLng(
//                    minOf(station1.latitude, station2.latitude),
//                    minOf(station1.longitude, station2.longitude))
//
//            val newBounds = LatLngBounds(upperLatLng, lowerLatLng)
//
//            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(newBounds, 150)
//            googleMap.moveCamera(cameraUpdate)
//        }
//    }

    private fun setListViewsForTrip(bartTrip: BartTrip?) {
        if (bartTrip != null) {
            bartTrip.trips
                ?.map { "${it.origTimeMin} - ${it.destTimeMin}" }
                ?.toTypedArray()
                ?.let { bottomBinding.southboundLv.adapter = buildAdapter(it) }

            bartTrip.flippedTrips
                ?.map { "${it.origTimeMin} - ${it.destTimeMin}" }

                ?.toTypedArray()
                ?.let { bottomBinding.northboundLv.adapter = buildAdapter(it) }

            bottomBinding.tabsLayout.getTabAt(0)?.text = bartTrip.primaryStation?.name + " ->"
            bottomBinding.tabsLayout.getTabAt(1)?.text = bartTrip.secondaryStation?.name + " ->"

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        } else {
            Toast.makeText(context,"no bart trips...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListViewsForStation(station: BartStation?) {
        if (station != null ) {
            station.schedules
                ?.filter { it.direction == BartStation.Direction.SOUTH }
                ?.map { "${it.time} ${it.destination}" }
                ?.toTypedArray()
                ?.let { bottomBinding.southboundLv.adapter = buildAdapter(it) }

            station.schedules
                ?.filter { it.direction == BartStation.Direction.NORTH }
                ?.map { "${it.time} ${it.destination}" }
                ?.toTypedArray()
                ?.let { bottomBinding.northboundLv.adapter = buildAdapter(it) }

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        } else {
            Toast.makeText(context,"no bart station...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildAdapter(list: Array<String>) = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)

    private fun initUi() {
        initTabs()

        initBottomView()
    }

    private fun initTabs() {
        bottomBinding.tabsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    toggleViewFlipperAnimation(it.position)
                    bottomBinding.viewFlipper.displayedChild = it.position
                    viewModel.setLiveReportDirection(BartStation.Direction.values()[it.position])
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun toggleViewFlipperAnimation(child: Int) {
        with(bottomBinding.viewFlipper) {
            if (child == 0) {
                setInAnimation(context, R.anim.slide_in_from_left)
                setOutAnimation(context, R.anim.slide_out_to_right)
            } else {
                setInAnimation(context, R.anim.slide_in_from_right)
                setOutAnimation(context, R.anim.slide_out_to_left)
            }
        }
    }
}
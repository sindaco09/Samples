package com.indaco.samples.ui.main.bart

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.indaco.samples.R
import com.indaco.samples.data.models.bart.BartObject
import com.indaco.samples.data.models.bart.BartType
import com.indaco.samples.databinding.FragmentBartBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.indaco.samples.ui.main.bart.station.BartStationsFragment
import com.indaco.samples.ui.main.bart.trip.BartTripsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BartFragment: Fragment(R.layout.fragment_bart) {

    private val viewmodel: BartViewModel by viewModels()
    private var _binding: FragmentBartBinding? = null
    private val binding: FragmentBartBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBartBinding.bind(view)

        initUi()

        fetchData()
    }

    private fun fetchData() {
        with(viewmodel) {
            getFavoriteStations()
            getFavoriteTrips()
            bartItem.observe(viewLifecycleOwner) { displayAreYouSure(it) }
        }
    }

    fun displayAreYouSure(bartItem: BartObject) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove item")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ -> viewmodel.removeFavoriteBartObject(bartItem) }
            .setNegativeButton("no", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        initPager()

        binding.add.setOnClickListener {
            val type = BartType.values()[binding.pager.currentItem]
            val action = com.indaco.samples.ui.main.bart.BartFragmentDirections.actionNavBartToStationPicker(type)
            findNavController().navigate(action)
        }
    }

    private fun initPager() {
        binding.pager.adapter = MyPagerAdapter(this)

        TabLayoutMediator(binding.tabsLayout, binding.pager) { tab, i ->
            tab.text = if (i == 0) "Stations" else "Trips"
        }.attach()
    }
}

class MyPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private var bartStationsFragment: BartStationsFragment? = null
        get() {
            if (field == null) field = BartStationsFragment()
            return field
        }

    private var bartTripsFragment: BartTripsFragment? = null
        get() {
            if (field == null) field = BartTripsFragment()
            return field
        }

    private val frags = listOf(bartStationsFragment, bartTripsFragment)

    override fun getItemCount() = frags.size

    override fun createFragment(position: Int) = frags[position]!!
}
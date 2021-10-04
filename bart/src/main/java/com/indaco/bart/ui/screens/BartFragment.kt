package com.indaco.bart.ui.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.indaco.bart.R
import com.indaco.bart.core.hilt.Injector
import com.indaco.bart.databinding.FragmentBartBinding
import com.indaco.bart.ui.screens.station.BartStationsFragment
import com.indaco.bart.ui.screens.trip.BartTripsFragment
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.data.models.bart.BartObject
import com.indaco.samples.data.models.bart.BartType
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

class BartFragment: Fragment(R.layout.fragment_bart) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewmodel: BartViewModel by viewModels({this},{viewModelFactory})
    private val binding by viewBinding(FragmentBartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

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

    private fun initUi() {
        initPager()

        binding.add.setOnClickListener {
            val type = BartType.values()[binding.pager.currentItem]
            val action = BartFragmentDirections.actionNavBartToStationPicker(type)
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
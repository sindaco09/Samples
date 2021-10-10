package com.indaco.bart.ui.screens.trip

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.indaco.bart.R
import com.indaco.bart.core.hilt.Injector
import com.indaco.bart.data.models.BartTrip
import com.indaco.bart.data.models.BartType
import com.indaco.bart.databinding.BartTripsBinding
import com.indaco.bart.ui.screens.BartFragmentDirections
import com.indaco.bart.ui.screens.BartViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

class BartTripsFragment: Fragment(R.layout.bart_trips) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: BartViewModel by lazy { ViewModelProvider(requireParentFragment(), viewModelFactory)[BartViewModel::class.java] }
    private val binding: BartTripsBinding by viewBinding(BartTripsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        initUi()
    }

    private fun initUi() {
        binding.tripsRv.adapter = BartTripsAdapter(requireContext()) { action, trip, _ ->
            when (action) {
                BartTripsAdapter.Action.REMOVE -> viewModel.removeFavoriteTrip(trip)
                BartTripsAdapter.Action.DETAILS -> goToDetails(trip)
            }
        }

        viewModel.favoriteTripsLiveData.observe(
            viewLifecycleOwner) {
            (binding.tripsRv.adapter as BartTripsAdapter).setData(it?: emptyList())
        }
    }

    private fun goToDetails(trip: BartTrip) =
            findNavController().navigate(
                BartFragmentDirections.actionCommuteToMap(
                    "Bart Trip",
                    trip.primaryAbbr,
                    trip.secondaryAbbr,
                    BartType.TRIP
                )
            )
}
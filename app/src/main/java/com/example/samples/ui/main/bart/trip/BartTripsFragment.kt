package com.example.samples.ui.main.bart.trip

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.samples.R
import com.example.samples.data.models.bart.BartTrip
import com.example.samples.data.models.bart.BartType
import com.example.samples.databinding.BartTripsBinding
import com.example.samples.ui.main.bart.BartFragmentDirections
import com.example.samples.ui.main.bart.BartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BartTripsFragment: Fragment(R.layout.bart_trips) {

    private val viewModel: BartViewModel by viewModels({requireParentFragment()})
    private var _binding: BartTripsBinding? = null
    private val binding: BartTripsBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BartTripsBinding.bind(view).apply {
            viewModel = this@BartTripsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initUi()
    }

    private fun initUi() {
        binding.tripsRv.adapter = BartTripsAdapter(requireContext()) { action, trip, _ ->
            when (action) {
                BartTripsAdapter.Action.REMOVE -> viewModel.updateBartItem(trip)
                BartTripsAdapter.Action.DETAILS -> goToDetails(trip)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.indaco.samples.ui.main.bart.station

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.indaco.samples.R
import com.indaco.samples.data.models.bart.BartStation
import com.indaco.samples.data.models.bart.BartType
import com.indaco.samples.databinding.BartStationsBinding
import com.indaco.samples.ui.main.bart.BartFragmentDirections
import com.indaco.samples.ui.main.bart.BartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BartStationsFragment: Fragment(R.layout.bart_stations) {

    private val viewModel: BartViewModel by viewModels({requireParentFragment()})
    private var _binding: BartStationsBinding? = null
    private val binding: BartStationsBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BartStationsBinding.bind(view).apply {
            viewModel = this@BartStationsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initUi()
    }

    private fun initUi() {
        binding.stationsRv.adapter = BartStationsAdapter(requireContext()) { action, station, _ ->
            when (action) {
                BartStationsAdapter.Action.REMOVE -> viewModel.updateBartItem(station)
                BartStationsAdapter.Action.DETAILS -> goToDetails(station)
            }
        }
    }

    private fun goToDetails(station: BartStation) =
            findNavController().navigate(
                com.indaco.samples.ui.main.bart.BartFragmentDirections.actionCommuteToMap(
                    station.name,
                    station.abbr,
                    null,
                    BartType.STATION
                )
            )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
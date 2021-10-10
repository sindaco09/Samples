package com.indaco.bart.ui.screens.station

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.indaco.bart.R
import com.indaco.bart.core.hilt.Injector
import com.indaco.bart.data.models.BartStation
import com.indaco.bart.data.models.BartType
import com.indaco.bart.databinding.BartStationsBinding
import com.indaco.bart.ui.screens.BartFragmentDirections
import com.indaco.bart.ui.screens.BartViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

class BartStationsFragment: Fragment(R.layout.bart_stations) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: BartViewModel by viewModels({ requireParentFragment()}, {viewModelFactory})
    private val binding: BartStationsBinding by viewBinding(BartStationsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        initUi()
    }

    private fun initUi() {
        binding.stationsRv.adapter = BartStationsAdapter(requireContext()) { action, station, _ ->
            when (action) {
                BartStationsAdapter.Action.REMOVE -> viewModel.updateBartItem(station)
                BartStationsAdapter.Action.DETAILS -> goToDetails(station)
            }
        }

        viewModel.favoriteStationsLiveData.observe(
            viewLifecycleOwner,
            Observer((binding.stationsRv.adapter as BartStationsAdapter)::setData)
        )
    }

    private fun goToDetails(station: BartStation) =
            findNavController().navigate(
                BartFragmentDirections.actionCommuteToMap(
                    station.name,
                    station.abbr,
                    null,
                    BartType.STATION
                )
            )
}
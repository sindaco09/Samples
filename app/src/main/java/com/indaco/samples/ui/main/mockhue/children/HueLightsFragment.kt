package com.indaco.samples.ui.main.mockhue.children

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.indaco.samples.R
import com.indaco.samples.databinding.FragmentHueLightsBinding
import com.indaco.samples.ui.base.DataBindingFragment
import com.indaco.samples.ui.main.mockhue.HueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HueLightsFragment: DataBindingFragment<FragmentHueLightsBinding>(R.layout.fragment_hue_lights) {

    private val viewModel: HueViewModel by viewModels({requireParentFragment().requireParentFragment()})

    private val lightViewModel: HueLightViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        binding.recyclerView.adapter = LightAdapter(lightViewModel, viewLifecycleOwner.lifecycleScope)
    }
}
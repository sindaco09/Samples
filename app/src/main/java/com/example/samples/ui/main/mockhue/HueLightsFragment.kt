package com.example.samples.ui.main.mockhue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.samples.R
import com.example.samples.data.models.mockhue.Light
import com.example.samples.databinding.FragmentHueLightsBinding
import com.example.samples.ui.base.DataBindingFragment
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
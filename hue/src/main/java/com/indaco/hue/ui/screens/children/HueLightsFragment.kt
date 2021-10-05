package com.indaco.hue.ui.screens.children

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.indaco.hue.R
import com.indaco.hue.core.hilt.Injector
import com.indaco.hue.databinding.FragmentHueLightsBinding
import com.indaco.hue.ui.screens.HueViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

class HueLightsFragment: Fragment(R.layout.fragment_hue_lights) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: HueViewModel by viewModels({requireParentFragment().requireParentFragment()},{viewModelFactory})
    private val binding: FragmentHueLightsBinding by viewBinding(FragmentHueLightsBinding::bind)

    private val lightViewModel: HueLightViewModel by viewModels{viewModelFactory}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        binding.recyclerView.adapter = LightAdapter(lightViewModel, viewLifecycleOwner.lifecycleScope)

        binding.addLight.setOnClickListener { viewModel.addLight() }
    }
}
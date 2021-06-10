package com.example.samples.ui.main.mockhue.children

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.samples.R
import com.example.samples.databinding.FragmentConnectionBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.ui.main.mockhue.HueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HueConnectionFragment: DataBindingFragment<FragmentConnectionBinding>(R.layout.fragment_connection) {

    private val viewModel: HueViewModel by viewModels({requireParentFragment().requireParentFragment()})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

    }
}
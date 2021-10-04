package com.indaco.hue.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.indaco.hue.R
import com.indaco.hue.core.hilt.Injector
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/*
 * Demonstrate embedding a navigation graph inside a fragment
 * the viewmodel is shared between fragment A and B
 * ISSUE! pressing back does not respect backstack of embedded navgraph
 */
class HueParentFragment: Fragment(R.layout.fragment_parent) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: HueViewModel by viewModels({this},{viewModelFactory})

    private lateinit var childController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        val localNavHost = childFragmentManager.findFragmentById(R.id.child_nav_host_fragment) as NavHostFragment
        childController = localNavHost.navController

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when (it) {
                    HueViewModel.State.NOT_CONNECTED ->
                        childController.navigate(R.id.lights_fragment)
                    HueViewModel.State.CONNECTED ->
                        childController.navigate(R.id.bridge_discovery_fragment)
                }
            }
        }
    }
}
package com.example.samples.ui.main.mockhue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.samples.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/*
 * Demonstrate embedding a navigation graph inside a fragment
 * the viewmodel is shared between fragment A and B
 * ISSUE! pressing back does not respect backstack of embedded navgraph
 */
@AndroidEntryPoint
class HueParentFragment: Fragment(R.layout.fragment_parent) {

    private val viewModel: HueViewModel by viewModels()

    private lateinit var childController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
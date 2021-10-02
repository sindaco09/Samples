package com.indaco.coffee.ui.screens

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.indaco.coffee.R
import com.indaco.coffee.databinding.FragmentCoffeeBinding
import com.indaco.samples.ui.base.DataBindingFragment
import com.indaco.samples.util.notifications.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint

/*
 * Test
 * DeepLinking to CoffeeFinalFragment in NavGraph
 * DataBinding with ViewModel and skip this fragment layer of communication
 * StateFlow in CoffeeMachine
 */
@AndroidEntryPoint
class CoffeeMachineFragment : DataBindingFragment<FragmentCoffeeBinding>(R.layout.fragment_coffee) {

    private val viewModel: CoffeeMachineViewModel by viewModels()

    private var prevProgress = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        observeData()

        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        binding.addForm.setOnClickListener {
            childFragmentManager.commit {
                add(CoffeeFormDialog(),"dialog")
            }
        }

        with(binding.progressMeter) {
            max = 100
            min = 0
            interpolator = AccelerateDecelerateInterpolator()
        }

        binding.notify.setOnClickListener { NotificationUtil.notifyApp(requireContext(), findNavController()) }
    }

    private fun observeData() {
        viewModel.orderFormExists.observe(viewLifecycleOwner) {
            binding.formStatus.setImageResource(if (it) R.drawable.ic_check else R.drawable.ic_clear)
        }

        observeProgress()
    }


    private fun observeProgress() = viewModel.progress.observe(viewLifecycleOwner) {
        with(binding.progressMeter) {
            delayedSmoothProgress(prevProgress, it.first, it.second.toLong())
            prevProgress = it.first
        }
    }

    fun LinearProgressIndicator.delayedSmoothProgress(start: Int, end: Int, delay: Long = 250) {
        progress = start
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            ObjectAnimator.ofInt(this, "progress", start, end).start()
            invalidate()
        }, delay)
    }

}
package com.example.newandroidxcomponentsdemo.ui.main.coffee

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
import com.example.newandroidxcomponentsdemo.R
import com.example.newandroidxcomponentsdemo.databinding.FragmentCoffeeBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.example.newandroidxcomponentsdemo.ui.base.DataBindingFragment
import com.example.newandroidxcomponentsdemo.util.notifications.NotificationUtil
import com.indaco.myhomeapp.ui.main.coffee.CoffeeMachineViewModel
import dagger.hilt.android.AndroidEntryPoint

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

//            findNavController().navigate(CoffeeMachineFragmentDirections.actionOpenOrderForm())
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


    fun observeProgress() = viewModel.progress.observe(viewLifecycleOwner) {
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
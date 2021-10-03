package com.indaco.coffee.ui.screens

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.indaco.coffee.R
import com.indaco.coffee.databinding.FragmentCoffeeBinding
import com.indaco.samples.ui.base.DataBindingFragment
import com.indaco.samples.util.notifications.NotificationUtil

/*
 * Test
 * DeepLinking to CoffeeFinalFragment in NavGraph
 * DataBinding with ViewModel and skip this fragment layer of communication
 * StateFlow in CoffeeMachine
 */

class CoffeeMachineFragment : DataBindingFragment<FragmentCoffeeBinding>(R.layout.fragment_coffee) {

    private val viewModel: CoffeeMachineViewModel by viewModels()

    // DataBinding isn't working in dynamic modules
    private val addForm: MaterialButton by lazy { binding.root.findViewById(R.id.add_form) }
    private val progressMeter: LinearProgressIndicator by lazy { binding.root.findViewById(R.id.progress_meter) }
    private val notify: MaterialButton by lazy { binding.root.findViewById(R.id.notify) }
    private val formStatus: ImageView by lazy { binding.root.findViewById(R.id.form_status) }

    private var prevProgress = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        observeData()

        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        addForm.setOnClickListener {
            childFragmentManager.commit {
                add(CoffeeFormDialog(),"dialog")
            }
        }

        with(progressMeter) {
            max = 100
            min = 0
            interpolator = AccelerateDecelerateInterpolator()
        }

        notify.setOnClickListener { NotificationUtil.notifyApp(requireContext(), findNavController()) }
    }

    private fun observeData() {
        viewModel.orderFormExists.observe(viewLifecycleOwner) {
            formStatus.setImageResource(if (it) R.drawable.ic_check else R.drawable.ic_clear)
        }

        observeProgress()
    }


    private fun observeProgress() = viewModel.progress.observe(viewLifecycleOwner) {
        with(progressMeter) {
            delayedSmoothProgress(prevProgress, it.first, it.second.toLong())
            prevProgress = it.first
        }
    }

    fun LinearProgressIndicator.delayedSmoothProgress(start: Int, end: Int, delay: Long = 250) {
        progress = start
            Handler(Looper.getMainLooper()).postDelayed({
            ObjectAnimator.ofInt(this, "progress", start, end).start()
            invalidate()
        }, delay)
    }

}
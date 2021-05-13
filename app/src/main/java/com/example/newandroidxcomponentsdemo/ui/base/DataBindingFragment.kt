package com.example.newandroidxcomponentsdemo.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

open class DataBindingFragment<B: ViewDataBinding>(
    @LayoutRes val layoutResId: Int
): Fragment(layoutResId) {

    private var _binding: B? = null
    val binding: B get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)
        _binding!!.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
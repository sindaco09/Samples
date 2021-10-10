package com.indaco.coffee.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.indaco.coffee.R
import com.indaco.coffee.data.models.Coffee
import com.indaco.coffee.data.models.Roast
import com.indaco.coffee.data.models.Size
import com.indaco.coffee.data.models.Temperature
import com.indaco.coffee.databinding.CoffeeFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoffeeFormDialog: DialogFragment(R.layout.coffee_form) {

    private var coffeeOrder: Coffee = Coffee()
    private val viewModel: CoffeeMachineViewModel by viewModels({requireParentFragment()})
    private var _binding: CoffeeFormBinding? = null
    private val binding: CoffeeFormBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Material_Light_Dialog_MinWidth
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = CoffeeFormBinding.bind(view)

        init()
    }

    private fun init() {
        initStrongSwitch()

        initSizeSpinner()

        initRoastSpinner()

        initTempSpinner()

        initSaveButton()
    }

    private fun initStrongSwitch() {
        with(binding.strong) {
            isChecked = coffeeOrder.strong
            setOnCheckedChangeListener { _, isChecked ->
                coffeeOrder.strong = isChecked
            }
        }
    }

    private fun initSaveButton() {
        binding.submit.setOnClickListener {
            viewModel.coffeeOrder = coffeeOrder
            viewModel.orderFormExists.postValue(viewModel.coffeeOrder != null)
            Log.d("TAG","orderform: ${viewModel.coffeeOrder != null}")
            dismiss()
        }
    }

    private fun initTempSpinner() {
        with(binding.tempPicker) {
            val temps = listOf(Temperature.COLD_BREW.name, Temperature.WARM.name, Temperature.HOT.name)
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, temps)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.d("TAG","temperature selected")
                    coffeeOrder.temperature = Temperature.valueOf(temps[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun initSizeSpinner() {
        with(binding.sizePicker) {
            val sizes = listOf(Size.SMALL.name, Size.MEDIUM.name, Size.LARGE.name)
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sizes)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.d("TAG","size selected")
                    coffeeOrder.size = Size.valueOf(sizes[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun initRoastSpinner() {
        with(binding.roastPicker) {
            val roasts = listOf(Roast.LIGHT.name, Roast.MEDIUM.name, Roast.DARK.name)
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roasts)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.d("TAG","bean selected")
                    coffeeOrder.bean = Roast.valueOf(roasts[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }
}
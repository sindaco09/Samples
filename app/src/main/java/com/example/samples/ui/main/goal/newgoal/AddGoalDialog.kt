package com.example.samples.ui.main.goal.newgoal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.samples.R
import com.example.samples.databinding.AddGoalFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGoalDialog: DialogFragment(R.layout.add_goal_form) {

    private val viewModel: AddGoalDialogViewModel by viewModels()
    private var _binding: AddGoalFormBinding? = null
    private val binding: AddGoalFormBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AddGoalFormBinding.bind(view).also {
            it.viewmodel = viewModel
            it.submit.setOnClickListener { dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
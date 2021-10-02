package com.indaco.samples.ui.main.goal.newgoal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.indaco.samples.R
import com.indaco.samples.databinding.AddGoalFormBinding
import com.indaco.samples.ui.main.goal.GoalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGoalDialog: DialogFragment(R.layout.add_goal_form) {

    private val viewModel: GoalViewModel by viewModels({requireParentFragment().requireParentFragment()})
    private var _binding: AddGoalFormBinding? = null

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

            it.submit.setOnClickListener { addGoal() }
        }
    }

    private fun addGoal() {
        viewModel.addNewGoal(_binding?.goalEntry?.text.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.indaco.goals.ui.screens.newgoal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.indaco.goals.R
import com.indaco.goals.core.Injector
import com.indaco.goals.databinding.AddGoalFormBinding
import com.indaco.goals.ui.screens.GoalViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import javax.inject.Inject

class AddGoalDialog: DialogFragment(R.layout.add_goal_form) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GoalViewModel

    private var binding: AddGoalFormBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth
        )

        Injector.from(requireContext()).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment().requireParentFragment(), viewModelFactory)[GoalViewModel::class.java]

        binding = AddGoalFormBinding.bind(view).also {
            it.submit.setOnClickListener { addGoal() }
        }
    }

    private fun addGoal() {
        viewModel.addNewGoal(binding?.goalEntry?.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
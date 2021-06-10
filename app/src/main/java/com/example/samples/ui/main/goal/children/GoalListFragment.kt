package com.example.samples.ui.main.goal.children

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.samples.R
import com.example.samples.data.models.goal.Goal
import com.example.samples.data.models.goal.GoalStatus
import com.example.samples.databinding.FragmentTaskChildBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.ui.main.goal.GoalViewModel

/*
 * Generic fragment holding a recyclerview that can drag & drop it's items
 * between other GoalListFragments
 */
class GoalListFragment(val goalStatus: GoalStatus): DataBindingFragment<FragmentTaskChildBinding>(R.layout.fragment_task_child) {

    private val viewModel: GoalViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.recyclerView.setOnDragListener(GoalAdapter.AdapterDragAndDropListener(goalStatus, viewModel))
        observeData()
    }

    private fun observeData() {
        when (goalStatus) {
            GoalStatus.TODO -> viewModel.toDoGoalLiveData.observe(viewLifecycleOwner, Observer(::setGoalData))
            GoalStatus.IN_PROGRESS -> viewModel.inProgressGoalLiveData.observe(viewLifecycleOwner, Observer(::setGoalData))
            GoalStatus.DONE -> viewModel.doneGoalLiveData.observe(viewLifecycleOwner, Observer(::setGoalData))
        }
    }

    private fun setGoalData(list: List<Goal>) {
        binding.recyclerView.adapter = GoalAdapter(list.toMutableList())
    }
}
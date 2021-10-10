package com.indaco.goals.ui.screens.children

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.indaco.goals.R
import com.indaco.goals.core.Injector
import com.indaco.goals.databinding.FragmentTaskChildBinding
import com.indaco.goals.ui.screens.GoalViewModel
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.data.models.goal.GoalStatus
import com.indaco.samples.data.models.goal.Goals
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

/*
 * Generic fragment holding a recyclerview that can drag & drop it's items
 * between other GoalListFragments
 */
class GoalListFragment(val goalStatus: GoalStatus): Fragment(R.layout.fragment_task_child) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: GoalViewModel by viewModels({requireParentFragment()},{viewModelFactory})
    private val binding by viewBinding(FragmentTaskChildBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

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

    private fun setGoalData(list: Goals) {
        binding.recyclerView.adapter = GoalAdapter(list?.toMutableList() ?: mutableListOf())
    }
}
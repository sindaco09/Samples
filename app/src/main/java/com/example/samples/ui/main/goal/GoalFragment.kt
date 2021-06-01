package com.example.samples.ui.main.goal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.samples.R
import com.example.samples.databinding.FragmentGoalsBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.ui.main.goal.children.DoneFragment
import com.example.samples.ui.main.goal.children.InProgressFragment
import com.example.samples.ui.main.goal.children.ToDoFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment: DataBindingFragment<FragmentGoalsBinding>(R.layout.fragment_goals) {

    private val viewModel: GoalViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initPager()

        fetchData()

        initFab()
    }

    private fun initPager() {
        binding.pager.adapter = TasksPagerAdapter(this)

        TabLayoutMediator(binding.tabsLayout, binding.pager) { tab, i ->
            tab.text = when (i) {
                0 -> "To Do"
                1 -> "In Progress"
                else -> "Done"
            }
        }.attach()
    }

    private fun fetchData() {
        viewModel.getGoals()
    }

    private fun initFab() {
        binding.add.setOnClickListener {
            findNavController().navigate(GoalsFragmentDirections.actionNavGoalsToAddGoalDialog())
        }
    }
}

class TasksPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private var toDoFragment = ToDoFragment()
    private var inProgressFragment = InProgressFragment()
    private var doneFragment = DoneFragment()

    private val frags = listOf(toDoFragment, inProgressFragment, doneFragment)

    override fun getItemCount(): Int = frags.size

    override fun createFragment(position: Int): Fragment = frags[position]

}
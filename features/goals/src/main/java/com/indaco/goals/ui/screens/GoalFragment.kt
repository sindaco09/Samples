package com.indaco.goals.ui.screens

import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.indaco.goals.R
import com.indaco.goals.core.Injector
import com.indaco.goals.databinding.FragmentGoalsBinding
import com.indaco.goals.ui.screens.children.GoalAdapter
import com.indaco.goals.ui.screens.children.GoalListFragment
import com.indaco.goals.ui.screens.newgoal.AddGoalDialog
import com.indaco.samples.core.hilt.viewmodel.ViewModelFactory
import com.indaco.samples.data.models.goal.GoalStatus
import com.indaco.samples.util.viewBinding
import javax.inject.Inject

/*
 * Demonstrates dragging and dropping views from one list into another
 */

class GoalsFragment: Fragment(R.layout.fragment_goals) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<GoalViewModel> { viewModelFactory }
    private val binding by viewBinding(FragmentGoalsBinding::bind)

    private lateinit var taskPager: TasksPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.from(requireContext()).inject(this)

        init()
    }

    private fun init() {
        initPager()

        fetchData()

        initFab()

        initBounds()
    }

    private fun initBounds() {
        with(binding) {
            endBound.setOnDragListener(DragListener(endBound) { nextFragment(pager) })
            startBound.setOnDragListener(DragListener(startBound) { previousFragment(pager) })
        }
    }

    private fun nextFragment(pager: ViewPager2) {
        val currentItem = pager.currentItem
        if (currentItem <= pager.childCount)
            pager.setCurrentItem(currentItem + 1, true)
    }

    private fun previousFragment(pager: ViewPager2) {
        val currentItem = pager.currentItem
        if (currentItem > 0)
            pager.setCurrentItem(currentItem - 1, true)
    }

    private fun initPager() {
        taskPager = TasksPagerAdapter(this)
        binding.pager.adapter = taskPager

        TabLayoutMediator(binding.tabsLayout, binding.pager) { tab, i ->
            tab.text = taskPager.getTabTitle(i)
        }.attach()
    }

    private fun fetchData() {
        viewModel.getGoals()
    }

    private fun initFab() {
        binding.add.setOnClickListener {
            childFragmentManager.commit {
                add(AddGoalDialog(),"dialog")
            }
        }
    }

    inner class DragListener(val bound: View, private val func: () -> Unit): View.OnDragListener {

        override fun onDrag(v: View?, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> func.invoke()
                DragEvent.ACTION_DRAG_STARTED -> bound.animate().alpha(1f).setDuration(150).start()
                DragEvent.ACTION_DRAG_ENDED -> bound.animate().alpha(0f).setDuration(100).start()
                DragEvent.ACTION_DROP -> {
                    val rowItem = GoalAdapter.AdapterDragAndDropListener.getRowItem(event.clipData)

                    val goalStatus = taskPager.getTabGoalStatus(binding.pager.currentItem)

                    viewModel.updateGoal(rowItem.goal.apply { status = goalStatus })
                }
            }
            return true
        }
    }
}

class TasksPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val tabFrags = List(GoalStatus.values().size) {
        val goalStatus = GoalStatus.values()[it]
        TabFragment(GoalListFragment(goalStatus), goalStatus)
    }

    class TabFragment(val fragment: Fragment, val status: GoalStatus)

    override fun getItemCount(): Int = tabFrags.size

    override fun createFragment(position: Int): Fragment = tabFrags[position].fragment

    fun getTabGoalStatus(i: Int): GoalStatus = tabFrags[i].status

    fun getTabTitle(i: Int): String = getTabGoalStatus(i).title
}


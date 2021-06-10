package com.example.samples.ui.main.goal

import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.samples.R
import com.example.samples.data.models.goal.GoalStatus
import com.example.samples.databinding.FragmentGoalsBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.ui.main.goal.children.*
import com.example.samples.ui.main.goal.newgoal.AddGoalDialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/*
 * Demonstrates dragging and dropping views from one list into another
 */
@AndroidEntryPoint
class GoalsFragment: DataBindingFragment<FragmentGoalsBinding>(R.layout.fragment_goals) {

    private val viewModel: GoalViewModel by viewModels()
    private lateinit var taskPager: TasksPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


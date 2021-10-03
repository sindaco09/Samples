package com.indaco.goals.ui.screens.newgoal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indaco.goals.data.repository.GoalRepository
import com.indaco.samples.data.models.goal.Goal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddGoalDialogViewModel @Inject constructor(private val repository: GoalRepository): ViewModel() {

    private val dispatcher = Dispatchers.Default

    // TODO: better way to let fragment know to dismiss dialog than via liveData?
    val dismissDialog = MutableLiveData(false)

    fun addNewGoal(goal: String) {
        if (goal.isNotBlank()) {
            viewModelScope.launch(dispatcher) {
                repository.addGoal(Goal(todoItem = goal))
                dismissDialog.postValue(true)
            }
        } else {
            Log.e("TAG", "newGoal is empty: goal: $goal")
        }
    }
}
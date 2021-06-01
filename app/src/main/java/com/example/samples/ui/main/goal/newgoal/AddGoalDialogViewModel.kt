package com.example.samples.ui.main.goal.newgoal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samples.data.models.goal.Goal
import com.example.samples.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGoalDialogViewModel @Inject constructor(
    private val repository: GoalRepository
): ViewModel() {

    private val dispatcher = Dispatchers.Default

    var newGoalLiveData = MutableLiveData("")

    fun addNewGoal() {
        val newGoal = newGoalLiveData.value
        if (newGoal.isNullOrBlank()) {
            Log.d("TAG","newGoal is $newGoal")
            viewModelScope.launch(dispatcher) {
                repository.addGoal(Goal(todoItem = newGoal!!))
            }
        } else {
            Log.e("TAG","newGoal is empty")
        }
    }
}
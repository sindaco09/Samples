package com.example.samples.ui.main.goal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samples.data.models.goal.Goal
import com.example.samples.data.models.goal.GoalStatus
import com.example.samples.data.models.goal.Goals
import com.example.samples.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(private val repository: GoalRepository): ViewModel() {

    private val defaultDispatcher = Dispatchers.Default

    private val _doneGoalLiveData = MutableLiveData<Goals>()
    val doneGoalLiveData: LiveData<Goals> get() = _doneGoalLiveData

    private val _inProgressGoalLiveData = MutableLiveData<Goals>()
    val inProgressGoalLiveData: LiveData<Goals> get() = _inProgressGoalLiveData

    private val _toDoGoalLiveData = MutableLiveData<Goals>()
    val toDoGoalLiveData: LiveData<Goals> get() = _toDoGoalLiveData
    
    fun getGoals() {
        viewModelScope.launch(defaultDispatcher) {
            repository.getGoals().collect { goals ->
                _toDoGoalLiveData.postValue(goals?.filter { it.status == GoalStatus.TODO } ?: emptyList())
                _inProgressGoalLiveData.postValue(goals?.filter { it.status == GoalStatus.IN_PROGRESS } ?: emptyList())
                _doneGoalLiveData.postValue(goals?.filter { it.status == GoalStatus.DONE } ?: emptyList())
            }
        }
    }

    fun addNewGoal(goal: String) {
        if (goal.isNotBlank()) {
            viewModelScope.launch(defaultDispatcher) {
                repository.addGoal(Goal(todoItem = goal))
            }
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch(defaultDispatcher) {
            repository.updateGoal(goal)
            getGoals()
        }
    }
}
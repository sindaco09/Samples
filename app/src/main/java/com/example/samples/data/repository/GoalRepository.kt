package com.example.samples.data.repository

import com.example.samples.data.models.goal.Goal
import com.example.samples.data.storage.tasks.GoalCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(private val goalCache: GoalCache) {

    fun addGoal(goal: Goal) {
        goalCache.addGoal(goal)
    }

    fun getGoals(): Flow<List<Goal>?> {
        return goalCache.getGoals()
    }

    fun updateGoal(goal: Goal) {
        goalCache.updateGoal(goal)
    }

    fun removeGoal(goal: Goal) {
        goalCache.removeGoal(goal)
    }
}
package com.indaco.goals.data.repository

import com.indaco.goals.data.storage.GoalCache
import com.indaco.samples.data.models.goal.Goal
import com.indaco.samples.data.models.goal.Goals
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(private val goalCache: GoalCache) {

    fun addGoal(goal: Goal) {
        goalCache.addGoal(goal)
    }

    fun getGoals(): Flow<Goals> {
        return goalCache.getGoals()
    }

    fun updateGoal(goal: Goal) {
        goalCache.updateGoal(goal)
    }

    fun removeGoal(goal: Goal) {
        goalCache.removeGoal(goal)
    }
}
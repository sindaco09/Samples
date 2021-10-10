package com.indaco.goals.data.storage

import com.indaco.samples.data.models.goal.Goal
import com.indaco.samples.data.models.goal.Goals
import com.indaco.samples.data.storage.goal.GoalDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalCache @Inject constructor(private val goalDao: GoalDao) {

    fun addGoal(goal: Goal) = goalDao.addGoal(goal)

    fun getGoals(): Flow<Goals> = goalDao.getGoals()

    fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)

    fun removeGoal(goal: Goal) = goalDao.removeGoal(goal)

}
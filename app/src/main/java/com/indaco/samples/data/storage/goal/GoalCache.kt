package com.indaco.samples.data.storage.goal

import com.indaco.samples.data.models.goal.Goal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalCache @Inject constructor(private val goalDao: GoalDao) {

    fun addGoal(goal: Goal) = goalDao.addGoal(goal)

    fun getGoals() = goalDao.getGoals()

    fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)

    fun removeGoal(goal: Goal) = goalDao.removeGoal(goal)

}
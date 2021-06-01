package com.example.samples.data.storage.tasks

import androidx.room.*
import com.example.samples.data.models.goal.Goal

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGoal(goal: Goal)

    @Query("SELECT * FROM goals")
    fun getGoals(): List<Goal>?

    @Query("SELECT * FROM goals WHERE id == :uuid LIMIT 1 ")
    fun getGoal(uuid: String): Goal?

    @Update
    fun updateGoal(goal: Goal)

    @Delete
    fun removeGoal(goal: Goal)
}
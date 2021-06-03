package com.example.samples.data.storage.goal

import androidx.room.*
import com.example.samples.data.models.goal.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGoal(goal: Goal)

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<Goal>?>

    @Query("SELECT * FROM goals WHERE id == :uuid LIMIT 1 ")
    fun getGoal(uuid: String): Goal?

    @Update
    fun updateGoal(goal: Goal)

    @Delete
    fun removeGoal(goal: Goal)
}
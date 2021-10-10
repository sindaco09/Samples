package com.indaco.goals.data.models

import com.indaco.samples.data.models.goal.Goal
import java.io.Serializable

class IndexedGoal(val position: Int, val goal: Goal): Serializable {
    companion object {
        const val GOAL = "goal"
    }
}
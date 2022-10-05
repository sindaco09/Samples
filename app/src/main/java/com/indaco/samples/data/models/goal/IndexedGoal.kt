package com.indaco.samples.data.models.goal

import java.io.Serializable

class IndexedGoal(val position: Int, val goal: Goal): Serializable {
    companion object {
        const val GOAL = "goal"
    }
}
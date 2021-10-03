package com.indaco.samples.data.models.goal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

typealias Goals = List<Goal>?

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var status: GoalStatus = GoalStatus.TODO,
    var todoItem: String): Serializable {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            !is Goal -> false
            else -> other.id == id
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
package com.example.samples.data.models.goal

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

typealias Goals = List<Goal>

@Entity(tableName = "goals")
@Parcelize
data class Goal(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var status: GoalStatus = GoalStatus.TODO,
    var todoItem: String): Parcelable {

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
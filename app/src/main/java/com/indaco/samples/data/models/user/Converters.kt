package com.indaco.samples.data.models.user

import androidx.room.TypeConverter
import com.indaco.samples.data.models.goal.GoalStatus

class Converters {

    @TypeConverter
    fun fromGender(ordinal: Int = Gender.NONE.ordinal): Gender = Gender.values()[ordinal]

    @TypeConverter
    fun toGender(gender: Gender = Gender.NONE): Int = gender.ordinal


    @TypeConverter
    fun fromGoalStatus(ordinal: Int = GoalStatus.TODO.ordinal): GoalStatus = GoalStatus.values()[ordinal]

    @TypeConverter
    fun toGoalStatus(status: GoalStatus = GoalStatus.TODO): Int = status.ordinal

}
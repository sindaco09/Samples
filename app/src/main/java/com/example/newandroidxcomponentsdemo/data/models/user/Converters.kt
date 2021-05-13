package com.example.newandroidxcomponentsdemo.data.models.user

import androidx.room.TypeConverter
import com.example.newandroidxcomponentsdemo.data.models.user.Gender

class Converters {

    @TypeConverter
    fun fromGender(ordinal: Int = Gender.NONE.ordinal): Gender = Gender.values()[ordinal]

    @TypeConverter
    fun toGender(gender: Gender = Gender.NONE): Int = gender.ordinal

}
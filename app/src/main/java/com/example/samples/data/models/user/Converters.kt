package com.example.samples.data.models.user

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromGender(ordinal: Int = Gender.NONE.ordinal): Gender = Gender.values()[ordinal]

    @TypeConverter
    fun toGender(gender: Gender = Gender.NONE): Int = gender.ordinal

}
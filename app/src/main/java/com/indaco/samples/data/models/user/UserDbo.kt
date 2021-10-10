package com.indaco.samples.data.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserDbo(
    @PrimaryKey var username: String = "",
    var password: String = "",
    var age: Int = 0,
    var email: String? = null,
    var gender: Gender = Gender.NONE
)
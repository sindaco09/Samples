package com.indaco.samples.data.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
    @PrimaryKey var username: String = "",
    var password: String? = null,
    var age: Int = 0,
    var email: String? = null,
    var gender: Gender = Gender.NONE,
    var token: String? = null
)
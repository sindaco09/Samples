package com.indaco.core.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.indaco.core.models.user.Gender

@Entity(tableName = "users")
class User(
    @PrimaryKey var username: String = "",
    var password: String? = null,
    var age: Int = 0,
    var email: String? = null,
    var gender: Gender = Gender.NONE,
    var token: String? = null
)
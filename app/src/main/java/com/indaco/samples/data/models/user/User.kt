package com.indaco.samples.data.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
    @PrimaryKey var username: String = "",
    var password: String = "",
    var age: Int = 0,
    var email: String? = null,
    var gender: Gender = Gender.NONE
) {
    constructor(signUpUser: SignUpUser) : this(
        signUpUser.usernameET,
        signUpUser.passwordET,
        signUpUser.age!!,
        signUpUser.emailET,
        signUpUser.gender
    )
}
package com.indaco.auth.data.models

import com.indaco.samples.data.models.user.Gender
import com.indaco.samples.data.models.user.User

class SignUpUser(
    var usernameET: String = "",
    var ageET: String = "",
    var passwordET: String = "",
    var confirmPasswordET: String = "",
    var emailET: String? = null
) {
    fun toUser(): User =
        User(usernameET, passwordET, age, emailET, gender)

    var male: Boolean = false
    var female: Boolean = false

    val age: Int get() = ageET.toIntOrNull() ?: 1

    val gender: Gender
        get() = when {
        male -> Gender.MALE
        female -> Gender.FEMALE
        else -> Gender.NONE
    }
}
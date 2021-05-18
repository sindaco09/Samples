package com.example.samples.data.models.user

class SignUpUser(
    var usernameET: String = "",
    var ageET: String = "",
    var passwordET: String = "",
    var confirmPasswordET: String = "",
    var emailET: String? = null
) {

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
package com.indaco.samples.util

import android.util.Log
import android.util.Patterns

object Validator {

    enum class FieldType {DEFAULT, EMAIL, PASSWORD}

    private val gmailRegex = Regex("[a-zA-Z0-9+._%\\-]{1,256}@gmail.com")

    fun fieldIsValid(field: String?, fieldType: FieldType = FieldType.DEFAULT): Pair<Boolean, String?> {
        return when {
            field.isNullOrEmpty() -> Pair(false, "field is null or empty")
            fieldType == FieldType.EMAIL -> {
                if (matchesEmailRegex(field))
                    Pair(true, null)
                else
                    Pair(false, "invalid email format")
            }
            fieldType == FieldType.PASSWORD -> {
                if (field.length < PASSWORD_MIN)
                    Pair(false, "password not long enough")
                else
                    Pair(true, null)
            }
            else -> Pair(false, "else...")
        }
    }

    fun emailIsValid(email: String?): Pair<Boolean, String?> {
        Log.d("TAG","email: $email")
        return when {
            email.isNullOrEmpty() -> Pair(false, "email is null or empty")
            matchesEmailRegex(email) -> Pair(true, null)
            else -> Pair(false, "else...")
        }
    }

    fun isGmailAccount(email: String): Boolean = email.contains(gmailRegex)

    private fun matchesEmailRegex(value: String) = Patterns.EMAIL_ADDRESS.matcher(value).matches()

    fun passwordsAreValid(password: String?, confirmPassword: String?): Pair<Boolean, String?> {
        return when {
            password.isNullOrEmpty() -> Pair(false, "password is null or empty")
            confirmPassword.isNullOrEmpty() -> Pair(false, "confirm password is empty")
            password.trim().length < PASSWORD_MIN -> Pair(false, "password too short")
            confirmPassword.trim().length < PASSWORD_MIN -> Pair(false, "confirm password too short")
            password == confirmPassword -> Pair(true, null)
            else -> Pair(false, "impossible state")
        }
    }

    private const val PASSWORD_MIN = 5
}
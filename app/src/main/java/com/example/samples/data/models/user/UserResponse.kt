package com.example.samples.data.models.user

class UserResponse<T: Any>(
    var result: T? = null,
    var error: UserError? = null,
    var isSuccess: Boolean = false
) {


    companion object {
        fun <T: Any> success(data: T): UserResponse<T> =
            UserResponse(result = data, isSuccess = true)

        fun error(error: UserError): UserResponse<Any> =
            UserResponse(error = error, isSuccess = false)
    }
}

class UserError(val code: Int, val message: String)
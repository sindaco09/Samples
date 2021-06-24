package com.example.samples.data.network.parser

import com.example.samples.data.models.user.UserError
import com.example.samples.data.models.user.UserResponse
import com.google.gson.Gson
import retrofit2.Converter
import retrofit2.Response

object UserResponseParser {

//    fun <T: Any>parseResponse(response: Response<T>): UserResponse<T> {
//        try {
//            UserResponse.success(convertResponse(response))
//        }
//    }
//
//    private fun <T> convertResponse(response: Response<T>): Any {
//        val code = response.code()
//        if (code != 200) {
//            val userError: UserError = Gson().fromJson(response.body())
//            return UserResponse.error(UserError(code, response.errorBody()))
//        }
//    }
}
package com.example.samples.data.network.api.user

import com.example.samples.data.network.result.user.LoginResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Dummy api class
interface UserService {

    companion object {
        private const val BASE_URL = "http://localhost:7070/"
    }

    @GET(BASE_URL + "login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LoginResult>
}
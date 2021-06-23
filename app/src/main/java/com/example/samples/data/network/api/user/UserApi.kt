package com.example.samples.data.network.api.user

import com.example.samples.data.network.result.user.LoginResult
import com.example.samples.util.mock.MockInterceptor
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserApi @Inject constructor(
    private val userService: UserService
) {

    fun login(email: String, password: String): Response<LoginResult> {
        MockInterceptor.interceptEndpoint("login","login_success.json")
        return userService.login(email, password).execute()
    }
}
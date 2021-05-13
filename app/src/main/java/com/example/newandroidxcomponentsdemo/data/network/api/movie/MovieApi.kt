package com.example.newandroidxcomponentsdemo.data.network.api.movie

import retrofit2.Retrofit
import javax.inject.Inject

class MovieApi @Inject constructor(
    private val retrofit: Retrofit
) {

//    suspend fun fetchTrendingMovies(): Result<TrendingMovieResponse> {
//        val movieService = retrofit.create(MovieService::class.java);
//        return getResponse(
//            request = { movieService.getPopularMovies() },
//            defaultErrorMessage = "Error fetching Movie list")
//
//    }
//
//    suspend fun fetchMovie(id: Int): Result<Movie> {
//        val movieService = retrofit.create(MovieService::class.java);
//        return getResponse(
//            request = { movieService.getMovie(id) },
//            defaultErrorMessage = "Error fetching Movie Description")
//    }

//    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
//        return try {
//            val result = request.invoke()
//            if (result.isSuccessful) {
//                return Result.success(result.body())
//            } else {
//                val errorResponse = ErrorUtils.parseError(result, retrofit)
//                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
//            }
//        } catch (e: Throwable) {
//            Result.error("Unknown Error", null)
//        }
//    }
}
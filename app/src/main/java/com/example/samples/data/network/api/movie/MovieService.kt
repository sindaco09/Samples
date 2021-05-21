package com.example.samples.data.network.api.movie

import com.example.samples.data.models.movie.Movie
import com.example.samples.data.network.result.movie.TrendingMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MovieService {

    @Headers("Content-Type: application/json")
    @GET("/3/trending/movie/week")
    suspend fun getPopularMovies() : Response<TrendingMovieResponse>

    @Headers("Content-Type: application/json")
    @GET("/3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int) : Response<Movie>
}
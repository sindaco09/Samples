package com.example.newandroidxcomponentsdemo.data.network.api.movie

import com.example.newandroidxcomponentsdemo.data.models.movie.Movie
import com.example.newandroidxcomponentsdemo.data.movie.TrendingMovieResponse
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
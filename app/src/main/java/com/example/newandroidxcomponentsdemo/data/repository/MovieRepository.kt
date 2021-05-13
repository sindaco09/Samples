package com.example.newandroidxcomponentsdemo.data.repository

import com.example.newandroidxcomponentsdemo.data.network.api.movie.MovieApi
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {
}
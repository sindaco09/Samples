package com.example.samples.data.repository

import com.example.samples.data.network.api.movie.MovieApi
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {
}
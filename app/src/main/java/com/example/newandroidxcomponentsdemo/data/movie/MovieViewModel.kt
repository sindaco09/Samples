package com.example.newandroidxcomponentsdemo.data.movie

import androidx.lifecycle.ViewModel
import com.example.newandroidxcomponentsdemo.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

}
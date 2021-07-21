package com.suret.moviesapp.domain.usecase

import androidx.lifecycle.LiveData
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.repository.MovieRepository

class GetAllMoviesUseCase(private val movieRepository: MovieRepository) {

    fun execute(): LiveData<List<TrendingMoviesModel>> =
        movieRepository.getAllMovies()
}
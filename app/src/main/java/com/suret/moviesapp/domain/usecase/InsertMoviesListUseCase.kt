package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.repository.MovieRepository

class InsertMoviesListUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(movieModel: List<TrendingMoviesModel>) {
        movieRepository.insertMovieList(movieModel)
    }
}
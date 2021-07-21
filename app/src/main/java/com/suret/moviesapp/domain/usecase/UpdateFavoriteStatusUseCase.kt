package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.repository.MovieRepository

class UpdateFavoriteStatusUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(movieModel: TrendingMoviesModel) {
        movieRepository.updateFavoriteStatus(movieModel)
    }
}
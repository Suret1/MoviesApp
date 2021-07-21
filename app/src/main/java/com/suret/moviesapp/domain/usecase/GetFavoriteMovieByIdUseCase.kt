package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.domain.repository.MovieRepository

class GetFavoriteMovieByIdUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(id: Int): FavoriteMovieModel = movieRepository.getFavoriteMovieById(id)
}
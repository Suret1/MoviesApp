package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.domain.repository.MovieRepository

class InsertFavoriteMovieUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(favoriteMovieModel: FavoriteMovieModel) {
        movieRepository.insertFavoriteMovie(favoriteMovieModel)
    }
}
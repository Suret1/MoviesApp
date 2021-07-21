package com.suret.moviesapp.domain.usecase

import androidx.lifecycle.LiveData
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.domain.repository.MovieRepository

class GetFavoriteMoviesUseCase(private val movieRepository: MovieRepository) {

    fun execute(): LiveData<List<FavoriteMovieModel>> =
        movieRepository.getFavoriteMovies()
}
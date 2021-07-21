package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.domain.repository.MovieRepository

class DeleteMoviesUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute() {
        movieRepository.deleteMovieTable()
    }
}
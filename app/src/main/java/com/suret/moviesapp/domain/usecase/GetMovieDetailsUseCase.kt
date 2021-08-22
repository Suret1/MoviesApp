package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.MovieDetailsModel
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetMovieDetailsUseCase(private val movieRepository: MovieRepository) {

    suspend fun execute(movieId: Int): Resource<MovieDetailsModel> =
        movieRepository.getMovieDetails(movieId)
}
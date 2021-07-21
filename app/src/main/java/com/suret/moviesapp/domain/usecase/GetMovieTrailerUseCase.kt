package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.Result
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetMovieTrailerUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: Int): Resource<List<Result>> =
        movieRepository.getMovieTrailer(movieId)

}
package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetSimilarMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend fun execute(movieId: Int, page: Int): Resource<List<TrendingMoviesModel>> =
        movieRepository.getSimilarMovie(movieId, page)
}
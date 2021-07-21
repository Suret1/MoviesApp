package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetTrendingMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend fun execute(): Resource<List<TrendingMoviesModel>> = movieRepository.getTrendingMovies()
}
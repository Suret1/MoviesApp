package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetGenreListUseCase(private val movieRepository: MovieRepository) {

    suspend fun execute(): Resource<List<GenreModel>> = movieRepository.getGenreList()
}
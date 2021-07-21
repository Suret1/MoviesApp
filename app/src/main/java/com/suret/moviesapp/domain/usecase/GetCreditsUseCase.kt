package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetCreditsUseCase(private val movieRepository: MovieRepository) {

    suspend fun execute(id: Int): Resource<List<Cast>> = movieRepository.getCredits(id)
}
package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.ActorModel
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetPersonDataUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(personId: Int): Resource<ActorModel> =
        movieRepository.getPersonData(personId)

}
package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.Filmography
import com.suret.moviesapp.data.model.FilmographyRoot
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetPersonMovieCreditsUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(personId: Int): Resource<List<Filmography>> =
        movieRepository.getPersonMovieCredits(personId)
}
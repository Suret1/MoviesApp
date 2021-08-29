package com.suret.moviesapp.domain.usecase

import com.suret.moviesapp.data.model.ReviewResult
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class GetReviewsUseCase(private val repository: MovieRepository) {
    suspend fun execute(movieId: Int): Resource<List<ReviewResult>> =
        repository.getReviews(movieId)
}
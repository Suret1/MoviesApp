package com.suret.moviesapp.data.repository.datasourceimpl

import com.suret.moviesapp.data.api.API
import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.data.repository.datasource.RemoteDataSource
import retrofit2.Response

class RemoteDataSourceImpl(
    private val api: API
) : RemoteDataSource {

    override suspend fun getTrendingMovies(apiKey: String): Response<TrendingMoviesRoot> =
        api.getTrendingMoviesAPI.getTrendingMovies(apiKey)

    override suspend fun getCredits(id: Int, apiKey: String): Response<CreditsModelRoot> =
        api.getCreditsAPI.getCredits(id, apiKey)

    override suspend fun getPersonData(id: Int, apiKey: String): Response<ActorModel> =
        api.getPersonDataAPI.getPersonData(id, apiKey)

    override suspend fun getMovieTrailer(id: Int, apiKey: String): Response<TrailerModelRoot> =
        api.getMovieTrailerAPI.getMovieTrailer(id, apiKey)

    override suspend fun getMovieDetails(id: Int, apiKey: String): Response<MovieDetailsModel> =
        api.getMovieDetailsAPI.getMovieDetails(id, apiKey)

    override suspend fun getReviews(id: Int, apiKey: String): Response<ReviewModel> =
        api.getReviewsAPI.getReviews(id, apiKey)

    override suspend fun getSimilarMovie(
        id: Int,
        page: Int,
        apiKey: String
    ): Response<TrendingMoviesRoot> =
        api.getSimilarAPI.getSimilarMovie(id, page, apiKey)

    override suspend fun getPersonMovieCredits(id: Int, apiKey: String): Response<FilmographyRoot> =
        api.getPersonMovieCreditsAPI.getPersonMovieCredits(id, apiKey)

}
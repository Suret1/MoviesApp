package com.suret.moviesapp.data.repository.datasourceimpl

import com.suret.moviesapp.data.api.*
import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.data.repository.datasource.RemoteDataSource
import retrofit2.Response

class RemoteDataSourceImpl(
    private val getTrendingMoviesAPI: GetTrendingMoviesAPI,
    private val getCreditsAPI: GetCreditsAPI,
    private val getGenreListAPI: GetGenreListAPI,
    private val getMovieTrailerAPI: GetMovieTrailerAPI,
    private val getPersonDataAPI: GetPersonDataAPI,
    private val getMovieDetailsAPI: GetMovieDetailsAPI
) : RemoteDataSource {

    override suspend fun getTrendingMovies(apiKey: String): Response<TrendingMoviesRoot> =
        getTrendingMoviesAPI.getTrendingMovies(apiKey)

    override suspend fun getGenreList(apiKey: String): Response<GenreModelRoot> =
        getGenreListAPI.getGenreList(apiKey)

    override suspend fun getCredits(id: Int, apiKey: String): Response<CreditsModelRoot> =
        getCreditsAPI.getCredits(id, apiKey)

    override suspend fun getPersonData(id: Int, apiKey: String): Response<ActorModel> =
        getPersonDataAPI.getPersonData(id, apiKey)

    override suspend fun getMovieTrailer(id: Int, apiKey: String): Response<TrailerModelRoot> =
        getMovieTrailerAPI.getMovieTrailer(id, apiKey)

    override suspend fun getMovieDetails(id: Int, apiKey: String): Response<MovieDetailsModel> =
        getMovieDetailsAPI.getMovieDetails(id, apiKey)


}
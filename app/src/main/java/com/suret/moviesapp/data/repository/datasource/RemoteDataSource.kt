package com.suret.moviesapp.data.repository.datasource

import com.suret.moviesapp.data.model.*
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteDataSource {

    suspend fun getTrendingMovies(@Query("api_key") apiKey: String): Response<TrendingMoviesRoot>

    suspend fun getGenreList(@Query("api_key") apiKey: String): Response<GenreModelRoot>

    suspend fun getCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<CreditsModelRoot>

    suspend fun getPersonData(
        @Path("personId") id: Int,
        @Query("api_key") apiKey: String
    ): Response<ActorModel>

    suspend fun getMovieTrailer(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<TrailerModelRoot>

    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieDetailsModel>

}
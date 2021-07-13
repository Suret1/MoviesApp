package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.ActorModel
import com.suret.moviesapp.data.model.CreditsModelRoot
import com.suret.moviesapp.data.model.GenreModelRoot
import com.suret.moviesapp.data.model.TrendingMoviesRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IAPI {

    @GET("/3/trending/all/week")
    suspend fun getTrendingMovies(@Query("api_key") apiKey: String): Response<TrendingMoviesRoot>

    @GET("/3/genre/movie/list")
    suspend fun getGenreList(@Query("api_key") apiKey: String): Response<GenreModelRoot>

    @GET("/3/movie/{id}/credits")
    suspend fun getCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<CreditsModelRoot>

    @GET("/3/person/{personId}")
    suspend fun getPersonData(
        @Path("personId") id: Int,
        @Query("api_key") apiKey: String
    ): Response<ActorModel>

}
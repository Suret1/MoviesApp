package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.TrendingMoviesRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetSimilarAPI {

    @GET("/3/movie/{id}/similar")
    suspend fun getSimilarMovie(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Response<TrendingMoviesRoot>

}
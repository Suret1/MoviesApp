package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.TrendingMoviesRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetTrendingMoviesAPI {

    @GET("/3/trending/movie/week")
    suspend fun getTrendingMovies(@Query("api_key") apiKey: String): Response<TrendingMoviesRoot>

}
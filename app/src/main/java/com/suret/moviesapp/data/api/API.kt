package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.GenreModelRoot
import com.suret.moviesapp.data.model.TrendingMoviesRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("/3/trending/all/week")
    suspend fun getTrendingMovies(@Query("api_key") apiKey: String): Response<TrendingMoviesRoot>

    @GET("/3/genre/movie/list")
    suspend fun getGenreList(@Query("api_key") apiKey: String): Response<GenreModelRoot>
}
package com.suret.moviesapp.data.domain

import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.Resource

interface MovieRepository {

    suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>>
}
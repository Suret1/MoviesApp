package com.suret.moviesapp.data.domain

import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.GenreModelRoot
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.Resource

interface MovieRepository {

    suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>>

    suspend fun getGenreList(): Resource<List<GenreModel>>
}
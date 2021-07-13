package com.suret.moviesapp.data.domain

import com.suret.moviesapp.data.model.ActorModel
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.Resource
import retrofit2.http.Path

interface MovieRepository {

    suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>>

    suspend fun getGenreList(): Resource<List<GenreModel>>

    suspend fun getCredits(movieId: Int): Resource<List<Cast>>

    suspend fun getPersonData(personId: Int): Resource<ActorModel>
}
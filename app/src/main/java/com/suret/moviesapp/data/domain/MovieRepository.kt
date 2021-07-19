package com.suret.moviesapp.data.domain

import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.util.Resource

interface MovieRepository {

    suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>>

    suspend fun getGenreList(): Resource<List<GenreModel>>

    suspend fun getCredits(movieId: Int): Resource<List<Cast>>

    suspend fun getPersonData(personId: Int): Resource<ActorModel>

    suspend fun getMovieTrailer(movieId: Int): Resource<List<Result>>
}
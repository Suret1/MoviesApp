package com.suret.moviesapp.domain.repository

import androidx.lifecycle.LiveData
import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.util.Resource

interface MovieRepository {

    suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>>

    fun getAllMovies(): LiveData<List<TrendingMoviesModel>>

    fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>>

    suspend fun deleteMovieTable()

    suspend fun updateFavoriteStatus(movieModel: TrendingMoviesModel)

    suspend fun insertMovieList(movieModel: List<TrendingMoviesModel>)

    suspend fun getGenreList(): Resource<List<GenreModel>>

    suspend fun getCredits(movieId: Int): Resource<List<Cast>>

    suspend fun getPersonData(personId: Int): Resource<ActorModel>

    suspend fun getMovieTrailer(movieId: Int): Resource<List<Result>>

    suspend fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)

    suspend fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)

    suspend fun getFavoriteMovieById(id: Int): FavoriteMovieModel


}
package com.suret.moviesapp.domain.repository

import androidx.lifecycle.LiveData
import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.util.Resource

interface MovieRepository {

    suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>>

    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetailsModel>

    suspend fun getPersonMovieCredits(id: Int): Resource<List<Filmography>>

    fun getAllMovies(): LiveData<List<TrendingMoviesModel>>

    fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>>

    suspend fun deleteMovieTable()

    suspend fun updateFavoriteStatus(movieModel: TrendingMoviesModel)

    suspend fun insertMovieList(movieModel: List<TrendingMoviesModel>)

    suspend fun getCredits(movieId: Int): Resource<List<Cast>>

    suspend fun getPersonData(personId: Int): Resource<ActorModel>

    suspend fun getMovieTrailer(movieId: Int): Resource<List<Result>>

    suspend fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)

    suspend fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)

    suspend fun getFavoriteMovieById(id: Int): FavoriteMovieModel

    suspend fun getReviews(movieId: Int): Resource<List<ReviewResult>>

    suspend fun getSimilarMovie(movieId: Int, page: Int): Resource<List<TrendingMoviesModel>>
}
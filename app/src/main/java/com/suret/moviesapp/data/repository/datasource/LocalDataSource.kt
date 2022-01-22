package com.suret.moviesapp.data.repository.datasource

import androidx.lifecycle.LiveData
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel

interface LocalDataSource {

    fun getAllMovies(): LiveData<List<TrendingMoviesModel>>
    fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>>
    suspend fun deleteMovieTable()
    suspend fun insertMovieList(movieModel: List<TrendingMoviesModel>)
    suspend fun updateFavoriteStatus(movieModel: TrendingMoviesModel)
    suspend fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)
    suspend fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)
    suspend fun getFavoriteMovieById(id: Int): FavoriteMovieModel

}
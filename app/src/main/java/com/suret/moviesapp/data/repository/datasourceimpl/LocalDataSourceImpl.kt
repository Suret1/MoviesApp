package com.suret.moviesapp.data.repository.datasourceimpl

import androidx.lifecycle.LiveData
import com.suret.moviesapp.data.db.MovieDao
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.repository.datasource.LocalDataSource

class LocalDataSourceImpl (private val movieDao: MovieDao) : LocalDataSource {

    override fun getAllMovies(): LiveData<List<TrendingMoviesModel>> =
        movieDao.getAllMovies()


    override fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>> =
        movieDao.getFavoriteMovies()

    override suspend fun deleteMovieTable() {
        movieDao.deleteMovieTable()
    }

    override suspend fun insertMovieList(movieModel: List<TrendingMoviesModel>) {
        movieDao.insertMovieList(movieModel)
    }

    override suspend fun updateFavoriteStatus(movieModel: TrendingMoviesModel) {
        movieDao.updateFavoriteStatus(movieModel)
    }

    override suspend fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) {
        movieDao.insertFavoriteMovie(favoriteMovieModel)
    }

    override suspend fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) {
        movieDao.removeFavoriteMovie(favoriteMovieModel)
    }

    override suspend fun getFavoriteMovieById(id: Int): FavoriteMovieModel =
        movieDao.getFavoriteMovieById(id)


}
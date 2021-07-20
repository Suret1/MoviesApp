package com.suret.moviesapp.data.domain

import androidx.lifecycle.LiveData
import com.suret.moviesapp.BuildConfig
import com.suret.moviesapp.data.api.IAPI
import com.suret.moviesapp.data.db.MovieDao
import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.util.Resource

class MovieRepositoryImpl(
    private val api: IAPI,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>> {
        val response = api.getTrendingMovies(BuildConfig.API_KEY)
        val result = response.body()
        result?.let {
            return if (response.isSuccessful) {
                Resource.Success(it.results!!)
            } else {
                Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())

    }

    override fun getAllMovies(): LiveData<List<TrendingMoviesModel>> = movieDao.getAllMovies()

    override fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>> =
        movieDao.getFavoriteMovies()

    override suspend fun deleteMovieTable() {
        movieDao.deleteMovieTable()
    }

    override suspend fun updateFavoriteStatus(movieModel: TrendingMoviesModel) {
        movieDao.updateFavoriteStatus(movieModel)
    }


    override suspend fun insertMovieList(movieModel: List<TrendingMoviesModel>) {
        movieDao.insertMovieList(movieModel)
    }

    override suspend fun getGenreList(): Resource<List<GenreModel>> {

        val response = api.getGenreList(BuildConfig.API_KEY)
        val result = response.body()
        result?.let {
            return if (response.isSuccessful) {
                Resource.Success(it.genres!!)
            } else {
                Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getCredits(movieId: Int): Resource<List<Cast>> {
        val response = api.getCredits(movieId, BuildConfig.API_KEY)
        val result = response.body()
        result?.let {
            return if (response.isSuccessful) {
                Resource.Success(it.cast!!)
            } else {
                Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getPersonData(personId: Int): Resource<ActorModel> {
        val response = api.getPersonData(personId, BuildConfig.API_KEY)
        val result = response.body()
        result?.let {
            return if (response.isSuccessful) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getMovieTrailer(movieId: Int): Resource<List<Result>> {
        val response = api.getMovieTrailer(movieId, BuildConfig.API_KEY)
        val result = response.body()
        result?.let {
            return if (response.isSuccessful) {
                Resource.Success(it.results!!)
            } else {
                Resource.Error(response.message())
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) {
        movieDao.insertFavoriteMovie(favoriteMovieModel)
    }

    override suspend fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) {
        movieDao.removeFavoriteMovie(favoriteMovieModel)
    }

    override suspend fun getFavoriteMovieById(id: Int): FavoriteMovieModel {
        return movieDao.getFavoriteMovieById(id)
    }
}
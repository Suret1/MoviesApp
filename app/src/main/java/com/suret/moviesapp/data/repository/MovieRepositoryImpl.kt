package com.suret.moviesapp.data.repository

import androidx.lifecycle.LiveData
import com.suret.moviesapp.BuildConfig
import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.data.repository.datasource.LocalDataSource
import com.suret.moviesapp.data.repository.datasource.RemoteDataSource
import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.util.Resource

class MovieRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : MovieRepository {

    override suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>> {
        val response = remoteDataSource.getTrendingMovies(BuildConfig.API_KEY)
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

    override fun getAllMovies(): LiveData<List<TrendingMoviesModel>> =
        localDataSource.getAllMovies()

    override fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>> =
        localDataSource.getFavoriteMovies()

    override suspend fun deleteMovieTable() {
        localDataSource.deleteMovieTable()
    }

    override suspend fun updateFavoriteStatus(movieModel: TrendingMoviesModel) {
        localDataSource.updateFavoriteStatus(movieModel)
    }


    override suspend fun insertMovieList(movieModel: List<TrendingMoviesModel>) {
        localDataSource.insertMovieList(movieModel)
    }

    override suspend fun getGenreList(): Resource<List<GenreModel>> {

        val response = remoteDataSource.getGenreList(BuildConfig.API_KEY)
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
        val response = remoteDataSource.getCredits(movieId, BuildConfig.API_KEY)
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
        val response = remoteDataSource.getPersonData(personId, BuildConfig.API_KEY)
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
        val response = remoteDataSource.getMovieTrailer(movieId, BuildConfig.API_KEY)
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
        localDataSource.insertFavoriteMovie(favoriteMovieModel)
    }

    override suspend fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) {
        localDataSource.removeFavoriteMovie(favoriteMovieModel)
    }

    override suspend fun getFavoriteMovieById(id: Int): FavoriteMovieModel {
        return localDataSource.getFavoriteMovieById(id)
    }
}
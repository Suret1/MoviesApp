package com.suret.moviesapp.data.domain

import com.suret.moviesapp.BuildConfig
import com.suret.moviesapp.data.api.IAPI
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.Resource

class MovieRepositoryImpl(
    private val api: IAPI
) : MovieRepository {

    override suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>> {
        val response = api.getTrendingMovies(BuildConfig.API_KEY)
        val result = response.body()
        return if (response.isSuccessful) {
            Resource.Success(result?.results!!)
        } else {
            Resource.Error(response.message())
        }
    }

    override suspend fun getGenreList(): Resource<List<GenreModel>> {

        val response = api.getGenreList(BuildConfig.API_KEY)
        val result = response.body()
        result?.let {
            return if (response.isSuccessful) {
                Resource.Success(result.genres!!)
            } else {
                Resource.Error(response.message())
            }
        }
        return Resource.Error("unknown error occurred")
    }

}
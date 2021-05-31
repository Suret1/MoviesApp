package com.suret.moviesapp.data.domain

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.suret.moviesapp.data.api.API
import com.suret.moviesapp.data.db.MovieDao
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.Resource

class MovieRepositoryImpl(
    private val activity: Activity,
    private val dao: MovieDao,
    private val api: API
) : MovieRepository {

    override suspend fun getTrendingMovies(): Resource<List<TrendingMoviesModel>> {

        if (isNetworkAvailable(activity)) {
            val response = api.getTrendingMovies("1f05bf0419f372e983c4708603be70b4")
            val result = response.body()

            result?.let {
                return if (response.isSuccessful) {
                    it.results?.let { movieList ->
                        dao.insertMovie(movieList)
                    }
                    Resource.Success(result.results!!)
                } else {
                    Resource.Error(response.message())
                }
            }
        } else {
            val result = dao.getAllMovies()

            return Resource.Success(result)
        }
        return Resource.Error("unknown error occurred")
    }

    override suspend fun getGenreList(): Resource<List<GenreModel>> {
        if (isNetworkAvailable(activity)) {
            val response = api.getGenreList("1f05bf0419f372e983c4708603be70b4")
            val result = response.body()
            result?.let {
                return if (response.isSuccessful) {
                    Resource.Success(result.genres!!)
                } else {
                    Resource.Error(response.message())
                }
            }
        } else {
            return Resource.Error("unknown error occurred")
        }
        return Resource.Error("unknown error occurred")
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        // For below 29 api
        else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

}
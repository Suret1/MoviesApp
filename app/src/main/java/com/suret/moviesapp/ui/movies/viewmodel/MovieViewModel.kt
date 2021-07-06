package com.suret.moviesapp.ui.movies.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.db.MovieDao
import com.suret.moviesapp.data.domain.MovieRepository
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: MovieRepository,
    private val movieDao: MovieDao
) : ViewModel() {

    sealed class Event {
        class Success(
            val result: List<TrendingMoviesModel>?,
            val genreModel: List<GenreModel>?
        ) : Event()

        class Failure(
            val localData: List<TrendingMoviesModel>?,
            val errorText: String
        ) : Event()

        object Loading : Event()

    }

    private val trendingMoviesChannel = Channel<Event>()
    val trendingMoviesFlow = trendingMoviesChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            trendingMoviesChannel.send(
                Event.Failure(
                    movieDao.getAllMovies(),
                    ""
                )
            )
        }
    }

    fun getTrendingMovies() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        if (isNetworkAvailable(context = context)) {
            trendingMoviesChannel.send(Event.Loading)
            when (val response = repository.getTrendingMovies()) {
                is Resource.Success -> {
                    response.data?.let {
                        trendingMoviesChannel.send(Event.Success(response.data, null))
                        movieDao.insertMovie(response.data)
                    } ?: kotlin.run {
                        trendingMoviesChannel.send(
                            Event.Failure(
                                movieDao.getAllMovies(),
                                response.message ?: ""
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    trendingMoviesChannel.send(
                        Event.Failure(
                            movieDao.getAllMovies(),
                            response.message ?: ""
                        )
                    )
                }
            }
        }

    }

    fun getGenreList() = viewModelScope.launch(Dispatchers.IO) {
        if(isNetworkAvailable(context)){
            trendingMoviesChannel.send(Event.Loading)
            when (val response = repository.getGenreList()) {
                is Resource.Success -> {
                    response.data?.let {
                        trendingMoviesChannel.send(Event.Success(null, response.data))
                    } ?: kotlin.run {
                        trendingMoviesChannel.send(Event.Failure(null,response.message ?: ""))
                    }
                }
                is Resource.Error -> {
                    trendingMoviesChannel.send(Event.Failure(null,response.message ?: ""))
                }
            }
        }

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
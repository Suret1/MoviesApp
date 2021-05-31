package com.suret.moviesapp.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.domain.MovieRepository
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    sealed class Event {
        class Success(
            val result: List<TrendingMoviesModel>?,
            val genreModel: List<GenreModel>?
        ) : Event()

        class Failure(val errorText: String) : Event()
        object Loading : Event()

    }

    private val trendingMoviesChannel = Channel<Event>()
    val trendingMoviesFlow = trendingMoviesChannel.receiveAsFlow()

    fun getTrendingMovies() = viewModelScope.launch(Dispatchers.IO) {
        trendingMoviesChannel.send(Event.Loading)

        when (val response = repository.getTrendingMovies()) {
            is Resource.Success -> {
                response.data?.let {
                    trendingMoviesChannel.send(Event.Success(response.data, null))
                } ?: kotlin.run {
                    trendingMoviesChannel.send(Event.Failure(response.message ?: ""))
                }
            }
            is Resource.Error -> {
                trendingMoviesChannel.send(Event.Failure(response.message ?: ""))
            }
        }
    }

    fun getGenreList() = viewModelScope.launch(Dispatchers.IO) {
        trendingMoviesChannel.send(Event.Loading)

        when (val response = repository.getGenreList()) {
            is Resource.Success -> {
                response.data?.let {
                    trendingMoviesChannel.send(Event.Success(null, response.data))
                } ?: kotlin.run {
                    trendingMoviesChannel.send(Event.Failure(response.message ?: ""))
                }
            }
            is Resource.Error -> {
                trendingMoviesChannel.send(Event.Failure(response.message ?: ""))
            }
        }
    }


}
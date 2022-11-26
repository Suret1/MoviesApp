package com.suret.moviesapp.ui.movies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.usecase.UseCases
import com.suret.moviesapp.util.Resource
import com.suret.moviesapp.util.Util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MoviesVM @Inject constructor(
    private val app: Application,
    private val useCases: UseCases
) : AndroidViewModel(app) {

    private val _listTrendingMovies = MutableStateFlow<List<TrendingMoviesModel>>(emptyList())
    val listTrendingMovies = _listTrendingMovies.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _errorMessage.emit(app.getString(R.string.unknown_error))
            _isLoading.emit(false)
        }
    }

    fun getTrendingMovies() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        if (isNetworkAvailable(app)) {
            _isLoading.emit(true)
            when (val response = useCases.getTrendingMoviesUseCase.execute()) {
                is Resource.Success -> {
                    response.data?.let {
                        it.map { model ->
                            model.id?.let { id ->
                                val favModel = useCases.getFavoriteMovieByIdUseCase.execute(id)
                                model.isFavorite = favModel != null
                            }
                        }
                        useCases.deleteMoviesUseCase.execute()
                        _listTrendingMovies.emit(it)
                        useCases.insertMoviesListUseCase.execute(it)
                        _isLoading.emit(false)
                    }
                }
                is Resource.Error -> {
                    _isLoading.emit(false)
                    response.message?.let { _errorMessage.emit(it) }
                }
            }
        } else {
            _isLoading.emit(false)
            _errorMessage.emit(app.getString(R.string.offline_message))
        }
    }

    fun updateMovieModel(movieModel: TrendingMoviesModel) =
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateFavoriteStatusUseCase.execute(movieModel)
        }

    fun localList(): LiveData<List<TrendingMoviesModel>> = useCases.getAllMoviesUseCase.execute()

    fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            useCases.insertFavoriteMovieUseCase.execute(favoriteMovieModel)
        }

    fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            useCases.remoteFavoriteMovieUseCase.execute(favoriteMovieModel)
        }

    fun createFavoriteModel(movie: TrendingMoviesModel): FavoriteMovieModel {
        return FavoriteMovieModel(
            movie.backdrop_path,
            movie.first_air_date,
            movie.genre_ids,
            movie.id,
            movie.name,
            movie.original_language,
            movie.original_name,
            movie.original_title,
            movie.overview,
            movie.popularity,
            movie.poster_path,
            movie.release_date,
            movie.title,
            movie.vote_average,
            movie.vote_count,
            movie.isFavorite
        )
    }

    fun setFavoriteStatus(
        movie: TrendingMoviesModel,
        isFavorite: Boolean
    ): TrendingMoviesModel {
        if (isFavorite) {
            return TrendingMoviesModel(
                movie.backdrop_path,
                movie.first_air_date,
                movie.genre_ids,
                movie.id,
                movie.name,
                movie.original_language,
                movie.original_name,
                movie.original_title,
                movie.overview,
                movie.popularity,
                movie.poster_path,
                movie.release_date,
                movie.title,
                movie.vote_average,
                movie.vote_count,
                false
            )
        } else {
            return TrendingMoviesModel(
                movie.backdrop_path,
                movie.first_air_date,
                movie.genre_ids,
                movie.id,
                movie.name,
                movie.original_language,
                movie.original_name,
                movie.original_title,
                movie.overview,
                movie.popularity,
                movie.poster_path,
                movie.release_date,
                movie.title,
                movie.vote_average,
                movie.vote_count,
                true
            )
        }
    }
}
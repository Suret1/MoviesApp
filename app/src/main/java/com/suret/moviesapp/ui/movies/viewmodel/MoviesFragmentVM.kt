package com.suret.moviesapp.ui.movies.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.usecase.*
import com.suret.moviesapp.util.Resource
import com.suret.moviesapp.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesFragmentVM @Inject constructor(
    @ApplicationContext val context: Context,
    private val deleteMoviesUseCase: DeleteMoviesUseCase,
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val getFavoriteMovieByIdUseCase: GetFavoriteMovieByIdUseCase,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase,
    private val insertMoviesListUseCase: InsertMoviesListUseCase,
    private val remoteFavoriteMovieUseCase: RemoteFavoriteMovieUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
) : ViewModel() {

    sealed class Event {

        class TrendingSuccess(
            val trendingMoviesModel: List<TrendingMoviesModel>?,
        ) : Event()

        class Failure(
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
                    "Unknown error occurred"
                )
            )
        }
    }

    fun getTrendingMovies() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        if (isNetworkAvailable(context)) {
            trendingMoviesChannel.send(Event.Loading)
            when (val response = getTrendingMoviesUseCase.execute()) {
                is Resource.Success -> {
                    response.data?.let {
                        it.map { model ->
                            model.id?.let { id ->
                                val favModel = getFavoriteMovieByIdUseCase.execute(id)
                                model.isFavorite = favModel != null
                            }
                        }
                        deleteMoviesUseCase.execute()
                        trendingMoviesChannel.send(Event.TrendingSuccess(it))
                        insertMoviesListUseCase.execute(it)
                    } ?: kotlin.run {
                        trendingMoviesChannel.send(
                            Event.Failure(
                                response.message ?: ""
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    trendingMoviesChannel.send(
                        Event.Failure(
                            response.message ?: ""
                        )
                    )
                }
            }
        } else {
            trendingMoviesChannel.send(
                Event.Failure(
                    "No Internet"
                )
            )
        }

    }

    fun updateMovieModel(movieModel: TrendingMoviesModel) =
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase.execute(movieModel)
        }

    fun getMovieList(): LiveData<List<TrendingMoviesModel>> = getAllMoviesUseCase.execute()


    fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            insertFavoriteMovieUseCase.execute(favoriteMovieModel)
        }

    fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            remoteFavoriteMovieUseCase.execute(favoriteMovieModel)
        }
}
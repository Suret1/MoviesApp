package com.suret.moviesapp.ui.movies.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.model.*
import com.suret.moviesapp.domain.usecase.*
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
    private val deleteMoviesUseCase: DeleteMoviesUseCase,
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val getCreditsUseCase: GetCreditsUseCase,
    private val getFavoriteMovieByIdUseCase: GetFavoriteMovieByIdUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val getGenreListUseCase: GetGenreListUseCase,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    private val getPersonDataUseCase: GetPersonDataUseCase,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase,
    private val insertMoviesListUseCase: InsertMoviesListUseCase,
    private val remoteFavoriteMovieUseCase: RemoteFavoriteMovieUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
) : ViewModel() {

    sealed class Event {
        class TrendingSuccess(
            val trendingMoviesModel: List<TrendingMoviesModel>?,

            ) : Event()

        class GenreSuccess(
            val genreModel: List<GenreModel>?,
        ) : Event()

        class CastSuccess(
            val cast: List<Cast>?
        ) : Event()

        class ActorSuccess(
            val actor: ActorModel?
        ) : Event()

        class TrailerSuccess(
            val trailerList: List<Result>?
        ) : Event()

        class Failure(
            val localData: LiveData<List<TrendingMoviesModel>>?,
            val errorText: String
        ) : Event()

        object Loading : Event()

    }


    private val trendingMoviesChannel = Channel<Event>()
    val trendingMoviesFlow = trendingMoviesChannel.receiveAsFlow()

    private val genreChannel = Channel<Event>()
    val genreFlow = genreChannel.receiveAsFlow()

    private val castChannel = Channel<Event>()
    val castFlow = castChannel.receiveAsFlow()

    private val actorChannel = Channel<Event>()
    val actorFlow = actorChannel.receiveAsFlow()

    private val trailerChannel = Channel<Event>()
    val trailerFlow = trailerChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            trendingMoviesChannel.send(
                Event.Failure(
                    getAllMoviesUseCase.execute(),
                    ""
                )
            )
        }
    }

    fun getTrendingMovies() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        if (isNetworkAvailable(context = context)) {
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
                                getAllMoviesUseCase.execute(),
                                response.message ?: ""
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    trendingMoviesChannel.send(
                        Event.Failure(
                            getAllMoviesUseCase.execute(),
                            response.message ?: ""
                        )
                    )
                }
            }
        } else {
            trendingMoviesChannel.send(
                Event.Failure(
                    getAllMoviesUseCase.execute(),
                    "No Internet" ?: ""
                )
            )
        }

    }

    fun getGenreList() = viewModelScope.launch(Dispatchers.IO) {
        if (isNetworkAvailable(context)) {
            genreChannel.send(Event.Loading)
            when (val response = getGenreListUseCase.execute()) {
                is Resource.Success -> {
                    response.data?.let {
                        genreChannel.send(Event.GenreSuccess(it))
                    } ?: kotlin.run {
                        genreChannel.send(Event.Failure(null, response.message ?: ""))
                    }
                }
                is Resource.Error -> {
                    genreChannel.send(Event.Failure(null, response.message ?: ""))
                }
            }
        }

    }

    fun getCredits(idMovie: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (isNetworkAvailable(context)) {
            castChannel.send(Event.Loading)
            when (val response = getCreditsUseCase.execute(idMovie)) {
                is Resource.Success -> {
                    response.data?.let {
                        castChannel.send(Event.CastSuccess(it))
                    } ?: kotlin.run {
                        castChannel.send(Event.Failure(null, response.message ?: ""))
                    }
                }
                is Resource.Error -> {
                    castChannel.send(Event.Failure(null, response.message ?: ""))
                }
            }
        }

    }

    fun getPersonData(personId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (isNetworkAvailable(context)) {
            actorChannel.send(Event.Loading)
            when (val response = getPersonDataUseCase.execute(personId)) {
                is Resource.Success -> {
                    response.data?.let {
                        actorChannel.send(Event.ActorSuccess(it))
                    } ?: kotlin.run {
                        actorChannel.send(Event.Failure(null, response.message ?: ""))
                    }
                }
                is Resource.Error -> {
                    actorChannel.send(Event.Failure(null, response.message ?: ""))
                }
            }
        }
    }

    fun getMovieTrailer(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (isNetworkAvailable(context)) {
            trailerChannel.send(Event.Loading)
            when (val response = getMovieTrailerUseCase.execute(movieId)) {
                is Resource.Success -> {
                    response.data?.let {
                        trailerChannel.send(Event.TrailerSuccess(it))
                    } ?: kotlin.run {
                        trailerChannel.send(Event.Failure(null, response.message ?: ""))
                    }
                }
                is Resource.Error -> {
                    actorChannel.send(Event.Failure(null, response.message ?: ""))
                }
            }
        }
    }

    fun updateMovieModel(movieModel: TrendingMoviesModel) =
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase.execute(movieModel)
        }

    fun getMovieList(): LiveData<List<TrendingMoviesModel>> = getAllMoviesUseCase.execute()

    fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>> = getFavoriteMoviesUseCase.execute()

    fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) = viewModelScope.launch {
        insertFavoriteMovieUseCase.execute(favoriteMovieModel)
    }

    fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) = viewModelScope.launch {
        remoteFavoriteMovieUseCase.execute(favoriteMovieModel)
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
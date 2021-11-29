package com.suret.moviesapp.ui.moviedetails.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.model.*
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
class MovieDetailsFragmentVM @Inject constructor(
    @ApplicationContext val context: Context,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getCreditsUseCase: GetCreditsUseCase,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val remoteFavoriteMovieUseCase: RemoteFavoriteMovieUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase,
    private val getFavoriteMovieByIdUseCase: GetFavoriteMovieByIdUseCase,
) : ViewModel() {


    sealed class Event {

        class DetailsSuccess(
            val details: MovieDetailsModel?
        ) : Event()

        class CastSuccess(
            val cast: List<Cast>?
        ) : Event()

        class TrailerSuccess(
            val trailerList: List<Result>?
        ) : Event()

        class ReviewsSuccess(
            val reviews: List<ReviewResult>?
        ) : Event()

        class Failure(
            val errorText: String
        ) : Event()

        object Loading : Event()
    }

    private val detailsChannel = Channel<Event>()
    val detailsFlow = detailsChannel.receiveAsFlow()

    private val castChannel = Channel<Event>()
    val castFlow = castChannel.receiveAsFlow()

    private val trailerChannel = Channel<Event>()
    val trailerFlow = trailerChannel.receiveAsFlow()

    private val reviewChannel = Channel<Event>()
    val reviewFlow = reviewChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            detailsChannel.send(
                Event.Failure(
                    "Request Timed Out"
                )
            )
        }
    }

    fun getMovieDetails(movieId: Int) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (isNetworkAvailable(context)) {
                when (val response = getMovieDetailsUseCase.execute(movieId)) {
                    is Resource.Success -> {
                        response.data?.let {
                            detailsChannel.send(Event.DetailsSuccess(it))
                        } ?: kotlin.run {
                            detailsChannel.send(
                                Event.Failure(
                                    response.message ?: ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        detailsChannel.send(
                            Event.Failure(
                                response.message ?: ""
                            )
                        )
                    }
                }
            } else {
                detailsChannel.send(
                    Event.Failure("Unknown error")
                )
            }
        }

    fun getCredits(idMovie: Int) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (isNetworkAvailable(context)) {
                when (val response = getCreditsUseCase.execute(idMovie)) {
                    is Resource.Success -> {
                        response.data?.let {
                            castChannel.send(Event.CastSuccess(it))
                        } ?: kotlin.run {
                            castChannel.send(
                                Event.Failure(
                                    response.message ?: ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        castChannel.send(
                            Event.Failure(
                                response.message ?: ""
                            )
                        )
                    }
                }
            } else {
                castChannel.send(
                    Event.Failure(
                        "No Internet"
                    )
                )
            }

        }

    fun getMovieTrailer(movieId: Int) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (isNetworkAvailable(context)) {
                trailerChannel.send(Event.Loading)
                when (val response = getMovieTrailerUseCase.execute(movieId)) {
                    is Resource.Success -> {
                        response.data?.let {
                            trailerChannel.send(Event.TrailerSuccess(it))
                        } ?: kotlin.run {
                            trailerChannel.send(Event.Failure(response.message ?: ""))
                        }
                    }
                    is Resource.Error -> {
                        trailerChannel.send(Event.Failure(response.message ?: ""))
                    }
                }
            } else {
                trailerChannel.send(
                    Event.Failure(
                        "No Internet"
                    )
                )
            }
        }

    fun getReviews(movieId: Int) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (isNetworkAvailable(context)) {
                when (val response = getReviewsUseCase.execute(movieId)) {
                    is Resource.Success -> {
                        response.data.let {
                            reviewChannel.send(Event.ReviewsSuccess(it))
                        }
                    }
                    is Resource.Error -> {
                        reviewChannel.send(
                            Event.Failure(
                                response.message ?: ""
                            )
                        )
                    }

                }
            } else {
                reviewChannel.send(
                    Event.Failure(
                        "No Internet"
                    )
                )
            }

        }

    fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            insertFavoriteMovieUseCase.execute(favoriteMovieModel)
        }

    fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            remoteFavoriteMovieUseCase.execute(favoriteMovieModel)
        }

    fun updateMovieModel(movieModel: TrendingMoviesModel) =
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase.execute(movieModel)
        }

    suspend fun getFavoriteMovieByID(movieId: Int): FavoriteMovieModel =
        getFavoriteMovieByIdUseCase.execute(movieId)

}
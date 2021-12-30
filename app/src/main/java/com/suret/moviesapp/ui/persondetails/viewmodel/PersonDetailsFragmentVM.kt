package com.suret.moviesapp.ui.persondetails.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.model.ActorModel
import com.suret.moviesapp.data.model.Filmography
import com.suret.moviesapp.domain.usecase.GetPersonDataUseCase
import com.suret.moviesapp.domain.usecase.GetPersonMovieCreditsUseCase
import com.suret.moviesapp.util.Resource
import com.suret.moviesapp.util.Util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsFragmentVM @Inject constructor(
    @ApplicationContext val context: Context,
    private val getPersonDataUseCase: GetPersonDataUseCase,
    private val getPersonMovieCreditsUseCase: GetPersonMovieCreditsUseCase
) : ViewModel() {

    sealed class Event {

        class ActorSuccess(
            val actor: ActorModel?
        ) : Event()

        class FilmographySuccess(
            val movie: List<Filmography>?
        ) : Event()

        class Failure(
            val errorText: String
        ) : Event()

        object Loading : Event()

    }

    private val actorChannel = Channel<Event>()
    val actorFlow = actorChannel.receiveAsFlow()

    private val movieChannel = Channel<Event>()
    val movieFlow = movieChannel.receiveAsFlow()


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            actorChannel.send(
                Event.Failure(
                    "Unknown error occurred"
                )
            )
        }
    }

    fun getPersonData(personId: Int) =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (isNetworkAvailable(context)) {
                actorChannel.send(Event.Loading)
                when (val response = getPersonDataUseCase.execute(personId)) {
                    is Resource.Success -> {
                        response.data?.let {
                            actorChannel.send(Event.ActorSuccess(it))
                        } ?: kotlin.run {
                            actorChannel.send(
                                Event.Failure(
                                    response.message ?: ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        actorChannel.send(Event.Failure(response.message ?: ""))
                    }
                }
            } else {
                actorChannel.send(
                    Event.Failure(
                        "No Internet"
                    )
                )
            }
        }

    fun getPersonMovieCredits(personId: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (isNetworkAvailable(context)) {
                movieChannel.send(Event.Loading)
                when (val response = getPersonMovieCreditsUseCase.execute(personId)) {
                    is Resource.Success -> {
                        response.data?.let {
                            movieChannel.send(Event.FilmographySuccess(it))
                        }
                    }
                    is Resource.Error -> {
                        movieChannel.send(Event.Failure(response.message ?: ""))
                    }
                }
            } else {
                movieChannel.send(
                    Event.Failure(
                        "No Internet"
                    )
                )
            }
        }
    }
}
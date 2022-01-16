package com.suret.moviesapp.ui.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesVM @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>> =
        useCases.getFavoriteMoviesUseCase.execute()

    fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            useCases.remoteFavoriteMovieUseCase.execute(favoriteMovieModel)
        }

    fun updateMovieModel(movieModel: TrendingMoviesModel) =
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateFavoriteStatusUseCase.execute(movieModel)
        }
}
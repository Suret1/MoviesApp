package com.suret.moviesapp.ui.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.usecase.GetFavoriteMoviesUseCase
import com.suret.moviesapp.domain.usecase.RemoteFavoriteMovieUseCase
import com.suret.moviesapp.domain.usecase.UpdateFavoriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesFragmentVM @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val remoteFavoriteMovieUseCase: RemoteFavoriteMovieUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
) : ViewModel() {

    fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>> = getFavoriteMoviesUseCase.execute()

    fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel) =
        viewModelScope.launch(Dispatchers.IO) {
            remoteFavoriteMovieUseCase.execute(favoriteMovieModel)
        }

    fun updateMovieModel(movieModel: TrendingMoviesModel) =
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase.execute(movieModel)
        }
}
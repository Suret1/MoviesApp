package com.suret.moviesapp.ui.similar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.suret.moviesapp.data.repository.datasourceimpl.SimilarMoviesPagingDataSource
import com.suret.moviesapp.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SimilarVM @Inject constructor(
    useCases: UseCases
) :
    ViewModel() {

    val listData = Pager(
        PagingConfig(pageSize = 500, enablePlaceholders = false),
        pagingSourceFactory = { SimilarMoviesPagingDataSource(useCases.getSimilarMoviesUseCase) }
    ).flow
        .cachedIn(viewModelScope)

}
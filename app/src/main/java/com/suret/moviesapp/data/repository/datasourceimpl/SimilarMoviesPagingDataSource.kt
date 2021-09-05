package com.suret.moviesapp.data.repository.datasourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.domain.usecase.GetSimilarMoviesUseCase
import javax.inject.Inject

class SimilarMoviesPagingDataSource @Inject constructor(private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase) :
    PagingSource<Int, TrendingMoviesModel>() {


    override fun getRefreshKey(state: PagingState<Int, TrendingMoviesModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrendingMoviesModel> {
        val position = params.key ?: STARTING_INDEX

        return try {
            val movies = getSimilarMoviesUseCase.execute(ID, position)
            LoadResult.Page(
                data = movies.data!!,
                prevKey = if (position == STARTING_INDEX) null else position - 1,
                nextKey = if (movies.data.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    companion object {
        const val STARTING_INDEX = 1
        var ID = 385128
    }
}
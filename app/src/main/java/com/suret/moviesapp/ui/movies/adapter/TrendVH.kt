package com.suret.moviesapp.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.databinding.TrendingMoviesListBinding

class TrendVH(val binding: TrendingMoviesListBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        trendModel: TrendingMoviesModel?,
        onItemClick: ((TrendingMoviesModel) -> Unit)? = null,
        onFavClick: ((TrendingMoviesModel) -> Unit)? = null
    ) {
        trendModel?.let { model ->
            binding.movie = model

            binding.root.setOnClickListener {
                onItemClick?.invoke(model)
            }
            binding.iwFavorite.setOnClickListener {
                onFavClick?.invoke(model)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup) = TrendVH(
            TrendingMoviesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

}
package com.suret.moviesapp.ui.movies.adapter

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
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
                if (model.isFavorite) {
                    binding.iwFavorite.setImageDrawable(ContextCompat.getDrawable(binding.iwFavorite.context, R.drawable.dislike_anim))
                    (binding.iwFavorite.drawable as AnimatedVectorDrawable?)?.start()
                }else{
                    binding.iwFavorite.setImageDrawable(ContextCompat.getDrawable(binding.iwFavorite.context, R.drawable.like_anim))
                    (binding.iwFavorite.drawable as AnimatedVectorDrawable?)?.start()
                }
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
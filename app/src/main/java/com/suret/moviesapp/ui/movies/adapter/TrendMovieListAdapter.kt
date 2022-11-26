package com.suret.moviesapp.ui.movies.adapter

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.TrendingMoviesModel

class TrendMovieListAdapter :
    ListAdapter<TrendingMoviesModel, TrendVH>(DifferCallBack) {

    var setOnItemClick: ((TrendingMoviesModel) -> Unit)? = null
    var setOnFavoriteClick: ((TrendingMoviesModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendVH =
        TrendVH.from(parent)

    override fun onBindViewHolder(holder: TrendVH, position: Int) {
        holder.bind(
            getItem(position),
            onItemClick = setOnItemClick,
            onFavClick = setOnFavoriteClick
        )
//        holder.itemView.animation =
//            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_animation)
    }
}

private object DifferCallBack : DiffUtil.ItemCallback<TrendingMoviesModel>() {
    override fun areItemsTheSame(
        oldItem: TrendingMoviesModel,
        newItem: TrendingMoviesModel
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TrendingMoviesModel,
        newItem: TrendingMoviesModel
    ) = oldItem == newItem

}
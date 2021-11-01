package com.suret.moviesapp.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.databinding.TrendingMoviesListBinding
import com.suret.moviesapp.ui.movies.adapter.TrendMovieListAdapter.TrendViewHolder

class TrendMovieListAdapter :
    ListAdapter<TrendingMoviesModel, TrendViewHolder>(DifferCallBack) {

    inner class TrendViewHolder(
        val binding: TrendingMoviesListBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trendingMoviesModel: TrendingMoviesModel?) {
            trendingMoviesModel?.let { model ->
                binding.apply {

                    binding.movie = currentList[bindingAdapterPosition]

                    root.setOnClickListener {
                        model.let { movies ->
                            setOnItemClick?.invoke(movies)
                        }
                    }
                    iwFavorite.setOnClickListener {
                        model.let { movies ->
                            setOnFavoriteClick?.invoke(movies)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendViewHolder {
        return TrendViewHolder(
            TrendingMoviesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrendViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_animation)
    }

    private var setOnItemClick: ((TrendingMoviesModel) -> Unit)? = null
    private var setOnFavoriteClick: ((TrendingMoviesModel) -> Unit)? = null

    fun setOnClickListener(listener: (TrendingMoviesModel) -> Unit) {
        setOnItemClick = listener
    }

    fun setOnFavoriteClickListener(listener: (TrendingMoviesModel) -> Unit) {
        setOnFavoriteClick = listener
    }
}

private object DifferCallBack : DiffUtil.ItemCallback<TrendingMoviesModel>() {
    override fun areItemsTheSame(
        oldItem: TrendingMoviesModel,
        newItem: TrendingMoviesModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TrendingMoviesModel,
        newItem: TrendingMoviesModel
    ): Boolean {
        return oldItem == newItem
    }
}
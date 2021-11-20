package com.suret.moviesapp.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.databinding.FavoriteListLayoutBinding
import com.suret.moviesapp.ui.favourite.FavoriteAdapter.FavoriteViewHolder


class FavoriteAdapter : ListAdapter<FavoriteMovieModel, FavoriteViewHolder>(DifferCallBack) {

    var setOnItemClick: ((FavoriteMovieModel) -> Unit)? = null
    var setOnFavoriteClick: ((FavoriteMovieModel) -> Unit)? = null

    inner class FavoriteViewHolder(
        private val binding: FavoriteListLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteMovieModel: FavoriteMovieModel?) {
            favoriteMovieModel?.let { model ->

                binding.model = model

                binding.root.setOnClickListener {
                    model.let { fav ->
                        setOnItemClick?.invoke(fav)
                    }
                }
                binding.iwFavorite.setOnClickListener {
                    model.let { fav ->
                        setOnFavoriteClick?.invoke(fav)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            FavoriteListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_rotate_anim)
    }

    private object DifferCallBack : DiffUtil.ItemCallback<FavoriteMovieModel>() {
        override fun areItemsTheSame(
            oldItem: FavoriteMovieModel,
            newItem: FavoriteMovieModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavoriteMovieModel,
            newItem: FavoriteMovieModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
package com.suret.moviesapp.ui.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Genre
import com.suret.moviesapp.databinding.GenreListLayoutBinding
import com.suret.moviesapp.ui.moviedetails.adapters.GenreAdapter.GenreViewHolder

class GenreAdapter : ListAdapter<Genre, GenreViewHolder>(DifferCallback) {

    inner class GenreViewHolder(private val binding: GenreListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Genre) {
            model.let {
                binding.model = it
            }
        }
    }

    private object DifferCallback : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            GenreListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_alpha_anim)
    }
}
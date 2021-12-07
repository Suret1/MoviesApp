package com.suret.moviesapp.ui.persondetails.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Filmography
import com.suret.moviesapp.databinding.FilmographyMoviesListBinding
import com.suret.moviesapp.ui.persondetails.adapter.FilmographyAdapter.FilmographyViewHolder

class FilmographyAdapter :
    ListAdapter<Filmography, FilmographyViewHolder>(DifferCallBack) {

    var setOnItemClick: ((Filmography) -> Unit)? = null

    inner class FilmographyViewHolder(
        private val binding: FilmographyMoviesListBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Filmography?) {
            model?.let { m ->

                binding.movie = m

                binding.root.setOnClickListener {
                    model.let { movies ->
                        setOnItemClick?.invoke(movies)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmographyViewHolder {
        return FilmographyViewHolder(
            FilmographyMoviesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FilmographyViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_animation)
    }
}

private object DifferCallBack : DiffUtil.ItemCallback<Filmography>() {
    override fun areItemsTheSame(
        oldItem: Filmography,
        newItem: Filmography
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Filmography,
        newItem: Filmography
    ) = oldItem == newItem

}
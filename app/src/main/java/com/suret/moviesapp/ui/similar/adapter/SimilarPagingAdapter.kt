package com.suret.moviesapp.ui.similar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.ui.similar.adapter.SimilarPagingAdapter.TrendViewHolder
import com.suret.moviesapp.util.roundForDouble
import com.willy.ratingbar.ScaleRatingBar

class SimilarPagingAdapter :
    PagingDataAdapter<TrendingMoviesModel, TrendViewHolder>(MovieDiffCallback) {

    inner class TrendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieImage: ImageView = itemView.findViewById(R.id.movie_image)
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val ratingTV: TextView = itemView.findViewById(R.id.ratingTV)
        private val ratingBar: ScaleRatingBar = itemView.findViewById(R.id.ratingBar)
        private val rootLayout: ConstraintLayout = itemView.findViewById(R.id.rootLayout)

        fun bind(trendingMoviesModel: TrendingMoviesModel?) {
            trendingMoviesModel?.let { model ->
                if (model.title == null) {
                    movieTitle.text = model.name
                    if (model.name == null) {
                        movieTitle.text = model.original_title
                    }
                } else {
                    movieTitle.text = model.title
                }
                movieImage.load(Constants.IMAGE_URL + model.poster_path)
                ratingBar.rating = model.vote_average?.toFloat() ?: 0f
                ratingTV.text = model.vote_average?.let { roundForDouble(it) }
                rootLayout.setOnClickListener {
                    model.let { movie ->
                        setOnItemClick?.invoke(movie)
                    }
                }
            }


        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<TrendingMoviesModel>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendViewHolder {
        return TrendViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.trending_movies_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrendViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_animation)
    }

    private var setOnItemClick: ((TrendingMoviesModel) -> Unit)? = null

    fun setOnClickListener(listener: (TrendingMoviesModel) -> Unit) {
        setOnItemClick = listener
    }
}



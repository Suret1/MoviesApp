package com.suret.moviesapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.databinding.TrendingMoviesListBinding

class TrendMovieListAdapter :
    RecyclerView.Adapter<TrendMovieListAdapter.TrendViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<TrendingMoviesModel>() {
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
    val differ = AsyncListDiffer(this, differCallBack)


    inner class TrendViewHolder(
        private val trendingMoviesListBinding: TrendingMoviesListBinding,
        setOnItemClickListener: ((TrendingMoviesModel) -> Unit)?
    ) :
        RecyclerView.ViewHolder(trendingMoviesListBinding.root) {

        fun bind(trendingMoviesModel: TrendingMoviesModel?) {

            if (trendingMoviesModel?.title == null) {
                trendingMoviesListBinding.movieTitle.text = trendingMoviesModel?.name
                if (trendingMoviesModel?.name == null) {
                    trendingMoviesListBinding.movieTitle.text = trendingMoviesModel?.original_title
                }
            } else {
                trendingMoviesListBinding.movieTitle.text = trendingMoviesModel.title
            }
            trendingMoviesListBinding.movieImage.load(
                "https://image.tmdb.org/t/p/original${
                    differ.currentList.getOrNull(adapterPosition)?.poster_path
                }"
            )
            trendingMoviesListBinding.ratingBar.rating =
                trendingMoviesModel?.vote_average?.toFloat() ?: 0f

            trendingMoviesListBinding.ratingTV.text = trendingMoviesModel?.vote_average?.toString()


            trendingMoviesListBinding.root.setOnClickListener {
                trendingMoviesModel?.let { movies ->
                    setOnItemClick?.invoke(movies)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val trendingMoviesListBinding =
            TrendingMoviesListBinding.inflate(layoutInflater, parent, false)
        return TrendViewHolder(trendingMoviesListBinding, setOnItemClick)

    }

    override fun onBindViewHolder(holder: TrendViewHolder, position: Int) {
        holder.bind(differ.currentList.getOrNull(position))
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var setOnItemClick: ((TrendingMoviesModel) -> Unit)? = null

    fun setOnClickListener(listener: (TrendingMoviesModel) -> Unit) {
        setOnItemClick = listener
    }


}
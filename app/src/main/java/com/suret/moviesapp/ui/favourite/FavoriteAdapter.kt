package com.suret.moviesapp.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.databinding.FavoriteListLayoutBinding
import com.suret.moviesapp.ui.favourite.FavoriteAdapter.FavoriteViewHolder

class FavoriteAdapter : RecyclerView.Adapter<FavoriteViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<FavoriteMovieModel>() {
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

    val differ = AsyncListDiffer(this, differCallBack)


    inner class FavoriteViewHolder(
        private val favoriteListLayoutBinding: FavoriteListLayoutBinding,
        setOnItemClickListener: ((FavoriteMovieModel) -> Unit)?,
        setOnFavoriteClickListener: ((FavoriteMovieModel) -> Unit)?
    ) :
        RecyclerView.ViewHolder(favoriteListLayoutBinding.root) {
        fun bind(favoriteMovieModel: FavoriteMovieModel?) {
            favoriteListLayoutBinding.apply {
                favoriteMovieModel?.let { model ->
                    if (model.title == null) {
                        tvMovieTitle.text = model.name
                        if (model.name == null) {
                            tvMovieTitle.text = model.original_title
                        }
                    } else {
                        tvMovieTitle.text = model.title
                    }
                    iwMovie.load(
                        Constants.IMAGE_URL +
                                differ.currentList.getOrNull(bindingAdapterPosition)?.poster_path
                    ) {
                        crossfade(true)
                    }
                    tvRating.text = model.vote_average.toString()

                    root.setOnClickListener {
                        model.let { fav ->
                            setOnItemClick?.invoke(fav)
                        }
                    }
                    iwFavorite.setOnClickListener {
                        model.let { fav ->
                            setOnFavoriteClick?.invoke(fav)
                        }
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
            ), setOnItemClick, setOnFavoriteClick
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(differ.currentList.getOrNull(position))
    }

    private var setOnItemClick: ((FavoriteMovieModel) -> Unit)? = null
    private var setOnFavoriteClick: ((FavoriteMovieModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (FavoriteMovieModel) -> Unit) {
        setOnItemClick = listener
    }

    fun setOnFavoriteClickListener(listener: (FavoriteMovieModel) -> Unit) {
        setOnFavoriteClick = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}
package com.suret.moviesapp.util

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants
import com.willy.ratingbar.ScaleRatingBar

@BindingAdapter("bind:setTextMovie")
fun AppCompatTextView.setTitle(movie: TrendingMoviesModel) {
    if (movie.title == null) {
        text = movie.name
        if (movie.name == null) {
            text = movie.original_title
        }
    } else {
        text = movie.title
    }
}

@BindingAdapter("bind:setTextFavMovie")
fun AppCompatTextView.setTitle(movie: FavoriteMovieModel) {
    if (movie.title == null) {
        text = movie.name
        if (movie.name == null) {
            text = movie.original_title
        }
    } else {
        text = movie.title
    }
}

@BindingAdapter("bind:movieRating")
fun AppCompatTextView.bindMovieRatingForTv(voteAverage: Double) {
    text = roundForDouble(voteAverage)
}

@BindingAdapter("bind:movieRating")
fun ScaleRatingBar.bindMovieRatingForRB(ratingValue: Float) {
    rating = ratingValue
}

@BindingAdapter("bind:checkFavorite")
fun AppCompatImageView.setImage(isFavorite: Boolean) {
    when (isFavorite) {
        true -> setImageResource(R.drawable.ic_favorite_movie)
        false -> setImageResource(R.drawable.ic_disable_favorite)
    }
}

@BindingAdapter("bind:downloadImage")
fun AppCompatImageView.downloadImage(url: String?) {
    load(Constants.IMAGE_URL + url) {
        placeholder(placeholderProgressbar(this@downloadImage.context))
        crossfade(true)
        error(R.drawable.no_photo)
    }
}

private fun placeholderProgressbar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 4f
        centerRadius = 40f
        start()
    }
}

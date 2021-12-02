package com.suret.moviesapp.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants.IMAGE_URL
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

@BindingAdapter("bind:ratingForRatingBar")
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

@BindingAdapter("bind:setImage", "bind:progress")
fun AppCompatImageView.setImage(url: String?, progressBar: LottieAnimationView?) {
    if (!url.isNullOrEmpty()) {
        downloadImage(this, IMAGE_URL + url, progressBar)
    } else {
        progressBar?.isVisible = false
        setImageResource(R.drawable.no_photo)
    }
}

@BindingAdapter("bind:setAvatar", "bind:avatarProgress")
fun AppCompatImageView.setAvatar(url: String?, progressBar: LottieAnimationView?) {
    if (!url.isNullOrEmpty()) {
        if (url.startsWith("/https:", true)) {
            downloadImage(this, url.removePrefix("/"), progressBar)
        } else {
            downloadImage(this, IMAGE_URL + url, progressBar)
        }
    } else {
        progressBar?.isVisible = false
        setImageResource(R.drawable.ic_round_person_24)
    }
}

@BindingAdapter("bind:setDate")
fun AppCompatTextView.setDate(date: String?) {
    text = toDate(date)
}
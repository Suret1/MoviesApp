package com.suret.moviesapp.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.Filmography
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants.IMAGE_URL
import com.suret.moviesapp.util.Util.downloadImage
import com.suret.moviesapp.util.Util.dpToPx
import com.suret.moviesapp.util.Util.roundForDouble
import com.suret.moviesapp.util.Util.toDate
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

@BindingAdapter("bind:setTextActorMovie")
fun AppCompatTextView.setTitle(movie: Filmography) {
    text = movie.title ?: movie.original_language
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
    if (url.isNullOrEmpty().not()) {
        downloadImage(this, IMAGE_URL + url, progressBar)
    } else {
        progressBar?.isVisible = false
        setImageResource(R.drawable.no_photo)
    }
}

@BindingAdapter("bind:setAvatar", "bind:avatarProgress")
fun AppCompatImageView.setAvatar(url: String?, progressBar: LottieAnimationView?) {
    if (url.isNullOrEmpty().not()) {
        if (url!!.startsWith("/https:", true)) {
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

@BindingAdapter("bind:equalSpacing")
fun RecyclerView.itemDecoration(dp: Int) {
    var orientation = 0
    if (layoutManager is GridLayoutManager) {
        orientation = -1
    } else if (layoutManager is LinearLayoutManager) {
        orientation = (layoutManager as LinearLayoutManager).orientation
    }
    if (0 == itemDecorationCount) {
        addItemDecoration(EqualSpacingItemDecoration(dpToPx(dp), orientation, true))
    }
}
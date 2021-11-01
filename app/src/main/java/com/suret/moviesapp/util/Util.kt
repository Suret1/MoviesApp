package com.suret.moviesapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants
import com.willy.ratingbar.ScaleRatingBar
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // For 29 api or above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    // For below 29 api
    else {
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
            return true
        }
    }
    return false
}

fun splitNumber(budget: Int): String {
    val integer: BigInteger =
        BigInteger.valueOf(budget.toLong())
    return NumberFormat.getNumberInstance(Locale.US).format(
        integer
    )
}

fun convertHourAndMinutes(time: Int): String {
    var result = ""
    val hour = time.div(60)
    val minutes = time.mod(60)
    result = if (hour > 0) {
        "$hour h $minutes min"
    } else {
        "$minutes min"
    }
    return result
}

fun toDate(dateString: String?): String? {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return formatter.format(parser.parse(dateString))
}

fun roundForDouble(value: Double): String {
    val df = DecimalFormat("0.0")
    var result = "0.0"
    result = df.format(value)
    return result
}

@BindingAdapter("android:setTextMovie")
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

@BindingAdapter("android:movieRating")
fun AppCompatTextView.bindMovieRatingForTv(voteAverage: Double) {
    text = roundForDouble(voteAverage)
}

@BindingAdapter("app:movieRating")
fun ScaleRatingBar.bindMovieRatingForRB(ratingValue: Float) {
    rating = ratingValue
}

@BindingAdapter("android:checkFavorite")
fun AppCompatImageView.setImage(isFavorite: Boolean) {
    when (isFavorite) {
        true -> setImageResource(R.drawable.ic_favorite_movie)
        false -> setImageResource(R.drawable.ic_disable_favorite)
    }
}

@BindingAdapter("android:downloadImage")
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

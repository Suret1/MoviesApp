package com.suret.moviesapp.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


object Util {
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

    fun downloadImage(iw: AppCompatImageView, url: String?, progressBar: LottieAnimationView?) {
        progressBar?.isVisible = true
        Glide.with(iw)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.isVisible = false
                    return false
                }

            }).into(iw)
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun hideSystemUI(activity: FragmentActivity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    fun showSystemUI(activity: FragmentActivity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity.window.decorView.systemUiVisibility = View.VISIBLE
    }
}
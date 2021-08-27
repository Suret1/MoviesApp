package com.suret.moviesapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.math.BigInteger
import java.text.NumberFormat
import java.util.*

object AppUtil {

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

}
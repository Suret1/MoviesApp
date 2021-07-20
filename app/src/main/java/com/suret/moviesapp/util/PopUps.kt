package com.suret.moviesapp.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.suret.moviesapp.R

class PopUps {

    companion object {
        fun createAlertDialog(context: Context, style: Int): AlertDialog.Builder {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(context, style)
            builder.setCancelable(false)
            return builder
        }

        fun progressDialog(
            activity: Activity
        ): ProgressDialog {
            val pd = ProgressDialog(activity, R.style.RoundedCornersDialog)
            pd.setMessage(activity.applicationContext.resources.getString(R.string.loading))
            pd.setCancelable(false)
            return pd
        }

        //        fun showSnackbar(activity: Activity, @StringRes id: Int) {
//            val snackbar =
//                Snackbar.make(activity.window.decorView.rootView, "", Snackbar.LENGTH_SHORT)
//            val custom: View = activity.layoutInflater.inflate(R.layout.custom_snackbar, null)
//            val content: TextView = custom.findViewById(R.id.content)
//            content.text = activity.resources.getString(id)
//            snackbar.view.setBackgroundColor(Color.TRANSPARENT)
//            val snackbarLayout = snackbar.view as SnackbarLayout
//            snackbarLayout.setPadding(0, 0, 0, 0)
//            snackbarLayout.addView(custom, 0)
//            snackbar.show()
//        }
//
//        fun showSnackbarWithText(
//            activity: Activity,
//            text: String?,
//            layoutParams: FrameLayout.LayoutParams
//        ) {
//            val snackbar =
//                Snackbar.make(
//                    activity.window.decorView.rootView,
//                    text.toString(),
//                    Snackbar.LENGTH_SHORT
//                )
//            snackbar.view.layoutParams as FrameLayout.LayoutParams
//            layoutParams.gravity = Gravity.TOP
//            snackbar.view.setPadding(0, 0, 0, 0)
//            snackbar.view.layoutParams = layoutParams
//            val snackbarLayout = snackbar.view as SnackbarLayout
//            snackbarLayout.setPadding(0, 0, 0, 0)
//            snackbar.show()
//        }
//    }
    }
}
package com.suret.moviesapp.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.suret.moviesapp.R

class PopUps {

    companion object {
        fun error(context: Context, customLayout: Int): AlertDialog {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(context, R.style.RoundedCornersDialog)
            builder.setCancelable(false)
//            builder.setPositiveButton(R.string.ok) { dialog, _ ->
//                dialog.cancel()
//            }
            val factory = LayoutInflater.from(context)
            val view = factory.inflate(customLayout, null)
            builder.setView(customLayout)
            val alert: AlertDialog = builder.create()
            alert.show()
            return alert
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
//        fun showSnackbarWithText(activity: Activity, text: String?) {
//            val snackbar =
//                Snackbar.make(activity.window.decorView.rootView, "", Snackbar.LENGTH_SHORT)
//            val custom: View = activity.layoutInflater.inflate(R.layout.custom_snackbar, null)
//            val content: TextView = custom.findViewById(R.id.content)
//            content.text = text
//            snackbar.view.setBackgroundColor(Color.TRANSPARENT)
//            val snackbarLayout = snackbar.view as SnackbarLayout
//            snackbarLayout.setPadding(0, 0, 0, 0)
//            snackbarLayout.addView(custom, 0)
//            snackbar.show()
//        }
    }

}
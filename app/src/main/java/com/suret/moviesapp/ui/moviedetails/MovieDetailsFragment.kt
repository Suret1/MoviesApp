package com.suret.moviesapp.ui.moviedetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.databinding.FragmentMovieDetailsBinding
import kotlin.math.abs


class MovieDetailsFragment() : Fragment() {
    private lateinit var movieDetailsBinding: FragmentMovieDetailsBinding
    private var movieModel: TrendingMoviesModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieDetailsBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return movieDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullStatusBar()
        movieModel = arguments?.getParcelable("movieModel")

        if (movieModel != null) {
            movieSetData(movieModel!!)
        }


        movieDetailsBinding.apply {
            toolbar.setNavigationIcon(R.drawable.back_btn)
            toolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }


        }

    }

    private fun fullStatusBar() {
        activity?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    private fun movieSetData(moviesModel: TrendingMoviesModel) {
        movieDetailsBinding.apply {

            movieImage.load(
                "https://image.tmdb.org/t/p/original${
                    moviesModel.backdrop_path
                }"
            )
            if (moviesModel.title == null) {
                movieTitle.text = moviesModel.name
                if (moviesModel.name == null) {
                    movieTitle.text = moviesModel.original_title
                }
            } else {
                movieTitle.text = moviesModel.title
            }
            movieTitle.setColor(R.color.white, R.color.silver)
            ratingBar.rating = moviesModel.vote_average!!.toFloat()
            ratingTV.text = moviesModel.vote_average.toString()
            storyLineTV.text = moviesModel.overview

            appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (appBarLayout != null) {
                    if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                        movieTitle.visibility = View.GONE
                        collapsingToolbar.title = movieTitle.text
                        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingTitleStyle)
                    } else {
                        movieTitle.visibility = View.VISIBLE
                        collapsingToolbar.title = ""
                    }
                }
            })
        }
    }

}
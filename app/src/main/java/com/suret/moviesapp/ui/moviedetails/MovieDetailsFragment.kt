package com.suret.moviesapp.ui.moviedetails

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.suret.moviesapp.R
import com.suret.moviesapp.data.api.API
import com.suret.moviesapp.data.db.MovieDao
import com.suret.moviesapp.data.db.MovieDatabase
import com.suret.moviesapp.data.domain.MovieRepository
import com.suret.moviesapp.data.domain.MovieRepositoryImpl
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.viewmodel.MovieViewModel
import com.suret.moviesapp.data.viewmodel.MovieViewModelFactory
import com.suret.moviesapp.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class MovieDetailsFragment() : Fragment() {
    @Inject
    lateinit var api: API

    @Inject
    lateinit var movieDatabase: MovieDatabase

    @Inject
    lateinit var dao: MovieDao


    private lateinit var movieDetailsBinding: FragmentMovieDetailsBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieViewModelFactory: MovieViewModelFactory
    private lateinit var movieRepository: MovieRepository
    private var genreModelList: List<GenreModel>? = null
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

        movieRepository = MovieRepositoryImpl(requireActivity(), dao, api)
        movieViewModelFactory = MovieViewModelFactory(repository = movieRepository)
        movieViewModel =
            ViewModelProvider(this, movieViewModelFactory).get(
                MovieViewModel::
                class.java
            )


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            movieViewModel.getGenreList()
            movieViewModel.trendingMoviesFlow.collect { event ->
                when (event) {
                    is MovieViewModel.Event.Loading -> {
                    }
                    is MovieViewModel.Event.Failure -> {
                        Snackbar.make(requireView(), "No internet", Snackbar.LENGTH_SHORT).show()
                    }
                    is MovieViewModel.Event.Success -> {
                        genreModelList = event.genreModel
                        if (genreModelList != null) {
                            setMovieGenre(genreModelList!!)
                        }
                    }
                }
            }


        }

        movieDetailsBinding.apply {
            toolbar.setNavigationIcon(R.drawable.back_btn)
            toolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setMovieGenre(genresModel: List<GenreModel>) {
        val genresString = StringBuilder()
        val movieGenresList: List<Int>? = movieModel?.genre_ids
        for (genre in genresModel) {
            if (movieGenresList != null) {
                for (g in movieGenresList) {
                    if (genre.id == g) {
                        genresString.append(genre.name + ", ")
                    }
                }
            }
        }
        movieDetailsBinding.apply {
            if(genresString.isNotEmpty()){
                genresString.deleteCharAt(genresString.length - 2)
                genreTV.text = genresString
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
package com.suret.moviesapp.ui.moviedetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.data.other.Constants.CAST_LIST
import com.suret.moviesapp.data.other.Constants.CAST_MODEL
import com.suret.moviesapp.data.other.Constants.MOVIE_MODEL
import com.suret.moviesapp.data.other.Constants.SIMPLE_CAST_TYPE
import com.suret.moviesapp.databinding.FragmentMovieDetailsBinding
import com.suret.moviesapp.ui.moviedetails.adapters.FullCastAdapter
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs


@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieDetailsBinding: FragmentMovieDetailsBinding
    private var genreModelList: List<GenreModel>? = null
    private var movieModel: TrendingMoviesModel? = null
    private var castList: List<Cast>? = null
    private val bundle = Bundle()
    private lateinit var castListAdapter: FullCastAdapter


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

        movieModel = arguments?.getParcelable(MOVIE_MODEL)
        castListAdapter = FullCastAdapter()
        castListAdapter.sendTypeCast(SIMPLE_CAST_TYPE)
        if (movieModel != null) {
            movieSetData(movieModel!!)
            movieDetailsBinding.apply {
                rvCast.adapter = castListAdapter
                viewLifecycleOwner.lifecycleScope.launch {
                    movieModel!!.id?.let {
                        movieViewModel.getCredits(it)
                        movieViewModel.castFlow.collect { event ->
                            when (event) {
                                is MovieViewModel.Event.CastSuccess -> {
                                    castList = event.cast
                                    castListAdapter.differ.submitList(event.cast)
                                }
                                is MovieViewModel.Event.Failure -> {
                                    Toast.makeText(
                                        requireContext(),
                                        event.errorText,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is MovieViewModel.Event.Loading -> {
                                }
                            }
                        }
                    }
                }
                tvSeeAll.setOnClickListener {
                    if (castList != null) {
                        val castList = Gson().toJson(castList)
                        bundle.apply {
                            putString(CAST_LIST, castList)
                        }
                        findNavController().navigate(R.id.action_to_fullFragment, bundle)
                    }
                }
            }
            castListAdapter.setOnItemClickListener {
                bundle.apply {
                    putParcelable(CAST_MODEL, it)
                }
                findNavController().navigate(R.id.action_to_personDetailsFragment, bundle)
            }
            castListAdapter.stateRestorationPolicy =
                PREVENT_WHEN_EMPTY

        }


        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.getGenreList()
            movieViewModel.genreFlow.collect { event ->
                when (event) {
                    is MovieViewModel.Event.Loading -> {
                    }
                    is MovieViewModel.Event.Failure -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.no_internet),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is MovieViewModel.Event.GenreSuccess -> {
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
            if (genresString.isNotEmpty()) {
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

            movieImage.load(Constants.IMAGE_URL + moviesModel.backdrop_path)

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
package com.suret.moviesapp.ui.moviedetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.model.GenreModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
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
    private lateinit var castListAdapter: FullCastAdapter
    private var youtubeKey = ""

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

        movieModel?.let {
            movieDetailsBinding.apply {
                rvCast.adapter = castListAdapter
                movieSetData(it)
                viewLifecycleOwner.lifecycleScope.launch {
                    it.id?.let {
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
                                    //
                                }
                            }
                        }
                    }
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    it.id?.let { id -> movieViewModel.getMovieTrailer(id) }
                    movieViewModel.trailerFlow.collect { event ->
                        when (event) {
                            is MovieViewModel.Event.Loading -> {
                                //
                            }
                            is MovieViewModel.Event.Failure -> {
                                Snackbar.make(
                                    requireView(),
                                    event.errorText,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            is MovieViewModel.Event.TrailerSuccess -> {
                                event.trailerList?.let { trailerList ->
                                    if (!trailerList.isNullOrEmpty()) {
                                        youtubeKey = trailerList[0].key.toString()
                                        setTrailer(youtubeKey)
                                    }
                                }
                            }
                        }
                    }
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    movieViewModel.getGenreList()
                    movieViewModel.genreFlow.collect { event ->
                        when (event) {
                            is MovieViewModel.Event.Loading -> {
                                //
                            }
                            is MovieViewModel.Event.Failure -> {
                                Snackbar.make(
                                    requireView(),
                                    event.errorText,
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
                goToFullCastFragment()
            }
        }
        goToPersonDetailFragment()
        castListAdapter.stateRestorationPolicy =
            PREVENT_WHEN_EMPTY
        setToolBar()
    }

    private fun FragmentMovieDetailsBinding.goToFullCastFragment() {
        tvSeeAll.setOnClickListener {
            if (castList != null) {
                val castList = Gson().toJson(castList)
                findNavController().navigate(
                    R.id.action_to_fullFragment,
                    bundleOf().apply { putString(CAST_LIST, castList) })
            }
        }
    }

    private fun goToPersonDetailFragment() {
        castListAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_to_personDetailsFragment,
                bundleOf().apply { putParcelable(CAST_MODEL, it) })
        }
    }

    private fun setToolBar() {
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
            setMovieTitle(moviesModel)
            setRatingData(moviesModel)
            setStoryline(moviesModel)
            appBarListener()
        }
    }

    private fun FragmentMovieDetailsBinding.setStoryline(moviesModel: TrendingMoviesModel) {
        storyLineTV.text = moviesModel.overview
    }

    private fun FragmentMovieDetailsBinding.setTrailer(youtubeKey: String?) {
        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (youtubeKey != null) {
                    youTubePlayer.cueVideo(
                        youtubeKey, 0f
                    )
                }
            }

        })
    }

    private fun FragmentMovieDetailsBinding.setRatingData(
        moviesModel: TrendingMoviesModel
    ) {
        ratingBar.rating = moviesModel.vote_average?.toFloat() ?: 0.0f
        ratingTV.text = moviesModel.vote_average.toString()
    }

    private fun FragmentMovieDetailsBinding.appBarListener() {
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

    private fun FragmentMovieDetailsBinding.setMovieTitle(moviesModel: TrendingMoviesModel) {
        if (moviesModel.title == null) {
            movieTitle.text = moviesModel.name
            if (moviesModel.name == null) {
                movieTitle.text = moviesModel.original_title
            }
        } else {
            movieTitle.text = moviesModel.title
        }
        movieTitle.setColor(R.color.white, R.color.silver)
    }

}
package com.suret.moviesapp.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.MovieDetailsModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants.INITIAL_IS_COLLAPSED
import com.suret.moviesapp.data.other.Constants.MAX_LINES_COLLAPSED
import com.suret.moviesapp.data.other.Constants.SIMPLE_CAST_TYPE
import com.suret.moviesapp.databinding.FragmentMovieDetailsBinding
import com.suret.moviesapp.ui.moviedetails.adapters.FullCastAdapter
import com.suret.moviesapp.ui.moviedetails.adapters.GenreAdapter
import com.suret.moviesapp.ui.moviedetails.adapters.ProductionsAdapter
import com.suret.moviesapp.ui.moviedetails.adapters.ReviewAdapter
import com.suret.moviesapp.ui.moviedetails.viewmodel.MovieDetailsFragmentVM
import com.suret.moviesapp.util.PopUps.Companion.showSnackBar
import com.suret.moviesapp.util.Util.convertHourAndMinutes
import com.suret.moviesapp.util.Util.roundForDouble
import com.suret.moviesapp.util.Util.splitNumber
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs


@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private val binding by lazy { FragmentMovieDetailsBinding.inflate(layoutInflater) }
    private val viewModel: MovieDetailsFragmentVM by viewModels()
    private var movieModel: TrendingMoviesModel? = null
    private var favoriteMovieModel: FavoriteMovieModel? = null
    private var castList: List<Cast>? = null
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var productionsAdapter: ProductionsAdapter
    private lateinit var castListAdapter: FullCastAdapter
    private lateinit var genreAdapter: GenreAdapter
    private var youtubeKey = ""
    private val args: MovieDetailsFragmentArgs by navArgs()
    private var isClicked = true
    private var movieId: Int = 0

    companion object {
        private var isFavorite = false
    }

    private val bounce: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.bounce
        )
    }
    private val bounce_gone: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.bounce_gone
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initAdapters()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieModel = args.movieModel
        favoriteMovieModel = args.favModel

        reviewOnClick()
        favoriteMovieModel?.let {
            getData(castToTrendingMoviesModel(it))
            movieId = it.id!!
        }
        movieModel?.let {
            getData(it)
            movieId = it.id!!
        }
        checkFavorite()
        goToPersonDetailFragment()
        setVisibility()
        setToolBar()
        goToSimilarFragment()
    }

    private fun reviewOnClick() {
        reviewAdapter.onItemClick = { review ->
            findNavController().navigate(
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToReviewBottomSheet(
                    review
                )
            )
        }
    }

    private fun checkFavorite() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val favModel = viewModel.getFavoriteMovieByID(movieId)
            isFavorite = if (favModel != null) {
                true
            } else {
                false
            }
            setFAB(isFavorite)
        }
    }

    private fun initAdapters() {
        productionsAdapter = ProductionsAdapter()
        castListAdapter = FullCastAdapter()
        castListAdapter.sendTypeCast(SIMPLE_CAST_TYPE)
        reviewAdapter = ReviewAdapter()
        genreAdapter = GenreAdapter()
        binding.rvCast.adapter = castListAdapter
        binding.rvProductions.adapter = productionsAdapter
        binding.rvReview.adapter = reviewAdapter
        binding.rvGenre.adapter = genreAdapter
        reviewAdapter.stateRestorationPolicy = PREVENT_WHEN_EMPTY
        castListAdapter.stateRestorationPolicy =
            PREVENT_WHEN_EMPTY
    }

    private fun goToSimilarFragment() {
        binding.apply {
            fabSearch.setOnClickListener {
                setVisibility()
            }
            fabSimilar.setOnClickListener {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToSimilarFragment()
                        .setMovieID(movieId)
                )
                isClicked = false
            }
        }
    }

    private fun setVisibility() {
        binding.apply {
            if (!isClicked) {
                fabSearch.extend()
                fabSimilar.startAnimation(bounce)
                tvSimilar.startAnimation(bounce)
                fabSimilar.isVisible = true
                tvSimilar.isVisible = true
                isClicked = true
            } else {
                fabSearch.shrink()
                tvSimilar.isVisible = false
                tvSimilar.isVisible = false
                fabSimilar.isVisible = false
                tvSimilar.startAnimation(bounce_gone)
                fabSimilar.startAnimation(bounce_gone)
                isClicked = false
            }
        }
    }

    private fun getData(model: TrendingMoviesModel) {
        binding.apply {
            movieSetData(model)
            viewLifecycleOwner.lifecycleScope.launch {
                model.id?.let {
                    viewModel.getMovieDetails(it)
                    viewModel.detailsFlow.collect { event ->
                        when (event) {
                            is MovieDetailsFragmentVM.Event.Failure -> {
                                //
                            }
                            is MovieDetailsFragmentVM.Event.DetailsSuccess -> {
                                binding.apply {
                                    event.details?.let { model ->
                                        setMovieDetails(model)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                delay(300)
                model.id?.let { id -> viewModel.getMovieTrailer(id) }
                viewModel.trailerFlow.collect { event ->
                    when (event) {
                        is MovieDetailsFragmentVM.Event.Loading -> {
                            progressBar.isVisible = true
                        }
                        is MovieDetailsFragmentVM.Event.Failure -> {
                            progressBar.isVisible = false
                            Snackbar.make(
                                requireView(),
                                event.errorText,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        is MovieDetailsFragmentVM.Event.TrailerSuccess -> {
                            progressBar.isVisible = false
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
                delay(500)
                model.id?.let {
                    viewModel.getCredits(it)
                    viewModel.castFlow.collect { event ->
                        when (event) {
                            is MovieDetailsFragmentVM.Event.CastSuccess -> {
                                castList = event.cast
                                castListAdapter.submitList(event.cast)
                            }
                            is MovieDetailsFragmentVM.Event.Failure -> {
                                //
                            }
                            is MovieDetailsFragmentVM.Event.Loading -> {
                                //
                            }
                        }
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000)
                model.id?.let { id -> viewModel.getReviews(id) }
                viewModel.reviewFlow.collect { event ->
                    when (event) {
                        is MovieDetailsFragmentVM.Event.Loading -> {
                            //
                        }
                        is MovieDetailsFragmentVM.Event.Failure -> {
                            //
                        }
                        is MovieDetailsFragmentVM.Event.ReviewsSuccess -> {
                            event.reviews.let {
                                reviewAdapter.submitList(it)
                            }
                        }

                    }
                }
            }
            goToFullCastFragment()
        }
    }

    private fun FragmentMovieDetailsBinding.setMovieDetails(model: MovieDetailsModel) {
        if (model.budget != null) {
            tvBudget.text =
                splitNumber(model.budget) + getString(R.string.dollar)
        }
        if (model.revenue != null) {
            tvRevenue.text =
                splitNumber(model.revenue) + getString(R.string.dollar)
        }
        if (model.runtime != null) {
            tvRuntime.text =
                convertHourAndMinutes(model.runtime)
        }
        genreAdapter.submitList(model.genres)
        productionsAdapter.submitList(model.production_companies)
    }

    private fun FragmentMovieDetailsBinding.goToFullCastFragment() {
        tvSeeAll.setOnClickListener {
            castList?.let {
                val array = arrayListOf<Cast>()
                array.addAll(it)
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToFullCastFragment(
                        array
                    )
                )
            }

        }
    }

    private fun goToPersonDetailFragment() {
        castListAdapter.onItemClick = {
            findNavController().navigate(
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPersonDetailsFragment(
                )
            )
        }
    }

    private fun setToolBar() {
        binding.apply {
            toolbarDetails.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun castToTrendingMoviesModel(movie: FavoriteMovieModel): TrendingMoviesModel {
        return TrendingMoviesModel(
            movie.backdrop_path,
            movie.first_air_date,
            movie.genre_ids,
            movie.id,
            movie.name,
            movie.original_language,
            movie.original_name,
            movie.original_title,
            movie.overview,
            movie.popularity,
            movie.poster_path,
            movie.release_date,
            movie.title,
            movie.vote_average,
            movie.vote_count,
            movie.isFavorite
        )
    }

    private fun movieSetData(moviesModel: TrendingMoviesModel) {
        binding.apply {
            setMovieTitle(moviesModel)
            setRatingData(moviesModel)
            setStoryline(moviesModel)
            setReleaseDate(moviesModel)
            setFAB(isFavorite)
            setFavoriteFabListener(moviesModel)
            appBarListener()
        }
    }

    private fun FragmentMovieDetailsBinding.setReleaseDate(moviesModel: TrendingMoviesModel) {
        if (!moviesModel.release_date.isNullOrEmpty()) {
            tvReleaseDate.text = moviesModel.release_date
        }
    }

    private fun setFavoriteFabListener(moviesModel: TrendingMoviesModel) {
        binding.apply {
            fabFav.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    moviesModel.let { model ->
                        viewModel.updateMovieModel(
                            setFavoriteStatus(
                                model,
                                isFavorite
                            )
                        )
                        if (!isFavorite) {
                            viewModel.insertFavoriteMovie(
                                createFavoriteModel(
                                    setFavoriteStatus(
                                        model,
                                        isFavorite
                                    )
                                )
                            )
                            isFavorite = true
                            movieSetData(newTrendModel(model, true))
                            showSnackBar(it, requireActivity(), R.string.add_to_fav)
                        } else {
                            viewModel.removeFavoriteMovie(
                                createFavoriteModel(
                                    setFavoriteStatus(
                                        model,
                                        isFavorite
                                    )
                                )
                            )
                            isFavorite = false
                            movieSetData(newTrendModel(model, false))
                            showSnackBar(it, requireActivity(), R.string.remove_favorites)
                        }
                    }
                }
            }
        }
    }

    private fun setFAB(isFavorite: Boolean) {
        binding.apply {
            if (isFavorite) {
                fabFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_favorite_movie
                    )
                )
            } else {
                fabFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.favorite
                    )
                )
            }
        }
    }

    private fun createFavoriteModel(movie: TrendingMoviesModel): FavoriteMovieModel {
        return FavoriteMovieModel(
            movie.backdrop_path,
            movie.first_air_date,
            movie.genre_ids,
            movie.id,
            movie.name,
            movie.original_language,
            movie.original_name,
            movie.original_title,
            movie.overview,
            movie.popularity,
            movie.poster_path,
            movie.release_date,
            movie.title,
            movie.vote_average,
            movie.vote_count,
            movie.isFavorite
        )
    }

    private fun setFavoriteStatus(
        movie: TrendingMoviesModel,
        isFavorite: Boolean
    ): TrendingMoviesModel {
        return if (isFavorite) {
            newTrendModel(
                movie,
                false
            )
        } else {
            newTrendModel(
                movie,
                true
            )
        }
    }

    private fun newTrendModel(
        model: TrendingMoviesModel,
        isFavorite: Boolean
    ): TrendingMoviesModel {
        return TrendingMoviesModel(
            model.backdrop_path,
            model.first_air_date,
            model.genre_ids,
            model.id,
            model.name,
            model.original_language,
            model.original_name,
            model.original_title,
            model.overview,
            model.popularity,
            model.poster_path,
            model.release_date,
            model.title,
            model.vote_average,
            model.vote_count,
            isFavorite
        )
    }

    private fun FragmentMovieDetailsBinding.setStoryline(moviesModel: TrendingMoviesModel) {
        var isCollapsed = INITIAL_IS_COLLAPSED
        storyLineTV.text = moviesModel.overview
        storyLineTV.setOnClickListener {
            if (isCollapsed) {
                storyLineTV.maxLines = Int.MAX_VALUE
            } else {
                storyLineTV.maxLines = MAX_LINES_COLLAPSED
            }
            isCollapsed = !isCollapsed
            TransitionManager.beginDelayedTransition(parentLayout)
        }

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
        ratingTV.text = moviesModel.vote_average?.let { roundForDouble(it) }
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
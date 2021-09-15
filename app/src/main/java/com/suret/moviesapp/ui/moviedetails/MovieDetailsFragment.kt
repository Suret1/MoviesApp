package com.suret.moviesapp.ui.moviedetails

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
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
import com.suret.moviesapp.ui.moviedetails.adapters.ProductionsAdapter
import com.suret.moviesapp.ui.moviedetails.adapters.ReviewAdapter
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel.Event
import com.suret.moviesapp.util.AppUtil.convertHourAndMinutes
import com.suret.moviesapp.util.AppUtil.roundForDouble
import com.suret.moviesapp.util.AppUtil.splitNumber
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs


@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private val movieViewModel: MovieViewModel by viewModels()
    private var _binding: FragmentMovieDetailsBinding? = null
    private var movieModel: TrendingMoviesModel? = null
    private var favoriteMovieModel: FavoriteMovieModel? = null
    private var castList: List<Cast>? = null
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var productionsAdapter: ProductionsAdapter
    private lateinit var castListAdapter: FullCastAdapter
    private var youtubeKey = ""
    private val args: MovieDetailsFragmentArgs by navArgs()
    private var isClicked = false
    private var movieId: Int = 0

    private val binding get() = _binding!!

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        movieModel = args.movieModel
        favoriteMovieModel = args.favModel

        binding.apply {
            initAdapters()
        }
        reviewAdapter.setOnItemClickListener { review ->
            findNavController().navigate(
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToReviewBottomSheet(
                    review
                )
            )
        }

        favoriteMovieModel?.let {
            getData(castToTrendingMoviesModel(it))
            movieId = it.id!!
        }
        movieModel?.let {
            getData(it)
            movieId = it.id!!
        }
        goToPersonDetailFragment()
        castListAdapter.stateRestorationPolicy =
            PREVENT_WHEN_EMPTY
        setToolBar()
        goToSimilarFragment()
    }

    private fun FragmentMovieDetailsBinding.initAdapters() {
        productionsAdapter = ProductionsAdapter()
        castListAdapter = FullCastAdapter()
        castListAdapter.sendTypeCast(SIMPLE_CAST_TYPE)
        reviewAdapter = ReviewAdapter()
        rvCast.adapter = castListAdapter
        rvProductions.adapter = productionsAdapter
        rvReview.adapter = reviewAdapter
    }

    private fun goToSimilarFragment() {
        binding.apply {
            fabSimilar.setOnClickListener {
                findNavController().navigate(
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToSimilarFragment()
                        .setMovieID(movieId)
                )
                isClicked = false
            }
            fabSearch.setOnClickListener {
                searchFABClicked()
            }
        }
    }

    private fun searchFABClicked() {
        setVisibility()
    }

    private fun setVisibility() {
        binding.apply {
            if (!isClicked) {
                fabSimilar.startAnimation(fromBottom)
                tvSimilar.startAnimation(fromBottom)
                fabSearch.setImageResource(R.drawable.ic_round_add_24)
                fabSearch.startAnimation(rotateOpen)
                fabSimilar.visibility = View.VISIBLE
                tvSimilar.visibility = View.VISIBLE
                isClicked = true
            } else {
                fabSimilar.visibility = View.GONE
                tvSimilar.visibility = View.GONE
                tvSimilar.startAnimation(toBottom)
                fabSimilar.startAnimation(toBottom)
                fabSearch.startAnimation(rotateClose)
                fabSearch.setImageResource(R.drawable.ic_round_search_24)
                isClicked = false
            }
        }
    }

    private fun getData(model: TrendingMoviesModel) {
        binding.apply {
            movieSetData(model)
            viewLifecycleOwner.lifecycleScope.launch {
                model.id?.let {
                    movieViewModel.getMovieDetails(it)
                    movieViewModel.detailsFlow.collect { event ->
                        when (event) {
                            is Event.Failure -> {
                                //
                            }
                            is Event.DetailsSuccess -> {
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
                model.id?.let {
                    movieViewModel.getCredits(it)
                    movieViewModel.castFlow.collect { event ->
                        when (event) {
                            is Event.CastSuccess -> {
                                castList = event.cast
                                castListAdapter.differ.submitList(event.cast)
                            }
                            is Event.Failure -> {
                                //
                            }
                            is Event.Loading -> {
                                //
                            }
                        }
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                model.id?.let { id -> movieViewModel.getMovieTrailer(id) }
                movieViewModel.trailerFlow.collect { event ->
                    when (event) {
                        is Event.Loading -> {
                            //
                        }
                        is Event.Failure -> {
                            Snackbar.make(
                                requireView(),
                                event.errorText,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        is Event.TrailerSuccess -> {
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
                model.id?.let { id -> movieViewModel.getReviews(id) }
                movieViewModel.reviewFlow.collect { event ->
                    when (event) {
                        is Event.Loading -> {
                            //
                        }
                        is Event.Failure -> {
                            //
                        }
                        is Event.ReviewsSuccess -> {
                            event.reviews.let {
                                reviewAdapter.differ.submitList(it)
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
        val list = model.genres
        val genresString = StringBuilder()
        list?.forEach { g ->
            genresString.append(g.name + ", ")
        }
        if (genresString.isNotEmpty()) {
            genresString.deleteCharAt(genresString.length - 2)
            genreTV.text = genresString
        }
        productionsAdapter.differ.submitList(model.production_companies)
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
        castListAdapter.setOnItemClickListener {
            findNavController().navigate(
                MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPersonDetailsFragment(
                    it
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
            setFAB(moviesModel)
            setFabListener(moviesModel)
            appBarListener()
        }
    }

    private fun FragmentMovieDetailsBinding.setReleaseDate(moviesModel: TrendingMoviesModel) {
        if (!moviesModel.release_date.isNullOrEmpty()) {
            tvReleaseDate.text = moviesModel.release_date
        }
    }

    private fun setFabListener(moviesModel: TrendingMoviesModel) {
        binding.apply {
            fabFav.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    moviesModel.let { model ->
                        movieViewModel.updateMovieModel(
                            setFavoriteStatus(
                                model,
                                model.isFavorite
                            )
                        )
                        if (!model.isFavorite) {
                            movieViewModel.insertFavoriteMovie(
                                createFavoriteModel(
                                    setFavoriteStatus(
                                        model,
                                        model.isFavorite
                                    )
                                )
                            )
                            movieSetData(newTrendModel(model, true))
                            showSnackBar(it, R.string.add_to_fav)
                        } else {
                            movieViewModel.removeFavoriteMovie(
                                createFavoriteModel(
                                    setFavoriteStatus(
                                        model,
                                        model.isFavorite
                                    )
                                )
                            )
                            movieSetData(newTrendModel(model, false))
                            showSnackBar(it, R.string.remove_favorites)
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBar(view: View, @StringRes id: Int) {
        val snack = Snackbar.make(view, getString(id), Snackbar.LENGTH_SHORT)
        val layoutParams = FrameLayout.LayoutParams(snack.view.layoutParams)
        layoutParams.gravity = Gravity.TOP
        snack.view.setPadding(0, 0, 0, 0)
        snack.view.layoutParams = layoutParams
        snack.view.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_in_snack_bar
            )
        )
        snack.show()
    }

    private fun setFAB(moviesModel: TrendingMoviesModel) {
        binding.apply {
            if (moviesModel.isFavorite) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
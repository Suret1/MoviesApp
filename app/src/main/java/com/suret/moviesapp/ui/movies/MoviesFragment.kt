package com.suret.moviesapp.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.google.android.material.snackbar.Snackbar
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.data.other.Constants.MOVIE_MODEL
import com.suret.moviesapp.databinding.FragmentMoviesBinding
import com.suret.moviesapp.ui.movies.adapter.TrendMovieListAdapter
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel
import com.suret.moviesapp.util.PopUps
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var moviesBinding: FragmentMoviesBinding

    private lateinit var movieAdapter: TrendMovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentMoviesBinding.inflate(inflater, container, false)
        return moviesBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = PopUps.progressDialog(requireActivity())

        movieAdapter = TrendMovieListAdapter()

        movieAdapter.stateRestorationPolicy =
            PREVENT_WHEN_EMPTY


        moviesBinding.apply {
            trendMoviesRV.adapter = movieAdapter
            swipeRefresh.setOnRefreshListener {
                movieViewModel.getTrendingMovies()
            }
        }

        movieAdapter.setOnClickListener { movie ->
            movie.let {
                view.findNavController().navigate(
                    R.id.action_to_movieDetailsFragment,
                    bundleOf().apply { putParcelable(MOVIE_MODEL, movie) })
            }
        }
        movieAdapter.setOnFavoriteClickListener { movie ->
            viewLifecycleOwner.lifecycleScope.launch {
                movieViewModel.updateMovieModel(setFavoriteStatus(movie, movie.isFavorite))
                if (!movie.isFavorite) {
                    movieViewModel.insertFavoriteMovie(
                        createFavoriteModel(
                            setFavoriteStatus(
                                movie,
                                movie.isFavorite
                            )
                        )
                    )
                    showSnackBar(view, R.string.add_to_fav)
                } else {
                    movieViewModel.removeFavoriteMovie(
                        createFavoriteModel(
                            setFavoriteStatus(
                                movie,
                                movie.isFavorite
                            )
                        )
                    )
                    showSnackBar(view, R.string.remove_favorites)
                }
            }
        }
        observeList()

        moviesBinding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                movieViewModel.trendingMoviesFlow.collect { event ->
                    when (event) {
                        is MovieViewModel.Event.Loading -> {
                            progressBar.show()
                        }
                        is MovieViewModel.Event.Failure -> {
                            progressBar.dismiss()
                            Snackbar.make(requireView(), event.errorText, Snackbar.LENGTH_SHORT)
                                .show()
                            observeList()
                            swipeRefresh.isRefreshing = false
                        }
                        is MovieViewModel.Event.TrendingSuccess -> {
                            progressBar.dismiss()
                            movieAdapter.differ.submitList(event.trendingMoviesModel)
                            swipeRefresh.isRefreshing = false
                        }
                    }
                }
            }
        }
    }

    private fun observeList() {
        movieViewModel.getMovieList().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                movieViewModel.getTrendingMovies()
            } else {
                movieAdapter.differ.submitList(it)
            }
        })
    }

    private fun showSnackBar(view: View, @StringRes id: Int) {
        val snack = Snackbar.make(view, getString(id), Snackbar.LENGTH_SHORT)
        val layoutParams = FrameLayout.LayoutParams(snack.view.layoutParams)
        layoutParams.gravity = Gravity.TOP
        snack.view.setPadding(0, 10, 0, 0)
        snack.view.layoutParams = layoutParams
        snack.view.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_in_snack_bar
            )
        )
        snack.show()
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
        if (isFavorite) {
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
                false
            )
        } else {
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
                true
            )
        }
    }


}





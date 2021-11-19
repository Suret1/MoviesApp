package com.suret.moviesapp.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.databinding.FragmentFavouritesBinding
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel
import com.suret.moviesapp.util.PopUps
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private val binding by lazy { FragmentFavouritesBinding.inflate(layoutInflater) }
    private var removeAlertDialog: AlertDialog.Builder? = null
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteAdapter = FavoriteAdapter()

        binding.rvFavoriteMovies.adapter = favoriteAdapter

        movieViewModel.getFavoriteMovies().observe(viewLifecycleOwner, {
            favoriteAdapter.submitList(it)
        })
        favoriteAdapter.setOnItemClick = { favModel ->
            view.findNavController().navigate(
                FavouritesFragmentDirections.actionFavouriteToMovieDetailsFragment(null, favModel)
            )
        }
        favoriteAdapter.setOnFavoriteClick = {
            initRemoveDialog(it)
        }
        favoriteAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun initRemoveDialog(favoriteMovieModel: FavoriteMovieModel) {
        removeAlertDialog = PopUps.createAlertDialog(requireContext(), R.style.RemoveAlertDialog)

        removeAlertDialog.let { remove ->
            remove?.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            remove?.setTitle(getString(R.string.remove_movie))
            remove?.setMessage(getString(R.string.remove_question))
            remove?.setPositiveButton(getString(R.string.yes)) { _, _ ->
                movieViewModel.updateMovieModel(castToTrendingMoviesModel(favoriteMovieModel))
                movieViewModel.removeFavoriteMovie(favoriteMovieModel)
            }
            val alertDialog: AlertDialog = removeAlertDialog!!.create()
            alertDialog.show()
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
            false
        )
    }
}
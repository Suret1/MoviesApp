package com.suret.moviesapp.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.google.android.material.snackbar.Snackbar
import com.suret.moviesapp.R
import com.suret.moviesapp.databinding.FragmentMoviesBinding
import com.suret.moviesapp.ui.movies.adapter.TrendMovieListAdapter
import com.suret.moviesapp.ui.movies.viewmodel.MoviesVM
import com.suret.moviesapp.util.PopUps
import com.suret.moviesapp.util.PopUps.Companion.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Movies : Fragment() {
    private val binding by lazy { FragmentMoviesBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MoviesVM>()

    private val progressBar by lazy { PopUps.progressDialog(requireActivity()) }

    private val adapter by lazy {
        TrendMovieListAdapter().also {
            binding.rvMovies.adapter = it
            it.stateRestorationPolicy = PREVENT_WHEN_EMPTY
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.executePendingBindings()

        initObservers()
        setClicks()

        return binding.root
    }

    private fun initObservers() {
        viewModel.localList().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                viewModel.getTrendingMovies()
            } else {
                adapter.submitList(it)
            }
        })

        viewModel.listTrendingMovies
            .asLiveData()
            .observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    adapter.submitList(it)
                }
            }

        viewModel.isLoading
            .asLiveData()
            .observe(viewLifecycleOwner) {
                if (it) {
                    progressBar.show()
                } else {
                    progressBar.dismiss()
                }
            }

        viewModel.errorMessage
            .asLiveData()
            .observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun setClicks() {
        adapter.setOnItemClick = { movie ->
            movie.let {
                findNavController().navigate(
                    MoviesDirections.actionMoviesToMovieDetailsFragment(movie, null)
                )
            }
        }
        adapter.setOnFavoriteClick = { movie ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateMovieModel(viewModel.setFavoriteStatus(movie, movie.isFavorite))
                if (movie.isFavorite.not()) {
                    viewModel.insertFavoriteMovie(
                        viewModel.createFavoriteModel(
                            viewModel.setFavoriteStatus(
                                movie,
                                movie.isFavorite
                            )
                        )
                    )
                    showSnackBar(requireView(), requireActivity(), R.string.add_to_fav)
                } else {
                    viewModel.removeFavoriteMovie(
                        viewModel.createFavoriteModel(
                            viewModel.setFavoriteStatus(
                                movie,
                                movie.isFavorite
                            )
                        )
                    )
                    showSnackBar(requireView(), requireActivity(), R.string.remove_favorites)
                }
            }
        }
    }

}





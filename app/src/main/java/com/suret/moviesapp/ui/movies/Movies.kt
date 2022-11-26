package com.suret.moviesapp.ui.movies

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import com.suret.moviesapp.util.PopUps.Companion.customSnackBar
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

    @RequiresApi(Build.VERSION_CODES.Q)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initObservers() {
        viewModel.localList().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.listTrendingMovies.collect {
                if (it.isNotEmpty()) {
                    adapter.submitList(it)
                }
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
                    binding.swipeRefresh.isRefreshing = false
                    customSnackBar(
                        requireView(), R.drawable.ic_baseline_wifi_off_, it,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(" ") {
                        val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                        startActivity(panelIntent)
                    }.show()
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
                    val favStr = getString(R.string.add_to_fav)
                    customSnackBar(
                        requireView(),
                        R.drawable.ic_favorite_movie,
                        favStr,
                        Snackbar.LENGTH_SHORT
                    ).setAction(" ") {}.show()
                } else {
                    viewModel.removeFavoriteMovie(
                        viewModel.createFavoriteModel(
                            viewModel.setFavoriteStatus(
                                movie,
                                movie.isFavorite
                            )
                        )
                    )
                    val removeStr = getString(R.string.remove_favorites)
                    customSnackBar(
                        requireView(),
                        R.drawable.ic_disable_favorite,
                        removeStr,
                        Snackbar.LENGTH_SHORT
                    ).setAction(" ") {}.show()
                }
            }
        }
    }

}





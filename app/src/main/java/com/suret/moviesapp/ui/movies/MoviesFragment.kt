package com.suret.moviesapp.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.suret.moviesapp.R
import com.suret.moviesapp.data.other.Constants.MOVIE_MODEL
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel
import com.suret.moviesapp.databinding.FragmentMoviesBinding
import com.suret.moviesapp.ui.movies.adapter.TrendMovieListAdapter
import com.suret.moviesapp.util.PopUps
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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


        val bundle = Bundle()

        val progressBar = PopUps.progressDialog(requireActivity())

        movieAdapter = TrendMovieListAdapter()

        movieAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        movieViewModel.getTrendingMovies()

        moviesBinding.apply {
            trendMoviesRV.adapter = movieAdapter
        }

        movieAdapter.setOnClickListener { movie ->
            movie.let {
                bundle.apply {
                    putParcelable(MOVIE_MODEL, movie)
                }
            }
            view.findNavController().navigate(R.id.action_to_movieDetailsFragment, bundle)
        }

        moviesBinding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                movieViewModel.trendingMoviesFlow.collect { event ->
                    when (event) {
                        is MovieViewModel.Event.Loading ->
                            progressBar.show()
                        is MovieViewModel.Event.Failure -> {
                            progressBar.dismiss()
                            Snackbar.make(requireView(), event.errorText, Snackbar.LENGTH_SHORT)
                                .show()
                            movieAdapter.differ.submitList(event.localData)
                        }
                        is MovieViewModel.Event.Success -> {
                            progressBar.dismiss()
                            movieAdapter.differ.submitList(event.result)
                        }
                    }

                }
            }
        }

    }

}





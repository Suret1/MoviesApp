package com.suret.moviesapp.ui.movies

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.suret.moviesapp.R
import com.suret.moviesapp.data.api.API
import com.suret.moviesapp.data.db.MovieDatabase
import com.suret.moviesapp.data.domain.MovieRepository
import com.suret.moviesapp.data.domain.MovieRepositoryImpl
import com.suret.moviesapp.data.viewmodel.MovieViewModel
import com.suret.moviesapp.data.viewmodel.MovieViewModelFactory
import com.suret.moviesapp.databinding.FragmentMoviesBinding
import com.suret.moviesapp.ui.adapters.TrendMovieListAdapter
import kotlinx.coroutines.flow.collect
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MoviesFragment : Fragment() {
    private lateinit var moviesBinding: FragmentMoviesBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieViewModelFactory: MovieViewModelFactory
    private lateinit var movieRepository: MovieRepository
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


        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val api: API = retrofit.create(API::class.java)
        val dao = MovieDatabase.getDatabase(requireActivity()).movieDao()
        movieRepository = MovieRepositoryImpl(requireActivity(), dao, api)
        movieViewModelFactory = MovieViewModelFactory(repository = movieRepository)
        movieViewModel =
            ViewModelProvider(this, movieViewModelFactory).get(MovieViewModel::class.java)

        val bundle = Bundle()


        movieAdapter = TrendMovieListAdapter()

        movieAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        moviesBinding.apply {
            trendMoviesRV.adapter = movieAdapter

        }

        movieAdapter.setOnClickListener { movie ->
            movie.let {
                bundle.apply {
                    putParcelable("movieModel", movie)
                }
            }
            view.findNavController().navigate(R.id.action_to_movieDetailsFragment, bundle)
        }

        moviesBinding.apply {


            lifecycleScope.launchWhenCreated {
                movieViewModel.getTrendingMovies()
                movieViewModel.trendingMoviesFlow.collect { event ->

                    when (event) {
                        is MovieViewModel.Event.Loading ->
                            loading.visibility = View.VISIBLE
                        is MovieViewModel.Event.Failure -> {
                            loading.visibility = View.GONE
                            Snackbar.make(requireView(), event.errorText, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        is MovieViewModel.Event.Success -> {
                            loading.visibility = View.GONE
                            movieAdapter.differ.submitList(event.result)
                        }
                    }

                }
            }
        }

    }



}
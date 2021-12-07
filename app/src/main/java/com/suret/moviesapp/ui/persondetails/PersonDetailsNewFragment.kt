package com.suret.moviesapp.ui.persondetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.suret.moviesapp.R
import com.suret.moviesapp.databinding.FragmentPersonDetailsNewBinding
import com.suret.moviesapp.ui.persondetails.adapter.FilmographyAdapter
import com.suret.moviesapp.ui.persondetails.viewmodel.PersonDetailsFragmentVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PersonDetailsNewFragment : Fragment() {
    private val binding by lazy { FragmentPersonDetailsNewBinding.inflate(layoutInflater) }
    private val viewModel: PersonDetailsFragmentVM by viewModels()
    private val args: PersonDetailsNewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FilmographyAdapter()

        binding.toolbarDetails.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.actorPoster.setImageResource(R.drawable.robert_downey)

        binding.rvFilmography.adapter = adapter

        viewModel.getPersonMovieCredits(args.castModel?.id!!)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.movieFlow.collect { event ->
                when (event) {
                    is PersonDetailsFragmentVM.Event.Loading -> {
                        //
                    }
                    is PersonDetailsFragmentVM.Event.Failure -> {
                    }
                    is PersonDetailsFragmentVM.Event.FilmographySuccess -> {
                        adapter.submitList(event.movie)
                    }
                }

            }
        }

    }
}
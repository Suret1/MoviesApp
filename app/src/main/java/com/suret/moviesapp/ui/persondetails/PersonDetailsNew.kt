package com.suret.moviesapp.ui.persondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ActorModel
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.data.other.Constants.IMAGE_URL
import com.suret.moviesapp.databinding.FragmentPersonDetailsNewBinding
import com.suret.moviesapp.ui.persondetails.adapter.FilmographyAdapter
import com.suret.moviesapp.ui.persondetails.viewmodel.PersonDetailsVM
import com.suret.moviesapp.util.Util.downloadImage
import com.suret.moviesapp.util.Util.hideSystemUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PersonDetailsNew : Fragment() {
    private val binding by lazy { FragmentPersonDetailsNewBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<PersonDetailsVM>()
    private val args by navArgs<PersonDetailsNewArgs>()
    private val adapter = FilmographyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        initAdapter()
        sendRequest()
        hideSystemUI(requireActivity())
        initObservers()
        onBack()
        return binding.root
    }

    private fun initAdapter() {
        binding.rvFilmography.adapter = adapter
    }

    private fun sendRequest() {
        args.castModel?.id?.let { personId ->
            viewModel.getPersonMovieCredits(personId)
            viewModel.getPersonData(personId)
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.movieFlow.collect { event ->
                when (event) {
                    is PersonDetailsVM.Event.FilmographySuccess -> {
                        adapter.submitList(event.movie)
                    }
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.actorFlow.collect { event ->
                when (event) {
                    is PersonDetailsVM.Event.ActorSuccess -> {
                        event.actor?.let { it1 -> setPersonData(it1) }
                    }
                    is PersonDetailsVM.Event.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            event.errorText,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun onBack() {
        binding.toolbarDetails.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setPersonData(actor: ActorModel) {
        binding.apply {
            if (actor.profile_path.isNullOrEmpty().not()) {
                downloadImage(
                    actorPoster, "$IMAGE_URL${actor.profile_path}",
                    progressBar
                )
                downloadImage(
                    actorProfile,
                    "$IMAGE_URL${actor.profile_path}",
                    progressBar
                )
            } else {
                progressBar.isVisible = false
                actorPoster.setImageResource(R.drawable.ic_round_person_24)
            }
            actorNameTV.text = actor.name
            when {
                actor.deathday != null -> {
                    actorBirthday.text = "${actor.birthday} - ${actor.deathday}"
                }
                actor.birthday != null -> {
                    actorBirthday.text = "${actor.birthday}"
                }
                else -> {
                    actorBirthday.text = ""
                }
            }
            actorBirthPlace.text = actor.place_of_birth
            actorDepartment.text = actor.known_for_department
            actorBio.text = actor.biography
        }
    }
}
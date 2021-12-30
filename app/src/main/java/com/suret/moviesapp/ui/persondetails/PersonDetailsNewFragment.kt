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
import com.suret.moviesapp.databinding.FragmentPersonDetailsNewBinding
import com.suret.moviesapp.ui.persondetails.adapter.FilmographyAdapter
import com.suret.moviesapp.ui.persondetails.viewmodel.PersonDetailsFragmentVM
import com.suret.moviesapp.util.Util.downloadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PersonDetailsNewFragment : Fragment() {
    private val binding by lazy { FragmentPersonDetailsNewBinding.inflate(layoutInflater) }
    private val viewModel: PersonDetailsFragmentVM by viewModels()
    private val args by navArgs<PersonDetailsNewFragmentArgs>()

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

        val castModel = args.castModel

        binding.actorPoster.setImageResource(R.drawable.robert_downey)

        binding.rvFilmography.adapter = adapter

        args.castModel?.id?.let { id ->
            viewModel.getPersonMovieCredits(id)
        }

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
        castModel?.let {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                it.id?.let { personId ->
                    viewModel.getPersonData(personId)
                    viewModel.actorFlow.collect { event ->
                        when (event) {
                            is PersonDetailsFragmentVM.Event.ActorSuccess -> {
                                event.actor?.let { it1 -> setPersonData(it1) }
                            }
                            is PersonDetailsFragmentVM.Event.Failure -> {
                                Toast.makeText(
                                    requireContext(),
                                    event.errorText,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is PersonDetailsFragmentVM.Event.Loading -> {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setPersonData(actor: ActorModel) {
        binding.apply {
            if (actor.profile_path.isNullOrEmpty().not()) {
                downloadImage(
                    actorPoster,
                    Constants.IMAGE_URL + actor.profile_path,
                    progressBar
                )
                downloadImage(
                    actorProfile,
                    Constants.IMAGE_URL + actor.profile_path,
                    progressBar
                )
            } else {
                progressBar.isVisible = false
                actorPoster.setImageResource(R.drawable.ic_round_person_24)
            }
            actorNameTV.text = actor.name
//            tvTitleActorName.text = actor.name
//            tvActorName.text = actor.name
//            when {
//                actor.deathday != null -> {
//                    tvPersonBirthday.text = "(${actor.birthday} - ${actor.deathday})"
//                }
//                actor.birthday != null -> {
//                    tvPersonBirthday.text = "(${actor.birthday})"
//                }
//                else -> {
//                    tvPersonBirthday.text = ""
//                }
//            }
//            tvBirthPlace.text = actor.place_of_birth
//            tvPersonDepartment.text = actor.known_for_department
//            tvBio.text = actor.biography
        }
    }
}
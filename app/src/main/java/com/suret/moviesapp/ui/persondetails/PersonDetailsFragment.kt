package com.suret.moviesapp.ui.persondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ActorModel
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.other.Constants.IMAGE_URL
import com.suret.moviesapp.databinding.FragmentPersonDetailsBinding
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel
import com.suret.moviesapp.util.downloadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PersonDetailsFragment : Fragment() {
    private val binding by lazy { FragmentPersonDetailsBinding.inflate(layoutInflater) }
    private var castModel: Cast? = null
    private val movieViewModel: MovieViewModel by viewModels()
    private val args: PersonDetailsFragmentArgs by navArgs()
    private val animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_left_title
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        castModel = args.castModel

        castModel?.let {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                it.id?.let { personId ->
                    movieViewModel.getPersonData(personId)
                    movieViewModel.actorFlow.collect { event ->
                        when (event) {
                            is MovieViewModel.Event.ActorSuccess -> {
                                event.actor?.let { it1 -> setPersonData(it1) }
                            }
                            is MovieViewModel.Event.Failure -> {
                                Toast.makeText(
                                    requireContext(),
                                    event.errorText,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is MovieViewModel.Event.Loading -> {
                                //
                            }
                        }
                    }
                }
            }
        }
        binding.apply {
            tvTitleActorName.animation = animation
            personToolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setPersonData(actor: ActorModel) {
        binding.apply {
            if (!actor.profile_path.isNullOrEmpty()) {
                downloadImage(iwPersonPhoto, IMAGE_URL + actor.profile_path, progressBar)
            } else {
                progressBar.isVisible = false
                iwPersonPhoto.setImageResource(R.drawable.ic_round_person_24)
            }
            tvTitleActorName.text = actor.name
            tvActorName.text = actor.name
            when {
                actor.deathday != null -> {
                    tvPersonBirthday.text = "(${actor.birthday} - ${actor.deathday})"
                }
                actor.birthday != null -> {
                    tvPersonBirthday.text = "(${actor.birthday})"
                }
                else -> {
                    tvPersonBirthday.text = " "
                }
            }
            tvBirthPlace.text = actor.place_of_birth
            tvPersonDepartment.text = actor.known_for_department
            tvBio.text = actor.biography
        }
    }
}
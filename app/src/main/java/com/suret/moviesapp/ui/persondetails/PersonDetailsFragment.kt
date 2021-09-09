package com.suret.moviesapp.ui.persondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ActorModel
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.other.Constants.IMAGE_URL
import com.suret.moviesapp.databinding.FragmentPersonDetailsBinding
import com.suret.moviesapp.ui.movies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PersonDetailsFragment : Fragment() {
    private var _binding: FragmentPersonDetailsBinding? = null
    private val binding get() = _binding!!
    private var castModel: Cast? = null
    private val movieViewModel: MovieViewModel by viewModels()
    private val args: PersonDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonDetailsBinding.inflate(inflater, container, false)
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
            personToolbar.setNavigationIcon(R.drawable.back_btn)
            personToolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setPersonData(actor: ActorModel) {
        binding.apply {
            iwPersonPhoto.load(IMAGE_URL + actor.profile_path)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.suret.moviesapp.ui.persondetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suret.moviesapp.R
import com.suret.moviesapp.databinding.FragmentPersonDetailsNewBinding

class PersonDetailsNewFragment : Fragment() {
    private val binding by lazy { FragmentPersonDetailsNewBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.actorPoster.setImageResource(R.drawable.robert_downey)
    }
}
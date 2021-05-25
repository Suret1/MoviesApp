package com.suret.moviesapp.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.suret.moviesapp.databinding.FragmentFavouritesBinding


class FavouritesFragment : Fragment() {
    private lateinit var favouritesBinding: FragmentFavouritesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return favouritesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
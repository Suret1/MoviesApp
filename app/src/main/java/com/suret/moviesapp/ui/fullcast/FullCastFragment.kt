package com.suret.moviesapp.ui.fullcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.other.Constants.FULL_CAST_TYPE
import com.suret.moviesapp.databinding.FragmentFullCastBinding
import com.suret.moviesapp.ui.moviedetails.adapters.FullCastAdapter

class FullCastFragment : Fragment() {
    private val binding by lazy { FragmentFullCastBinding.inflate(layoutInflater) }
    private lateinit var castAdapter: FullCastAdapter
    private val args: FullCastFragmentArgs by navArgs()

    private val animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_left_title
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val castList = args.castList

        binding.apply {
            tvTitle.animation = animation

            castToolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            castList?.let {
                castAdapter.submitList(it as List<Cast>)
            }

            castAdapter.onItemClick = {
                findNavController().navigate(
                    FullCastFragmentDirections.actionFullCastFragmentToPersonDetailsFragment(it)
                )
            }
        }
    }

    private fun initAdapter() {
        castAdapter = FullCastAdapter()
        castAdapter.sendTypeCast(FULL_CAST_TYPE)
        binding.rvFullCast.adapter = castAdapter
    }

}
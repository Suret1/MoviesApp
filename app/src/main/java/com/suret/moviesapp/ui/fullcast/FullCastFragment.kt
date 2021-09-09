package com.suret.moviesapp.ui.fullcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.other.Constants.FULL_CAST_TYPE
import com.suret.moviesapp.databinding.FragmentFullCastBinding
import com.suret.moviesapp.ui.moviedetails.adapters.FullCastAdapter


class FullCastFragment : Fragment() {
    private var _binding: FragmentFullCastBinding? = null
    private lateinit var castAdapter: FullCastAdapter
    private val binding get() = _binding!!
    private val args: FullCastFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullCastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val castList = args.castList

        castAdapter = FullCastAdapter()
        castAdapter.sendTypeCast(FULL_CAST_TYPE)

        binding.apply {
            castToolbar.setNavigationIcon(R.drawable.back_btn)

            castToolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            rvFullCast.adapter = castAdapter

            castList?.let {
                castAdapter.differ.submitList(it as List<Cast>)
            }

            castAdapter.setOnItemClickListener {
                findNavController().navigate(
                    FullCastFragmentDirections.actionFullCastFragmentToPersonDetailsFragment(it)
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
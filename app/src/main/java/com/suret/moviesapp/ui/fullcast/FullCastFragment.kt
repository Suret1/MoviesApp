package com.suret.moviesapp.ui.fullcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.other.Constants.CAST_LIST
import com.suret.moviesapp.data.other.Constants.CAST_MODEL
import com.suret.moviesapp.data.other.Constants.FULL_CAST_TYPE
import com.suret.moviesapp.databinding.FragmentFullCastBinding
import com.suret.moviesapp.ui.moviedetails.adapters.FullCastAdapter


class FullCastFragment : Fragment() {
    private lateinit var fullCastBinding: FragmentFullCastBinding
    private var castString: String? = null
    private lateinit var castAdapter: FullCastAdapter
    private var bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fullCastBinding = FragmentFullCastBinding.inflate(inflater, container, false)
        return fullCastBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        castString = arguments?.getString(CAST_LIST)

        val turnsType = object : TypeToken<List<Cast>>() {}.type
        val turns = Gson().fromJson<List<Cast>>(castString, turnsType)

        castAdapter = FullCastAdapter()
        castAdapter.sendTypeCast(FULL_CAST_TYPE)

        fullCastBinding.apply {
            castToolbar.setNavigationIcon(R.drawable.back_btn)

            castToolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            rvFullCast.adapter = castAdapter

            castAdapter.differ.submitList(turns)

            castAdapter.setOnItemClickListener {
                bundle.apply {
                    putParcelable(CAST_MODEL, it)
                }
                findNavController().navigate(R.id.action_to_personDetailsFragment, bundle)
            }
        }

    }

}
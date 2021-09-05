package com.suret.moviesapp.ui.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.suret.moviesapp.data.repository.datasourceimpl.SimilarMoviesPagingDataSource
import com.suret.moviesapp.databinding.FragmentSimilarBinding
import com.suret.moviesapp.ui.similar.adapter.SimilarPagingAdapter
import com.suret.moviesapp.ui.similar.viewmodel.SimilarViewModel
import com.suret.moviesapp.util.PopUps
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SimilarFragment : Fragment() {
    private lateinit var binding: FragmentSimilarBinding
    private lateinit var similarPagingAdapter: SimilarPagingAdapter
    private val args: SimilarFragmentArgs by navArgs()
    private val similarViewModel: SimilarViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSimilarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = PopUps.progressDialog(requireActivity())
        similarPagingAdapter = SimilarPagingAdapter()

        binding.apply {
            rvSimilar.adapter = similarPagingAdapter
            similarToolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        val id = args.movieID

        SimilarMoviesPagingDataSource.ID = id

        lifecycleScope.launch {
            similarViewModel.listData.collect {
                similarPagingAdapter.submitData(lifecycle, it)
            }
        }

        similarPagingAdapter.addLoadStateListener { combinedLoadStates ->
            when (combinedLoadStates.source.refresh) {
                is LoadState.NotLoading -> {
                    progressBar.dismiss()
                }
                is LoadState.Loading -> {
                    progressBar.show()
                }
                is LoadState.Error -> {
//                    Toast.makeText(
//                        requireContext(),
//                        loadState.error.localizedMessage,
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }


    }


}
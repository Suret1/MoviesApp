package com.suret.moviesapp.ui.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
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
    private var _binding: FragmentSimilarBinding? = null
    private lateinit var similarPagingAdapter: SimilarPagingAdapter
    private val binding get() = _binding!!
    private val args: SimilarFragmentArgs by navArgs()
    private val similarViewModel: SimilarViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSimilarBinding.inflate(inflater, container, false)
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
            hideAndShowFloatingActionButton()
            fabArrow.setOnClickListener {
               rvSimilar.smoothScrollToPosition(0)
            }

        }
        similarPagingAdapter.setOnClickListener {
            findNavController().navigate(
                SimilarFragmentDirections.actionSimilarFragmentToMovieDetailsFragment(
                    it,
                    null
                )
            )
        }

        SimilarMoviesPagingDataSource.ID = args.movieID

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
                    progressBar.dismiss()
                }
            }
        }


    }

    private fun FragmentSimilarBinding.hideAndShowFloatingActionButton() {
        rvSimilar.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fabArrow.isShown) {
                    fabArrow.hide()
                }
            }

            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fabArrow.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
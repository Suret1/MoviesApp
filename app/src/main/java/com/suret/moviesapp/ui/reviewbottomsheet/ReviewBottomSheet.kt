package com.suret.moviesapp.ui.reviewbottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ReviewResult
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.databinding.FragmentReviewBottomSheetBinding
import com.suret.moviesapp.util.AppUtil.toDate

class ReviewBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentReviewBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val args: ReviewBottomSheetArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReviewBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = args.reviewModel

        binding.apply {
            setData(model)
        }

    }

    private fun FragmentReviewBottomSheetBinding.setData(review: ReviewResult?) {
        review?.let { r ->
            tvProfileName.text = r.author
            val path = r.author_details?.avatar_path
            if (path != null) {
                if (path.startsWith("/https:", true)) {
                    iwProfile.load(
                        path.removePrefix("/")
                    ) {
                        crossfade(true)
                        error(R.drawable.ic_round_person_24)
                    }
                } else {
                    iwProfile.load(
                        Constants.IMAGE_URL + path
                    ) {
                        crossfade(true)
                        error(R.drawable.ic_round_person_24)
                    }
                }

            } else {
                iwProfile.load(R.drawable.ic_round_person_24)
            }
            if (r.author_details?.rating != null) {
                tvRate.text = r.author_details.rating.toString()
            }
            tvDate.text = toDate(r.created_at)
            tvReview.text = r.content
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.suret.moviesapp.ui.reviewbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ReviewResult
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.data.other.Constants.IMAGE_URL
import com.suret.moviesapp.databinding.FragmentReviewBottomSheetBinding
import com.suret.moviesapp.util.Util.downloadImage
import com.suret.moviesapp.util.Util.toDate

class ReviewBottomSheet : BottomSheetDialogFragment() {
    private val binding by lazy { FragmentReviewBottomSheetBinding.inflate(layoutInflater) }
    private val args by navArgs<ReviewBottomSheetArgs>()

    private val reviewModel by lazy {
        args.reviewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            setData(reviewModel)
        }

    }

    private fun FragmentReviewBottomSheetBinding.setData(review: ReviewResult?) {
        review?.let { r ->
            tvProfileName.text = r.author
            val path = r.author_details?.avatar_path
            if (!path.isNullOrEmpty()) {
                if (path.startsWith("/https:", true)) {
                    downloadImage(iwProfile, path.removePrefix("/"), progressBar)
                } else {
                    downloadImage(iwProfile, "$IMAGE_URL${path}", progressBar)
                }
            } else {
                progressBar.isVisible = false
                iwProfile.setImageResource(R.drawable.ic_round_person_24)
            }
            if (r.author_details?.rating != null) {
                tvRate.text = r.author_details.rating.toString()
            }
            tvDate.text = toDate(r.created_at)
            tvReview.text = r.content
        }

    }
}
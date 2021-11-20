package com.suret.moviesapp.ui.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ReviewResult
import com.suret.moviesapp.databinding.ReviewListLayoutBinding
import com.suret.moviesapp.ui.moviedetails.adapters.ReviewAdapter.ReviewHolder


class ReviewAdapter : ListAdapter<ReviewResult, ReviewHolder>(DifferCallBack) {

    var onItemClick: ((ReviewResult) -> Unit)? = null

    inner class ReviewHolder(
        private val binding: ReviewListLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewResult: ReviewResult?) {

            reviewResult?.let { review ->

//                    tvReview.movementMethod = object : ScrollingMovementMethod() {}
//                    val listener = View.OnTouchListener { v, event ->
//                        val isLarger: Boolean =
//                            (v as AppCompatTextView).lineCount * v.lineHeight > v.getHeight()
//                        if (event.action === MotionEvent.ACTION_MOVE && isLarger) {
//                            v.getParent().requestDisallowInterceptTouchEvent(true)
//                        } else {
//                            v.getParent().requestDisallowInterceptTouchEvent(false)
//                        }
//                        false
//                    }
//                    this.tvReview.setOnTouchListener(listener)

                binding.model = review

                binding.root.setOnClickListener {
                    review.let { r ->
                        onItemClick?.invoke(r)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        return ReviewHolder(
            ReviewListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_alpha_anim)
    }

    private object DifferCallBack : DiffUtil.ItemCallback<ReviewResult>() {
        override fun areItemsTheSame(
            oldItem: ReviewResult,
            newItem: ReviewResult
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReviewResult,
            newItem: ReviewResult
        ): Boolean {
            return oldItem == newItem
        }
    }

}
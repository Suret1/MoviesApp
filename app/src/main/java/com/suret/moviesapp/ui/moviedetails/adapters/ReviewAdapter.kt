package com.suret.moviesapp.ui.moviedetails.adapters

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ReviewResult
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.databinding.ReviewListLayoutBinding
import com.suret.moviesapp.ui.moviedetails.adapters.ReviewAdapter.ReviewHolder
import com.suret.moviesapp.util.AppUtil.toDate


class ReviewAdapter : RecyclerView.Adapter<ReviewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<ReviewResult>() {
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

    val differ = AsyncListDiffer(this, differCallBack)

    inner class ReviewHolder(
        private val reviewListLayoutBinding: ReviewListLayoutBinding
    ) : RecyclerView.ViewHolder(reviewListLayoutBinding.root) {

        fun bind(reviewResult: ReviewResult?) {
            reviewListLayoutBinding.apply {
                reviewResult?.let { review ->
                    tvProfileName.text = review.author
                    val path = review.author_details?.avatar_path
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
                    if (review.author_details?.rating != null) {
                        tvRate.text = review.author_details.rating.toString()
                    }
                    tvDate.text = toDate(review.created_at)
                    tvReview.text = review.content

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

                    root.setOnClickListener {
                        review.let { r ->
                            onItemClickListener?.invoke(r)
                        }
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

    private var onItemClickListener: ((ReviewResult) -> Unit)? = null

    fun setOnItemClickListener(listener: (ReviewResult) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bind(differ.currentList.getOrNull(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_alpha_anim)
    }

    override fun getItemCount(): Int = differ.currentList.size
}
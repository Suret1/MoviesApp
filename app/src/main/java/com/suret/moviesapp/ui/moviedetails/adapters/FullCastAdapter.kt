package com.suret.moviesapp.ui.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.databinding.CastListLayoutBinding
import com.suret.moviesapp.databinding.FullCastListBinding

class FullCastAdapter : ListAdapter<Cast, RecyclerView.ViewHolder>(DifferCallBack) {

    var onItemClick: ((Cast) -> Unit)? = null

    var castType: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (castType) {
            0 -> {
                CastViewHolder(
                    CastListLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                FullCastViewHolder(
                    FullCastListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CastViewHolder) {
            holder.bind(getItem(position))
            holder.itemView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_alpha_anim)

        } else if (holder is FullCastViewHolder) {
            holder.bindfull(getItem(position))
            holder.itemView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_one)
        }

    }

    inner class CastViewHolder(private val binding: CastListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Cast) {

            model.let { cast ->

                binding.cast = cast

                binding.root.setOnClickListener {
                    onItemClick?.invoke(cast)
                }

            }

        }
    }

    inner class FullCastViewHolder(private val bindingFull: FullCastListBinding) :
        RecyclerView.ViewHolder(bindingFull.root) {

        fun bindfull(model: Cast) {

            model.let { cast ->
                bindingFull.cast = cast

                bindingFull.root.setOnClickListener {
                    onItemClick?.invoke(cast)
                }
            }
        }
    }

    fun sendTypeCast(type: Int) {
        castType = type
    }

    private object DifferCallBack : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }
}
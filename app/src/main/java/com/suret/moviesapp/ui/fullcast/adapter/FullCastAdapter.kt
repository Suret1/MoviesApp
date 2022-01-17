package com.suret.moviesapp.ui.fullcast.adapter

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast

class FullCastAdapter : ListAdapter<Cast, RecyclerView.ViewHolder>(DifferCallBack) {

    var onItemClick: ((Cast) -> Unit)? = null

    var castType: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (castType) {
            0 -> {
                CastVH.from(parent)
            }
            else -> {
                FullCastVH.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CastVH -> {
                holder.bind(getItem(position), onItemClick)
                holder.itemView.animation = AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.recycler_alpha_anim
                )
            }
            is FullCastVH -> {
                holder.bind(getItem(position), onItemClick)
                holder.itemView.animation = AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.recycler_alpha_anim
                )
            }
        }
    }

    fun sendTypeCast(type: Int) {
        castType = type
    }

    private object DifferCallBack : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast) = oldItem == newItem
    }
}
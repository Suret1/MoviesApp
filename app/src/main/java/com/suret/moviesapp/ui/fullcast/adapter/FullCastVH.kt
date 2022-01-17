package com.suret.moviesapp.ui.fullcast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.databinding.FullCastListBinding

class FullCastVH(val binding: FullCastListBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        model: Cast,
        onItemClick: ((Cast) -> Unit)? = null
    ) {
        model.let { cast ->
            binding.cast = cast

            binding.root.setOnClickListener {
                onItemClick?.invoke(cast)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup) = FullCastVH(
            FullCastListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}
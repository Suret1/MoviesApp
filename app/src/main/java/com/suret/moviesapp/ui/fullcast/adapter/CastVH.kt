package com.suret.moviesapp.ui.fullcast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.databinding.CastListLayoutBinding

class CastVH(val binding: CastListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    private val banner: ImageView = binding.iwPersonPhoto
    private val title: TextView = binding.tvPersonName

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
        fun from(parent: ViewGroup) = CastVH(
            CastListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}
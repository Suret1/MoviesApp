package com.suret.moviesapp.ui.moviedetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.Cast
import com.suret.moviesapp.data.other.Constants

class FullCastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var castType: Int? = null

    private val differCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (castType) {
            0 -> {
                CastViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.cast_list_layout, parent, false)
                )
            }
            else -> {
                FullCastViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.full_cast_list, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CastViewHolder) {
            holder.iwPhoto.load(Constants.IMAGE_URL + differ.currentList.getOrNull(position)?.profile_path) {
                crossfade(true)
                error(R.drawable.ic_round_person_24)
            }
            holder.tvPersonName.text = differ.currentList.getOrNull(position)?.name
            if (!differ.currentList.getOrNull(position)?.character.isNullOrEmpty()) {
                holder.tvCharacterName.text =
                    "(" + differ.currentList.getOrNull(position)?.character + ")"
            }
        } else if (holder is FullCastViewHolder) {
            holder.iwPhoto.load(Constants.IMAGE_URL + differ.currentList.getOrNull(position)?.profile_path) {
                crossfade(true)
                error(R.drawable.ic_round_person_24)
            }
            holder.tvPersonName.text = differ.currentList.getOrNull(position)?.name
            holder.tvCharacterName.text = differ.currentList.getOrNull(position)?.character
            holder.tvDepartment.text = differ.currentList.getOrNull(position)?.known_for_department
        }
        holder.itemView.setOnClickListener {
            differ.currentList.getOrNull(position)?.let {
                onItemClickListener?.invoke(it)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iwPhoto: AppCompatImageView = itemView.findViewById(R.id.iwPersonPhoto)
        val tvPersonName: AppCompatTextView = itemView.findViewById(R.id.tvPersonName)
        val tvCharacterName: AppCompatTextView = itemView.findViewById(R.id.tvCharacterName)
    }

    inner class FullCastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iwPhoto: AppCompatImageView = itemView.findViewById(R.id.iwPersonPhoto)
        val tvPersonName: AppCompatTextView = itemView.findViewById(R.id.tvPersonName)
        val tvCharacterName: AppCompatTextView = itemView.findViewById(R.id.tvCharacterName)
        val tvDepartment: AppCompatTextView = itemView.findViewById(R.id.tvDepartment)
    }

    private var onItemClickListener: ((Cast) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cast) -> Unit) {
        onItemClickListener = listener
    }

    fun sendTypeCast(type: Int) {
        castType = type
    }

}
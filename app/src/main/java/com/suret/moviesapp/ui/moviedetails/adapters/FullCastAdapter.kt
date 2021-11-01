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
//            holder.iwPhoto.load(Constants.IMAGE_URL + differ.currentList.getOrNull(position)?.profile_path) {
//                crossfade(true)
//                error(R.drawable.ic_round_person_24)
//            }
//            holder.tvPersonName.text = differ.currentList.getOrNull(position)?.name
//            if (!differ.currentList.getOrNull(position)?.character.isNullOrEmpty()) {
//                holder.tvCharacterName.text =
//                    "(" + differ.currentList.getOrNull(position)?.character + ")"
//            }
            holder.bind(getItem(position))

//            holder.itemView.animation =
//                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_alpha_anim)

        } else if (holder is FullCastViewHolder) {
//            holder.iwPhoto.load(Constants.IMAGE_URL + differ.currentList.getOrNull(position)?.profile_path) {
//                crossfade(true)
//                error(R.drawable.ic_round_person_24)
//            }
//            holder.tvPersonName.text = differ.currentList.getOrNull(position)?.name
//            holder.tvCharacterName.text = differ.currentList.getOrNull(position)?.character
//            holder.tvDepartment.text = differ.currentList.getOrNull(position)?.known_for_department
            holder.bindfull(getItem(position))
//            holder.itemView.animation =
//                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_rotate_anim)
        }
//        holder.itemView.setOnClickListener {
//            differ.currentList.getOrNull(position)?.let {
//                onItemClickListener?.invoke(it)
//            }
//        }
    }

    inner class CastViewHolder(val binding: CastListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        val iwPhoto: AppCompatImageView = itemView.findViewById(R.id.iwPersonPhoto)
//        val tvPersonName: AppCompatTextView = itemView.findViewById(R.id.tvPersonName)
//        val tvCharacterName: AppCompatTextView = itemView.findViewById(R.id.tvCharacterName)

        fun bind(model: Cast) {
            binding.apply {
                binding.cast = currentList[bindingAdapterPosition]

                root.setOnClickListener {
                    onItemClickListener?.invoke(model)
                }

            }
        }
    }

    inner class FullCastViewHolder(val bindingFull: FullCastListBinding) :
        RecyclerView.ViewHolder(bindingFull.root) {
//        val iwPhoto: AppCompatImageView = itemView.findViewById(R.id.iwPersonPhoto)
//        val tvPersonName: AppCompatTextView = itemView.findViewById(R.id.tvPersonName)
//        val tvCharacterName: AppCompatTextView = itemView.findViewById(R.id.tvCharacterName)
//        val tvDepartment: AppCompatTextView = itemView.findViewById(R.id.tvDepartment)

        fun bindfull(model: Cast) {
            bindingFull.apply {

                bindingFull.cast = currentList[bindingAdapterPosition]

                root.setOnClickListener {
                    onItemClickListener?.invoke(model)
                }

            }
        }
    }

    private var onItemClickListener: ((Cast) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cast) -> Unit) {
        onItemClickListener = listener
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
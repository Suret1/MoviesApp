package com.suret.moviesapp.ui.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ProductionCompany
import com.suret.moviesapp.databinding.ProductionListLayoutBinding
import com.suret.moviesapp.ui.moviedetails.adapters.ProductionsAdapter.ProductionViewHolder

class ProductionsAdapter :
    ListAdapter<ProductionCompany, ProductionViewHolder>(DifferCallBack) {

    inner class ProductionViewHolder(
        private val binding: ProductionListLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(productions: ProductionCompany?) {
            productions.let { model ->
                binding.model = model
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val productionListLayoutBinding =
            ProductionListLayoutBinding.inflate(layoutInflater, parent, false)
        return ProductionViewHolder(
            productionListLayoutBinding
        )

    }

    override fun onBindViewHolder(holder: ProductionViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_alpha_anim)
    }

    private object DifferCallBack : DiffUtil.ItemCallback<ProductionCompany>() {
        override fun areItemsTheSame(
            oldItem: ProductionCompany,
            newItem: ProductionCompany
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductionCompany,
            newItem: ProductionCompany
        ): Boolean {
            return oldItem == newItem
        }

    }
}
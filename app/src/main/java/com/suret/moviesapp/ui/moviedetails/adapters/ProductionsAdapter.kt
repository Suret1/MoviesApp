package com.suret.moviesapp.ui.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.suret.moviesapp.R
import com.suret.moviesapp.data.model.ProductionCompany
import com.suret.moviesapp.data.other.Constants
import com.suret.moviesapp.databinding.ProductionListLayoutBinding

class ProductionsAdapter :
    RecyclerView.Adapter<ProductionsAdapter.ProductionViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<ProductionCompany>() {
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

    val differ = AsyncListDiffer(this, differCallBack)


    inner class ProductionViewHolder(
        private val productionListLayoutBinding: ProductionListLayoutBinding
    ) :
        RecyclerView.ViewHolder(productionListLayoutBinding.root) {

        fun bind(productions: ProductionCompany?) {
            productions?.let { model ->
                productionListLayoutBinding.apply {
                    if (!model.name.isNullOrEmpty()) {
                        tvProductionName.text = model.name
                    }
                    iwProductionLogo.load(
                        Constants.IMAGE_URL +
                                differ.currentList.getOrNull(bindingAdapterPosition)?.logo_path
                    ) {
                        crossfade(true)
                        error(R.drawable.no_photo)
                    }

                }
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
        holder.bind(differ.currentList.getOrNull(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_alpha_anim)
    }

    override fun getItemCount(): Int = differ.currentList.size

}
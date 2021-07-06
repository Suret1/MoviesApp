package com.suret.moviesapp.ui.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suret.moviesapp.databinding.CastListLayoutBinding

class CastListAdapter : RecyclerView.Adapter<CastListAdapter.CastViewHolder>() {


    inner class CastViewHolder(private val castListLayoutBinding: CastListLayoutBinding) :
        RecyclerView.ViewHolder(castListLayoutBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val castListLayoutBinding = CastListLayoutBinding.inflate(layoutInflater)
        return CastViewHolder(castListLayoutBinding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
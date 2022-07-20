package com.example.furniture.presentation.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furniture.R
import com.example.furniture.data.model.home.Offer
import com.example.furniture.databinding.FragmentHomeOfferSaveRecyclerViewItemBinding

class CustomHolderOffersSaves :
    RecyclerView.Adapter<CustomHolderOffersSaves.MyViewHolder>() {

    private lateinit var context: Context

    private val callback = object : DiffUtil.ItemCallback<Offer>() {
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FragmentHomeOfferSaveRecyclerViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_home_offer_save_recycler_view_item,
            parent,
            false
        )
        context = parent.context
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener: ((Offer) -> Unit)? = null

    fun setOnItemClickListener(
        listener: (Offer) -> Unit
    ) {
        onItemClickListener = listener
    }

    inner class MyViewHolder(private val binding: FragmentHomeOfferSaveRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Offer) {
            binding.item = data
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(data)
                }
            }
        }
    }
}



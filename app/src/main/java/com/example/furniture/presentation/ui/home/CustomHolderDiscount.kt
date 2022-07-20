package com.example.furniture.presentation.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.furniture.R
import com.example.furniture.data.model.home.Discount
import com.example.furniture.databinding.FragmentHomeDiscountRecyclerViewItemBinding

class CustomHolderDiscount :
    RecyclerView.Adapter<CustomHolderDiscount.MyViewHolder>() {

    private lateinit var context: Context

    private val callback = object : DiffUtil.ItemCallback<Discount>() {
        override fun areItemsTheSame(oldItem: Discount, newItem: Discount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Discount, newItem: Discount): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FragmentHomeDiscountRecyclerViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_home_discount_recycler_view_item,
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


    private var onItemClickListener: ((Discount) -> Unit)? = null

    fun setOnItemClickListener(
        listener: (Discount) -> Unit
    ) {
        onItemClickListener = listener
    }

    inner class MyViewHolder(private val binding: FragmentHomeDiscountRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Discount) {
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



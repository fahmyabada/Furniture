package com.example.furniture.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.furniture.R
import com.example.furniture.data.model.home.Category
import com.example.furniture.databinding.FragmentHomeCategoryRecyclerViewItemBinding

class CustomHolderCategory :
    RecyclerView.Adapter<CustomHolderCategory.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FragmentHomeCategoryRecyclerViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_home_category_recycler_view_item,
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener: ((Category) -> Unit)? = null

    fun setOnItemClickListener(
        listener: (Category) -> Unit
    ) {
        onItemClickListener = listener
    }

    inner class MyViewHolder(private val binding: FragmentHomeCategoryRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Category) {
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



package com.example.furniture.presentation.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.furniture.R
import com.example.furniture.data.model.home.Save
import com.example.furniture.databinding.FragmentHomeSavesRecyclerViewItemBinding

class CustomHolderSaves :
    RecyclerView.Adapter<CustomHolderSaves.MyViewHolder>() {

    private lateinit var context: Context

    private val callback = object : DiffUtil.ItemCallback<Save>() {
        override fun areItemsTheSame(oldItem: Save, newItem: Save): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Save, newItem: Save): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FragmentHomeSavesRecyclerViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_home_saves_recycler_view_item,
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


    private var onItemClickListener: ((Save) -> Unit)? = null

    fun setOnItemClickListener(
        listener: (Save) -> Unit
    ) {
        onItemClickListener = listener
    }

    inner class MyViewHolder(private val binding: FragmentHomeSavesRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Save) {
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



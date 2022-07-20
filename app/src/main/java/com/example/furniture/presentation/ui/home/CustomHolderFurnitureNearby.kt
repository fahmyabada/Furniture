package com.example.furniture.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.furniture.R
import com.example.furniture.data.model.furniture_nearby.Data
import com.example.furniture.databinding.FragmentHomeNearbyRecyclerViewItemBinding

class CustomHolderFurnitureNearby :
    RecyclerView.Adapter<CustomHolderFurnitureNearby.MyViewHolder>() {

    private var item: MutableList<Data> = ArrayList()
    private var oldItem: MutableList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FragmentHomeNearbyRecyclerViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_home_nearby_recycler_view_item,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int {
        return if (item.isEmpty()) 0 else item.size
    }

    fun setData(newItem: List<Data>) {
        oldItem.clear()
        oldItem.addAll(item)
        item.clear()
        item.addAll(newItem)
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(HomeFurnitureNearbyDiffCallback(oldItem, item))
        result.dispatchUpdatesTo(this)
    }

    private var onItemClickListener: ((Data) -> Unit)? = null

    fun setOnItemClickListener(
        listener: (Data) -> Unit
    ) {
        onItemClickListener = listener
    }

    inner class MyViewHolder(private val binding: FragmentHomeNearbyRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Data) {
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



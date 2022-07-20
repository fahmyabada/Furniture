package com.example.furniture.presentation.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.example.furniture.data.model.furniture_nearby.Data

class HomeFurnitureNearbyDiffCallback(private val oldItem : List<Data>, private val newItem : List<Data>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition].id == newItem[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition] == newItem[newItemPosition]
    }
}
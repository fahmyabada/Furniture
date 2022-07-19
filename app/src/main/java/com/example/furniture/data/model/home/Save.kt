package com.example.furniture.data.model.home

data class Save(
    val diff_day: Int,
    val end: String,
    val from: String,
    val furniture_id: Int,
    val furniture_logo: String,
    val furniture_name: String,
    val hours: Int,
    val id: Int,
    val images: List<Image>,
    val minutes: Int,
    val model_type: String,
    val name: String,
    val price: Int,
    val products: List<Product>,
    val qty_cart: String,
    val rate: Int,
    val rate_count: Int,
    val save_id: Int,
    val start: String,
    val to: String,
    val video: String
)
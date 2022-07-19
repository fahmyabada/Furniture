package com.example.furniture.data.model.home

data class Offer(
    val furniture_id: Int,
    val furniture_logo: String,
    val furniture_name: String,
    val id: Int,
    val images: List<Image>,
    val model_type: String,
    val name: String,
    val offer_id: Int,
    val price: Int,
    val products: List<Product>,
    val qty_cart: String,
    val rate: Int,
    val rate_count: Int,
    val video: Any
)
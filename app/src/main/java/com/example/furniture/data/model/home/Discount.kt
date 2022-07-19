package com.example.furniture.data.model.home

data class Discount(
    val discount_id: Int,
    val furniture_id: Int,
    val furniture_logo: String,
    val furniture_name: String,
    val id: Int,
    val images: List<Image>,
    val model_type: String,
    val percent: Int,
    val price_after: Int,
    val price_before: Int,
    val product_description: String,
    val product_name: String,
    val qty_cart: String,
    val rate: Int,
    val rate_count: Int
)
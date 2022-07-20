package com.example.furniture.data.model.furniture_nearby

data class FurnitureNearby(
    val `data`: List<Data>,
    val message: String,
    val paginator: Paginator,
    val status: Boolean
)
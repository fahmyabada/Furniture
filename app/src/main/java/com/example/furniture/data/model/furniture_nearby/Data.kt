package com.example.furniture.data.model.furniture_nearby

data class Data(
    val address: String,
    val branch_type_id: Int,
    val branch_type_name: String,
    val country: Country,
    val description: String,
    val distance: Int,
    val furniture_id: Int,
    val governorate: Governorate,
    val id: Int,
    val is_fav: Boolean,
    val lat: Any,
    val link_apple_store: String,
    val link_google_play: String,
    val lng: Any,
    val logo: String,
    val name: String,
    val `open`: Boolean,
    val qr_image: String,
    val rate: Int,
    val rate_count: Any,
    val region: Region
)
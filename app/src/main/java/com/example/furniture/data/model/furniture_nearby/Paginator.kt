package com.example.furniture.data.model.furniture_nearby

data class Paginator(
    val count: Int,
    val currentPage: Int,
    val hasMorePages: Boolean,
    val lastPage: Int,
    val nextPageUrl: Any,
    val previousPageUrl: Any,
    val total: Int
)
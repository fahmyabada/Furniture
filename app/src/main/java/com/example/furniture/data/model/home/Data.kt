package com.example.furniture.data.model.home

data class Data(
    val branch_type: List<BranchType>,
    val categories: List<Category>,
    val discounts: List<Discount>,
    val offers: List<Offer>,
    val saves: List<Save>
)
package ru.gozerov.database.sales

import kotlinx.serialization.Serializable

@Serializable
data class Sale(
    val id: Int,
    val title: String,
    val description: String,
    val url: String,
    val image: String
)

@Serializable
data class GetSalesResponse(
    val sales: List<Sale>
)
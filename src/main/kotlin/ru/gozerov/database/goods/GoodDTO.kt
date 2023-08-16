package ru.gozerov.database.goods

data class GoodDTO(
    val vendorCode: Int,
    val name: String,
    val description: String,
    val price: Int,
    val images: List<String>?
)
package ru.gozerov.features.goods

import kotlinx.serialization.Serializable


@Serializable
data class GoodRemote(
    val vendorCode: Int,
    val name: String,
    val description: String,
    val price: Int,
    val images: List<String>?
)

@Serializable
data class GetGoodsResponse(
    val goods: List<GoodRemote>
)

@Serializable
data class AddGoodRequest(
    val name: String,
    val description: String,
    val price: Int,
    val images: List<String>?
)
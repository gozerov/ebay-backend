package ru.gozerov.features.goods

import kotlinx.serialization.Serializable
import ru.gozerov.features.reviews.Review


@Serializable
data class GoodRemote(
    val vendorCode: Int,
    val name: String,
    val description: String,
    val price: Int,
    val images: List<String>?,
    val reviews: List<Review>? = null
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

@Serializable
data class AddGoodResponse(
    val isSuccessful: Boolean
)

@Serializable
data class GetGoodsSizeResponse(
    val count: Int
)

@Serializable
data class GetGoodsInPartsRequest(
    val index: Int
)

@Serializable
data class GetGoodsInPartsResponse(
    val goods: List<GoodRemote>
)

@Serializable
data class GetGoodByIdResponse(
    val good: GoodRemote
)
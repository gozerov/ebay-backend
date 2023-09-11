package ru.gozerov.features.reviews

import kotlinx.serialization.Serializable
import ru.gozerov.database.reviews.ReviewsDTO

@Serializable
data class AddReviewRequest(
    val userEmail: String,
    val goodId: Int,
    val rating: Double,
    val addedAgo: String?,
    val body: String?
)

@Serializable
data class Review(
    val id: Int,
    val userEmail: String,
    val goodId: Int,
    val rating: Double,
    val addedAgo: String?,
    val body: String?
)

fun AddReviewRequest.toDTO() = ReviewsDTO(
    id = 0,
    userEmail = userEmail,
    goodId = goodId,
    rating = rating,
    addedAgo = addedAgo,
    body = body
)

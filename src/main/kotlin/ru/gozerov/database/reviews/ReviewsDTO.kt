package ru.gozerov.database.reviews

data class ReviewsDTO(
    val id: Int,
    val userEmail: String,
    val goodId: Int,
    val rating: Double,
    val addedAgo: String?,
    val body: String?
)
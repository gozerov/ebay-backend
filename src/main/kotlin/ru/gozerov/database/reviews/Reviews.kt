package ru.gozerov.database.reviews

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.gozerov.features.reviews.Review

object Reviews : Table(name = "reviews") {

    private val id = Reviews.integer("id")
    private val userEmail = Reviews.varchar("user_email", 50)
    private val goodId = Reviews.integer("good_id")
    private val rating = Reviews.double("rating")
    private val addedAgo = Reviews.varchar("added_ago", 50).nullable()
    private val body = Reviews.varchar("body", 300).nullable()

    fun insert(reviewsDTO: ReviewsDTO) = transaction {
        Reviews.insert {
            it[userEmail] = reviewsDTO.userEmail
            it[goodId] = reviewsDTO.goodId
            it[rating] = reviewsDTO.rating
            it[addedAgo] = reviewsDTO.addedAgo
            it[body] = reviewsDTO.body
        }
    }

    fun getReviewsByGoodId(goodId: Int): List<ReviewsDTO> = transaction {
        return@transaction Reviews.select { Reviews.goodId.eq(goodId) }.mapNotNull {
            ReviewsDTO(
                id = it[this@Reviews.id],
                userEmail = it[this@Reviews.userEmail],
                goodId = it[this@Reviews.goodId],
                rating = it[rating],
                addedAgo = it[addedAgo],
                body = it[body]
            )
        }
    }

}

fun ReviewsDTO.toReview() = Review(
    id = id,
    userEmail = userEmail,
    goodId = goodId,
    rating = rating,
    addedAgo = addedAgo,
    body = body
)
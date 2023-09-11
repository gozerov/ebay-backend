package ru.gozerov.features.reviews

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.reviews.Reviews
import ru.gozerov.database.tokens.checkToken

class ReviewController(
    private val call: ApplicationCall
) {

    suspend fun addReview(token: String?, request: AddReviewRequest) {
        call.checkToken(token) {
            val rating = if (request.rating < 0.0) 0.0 else (if (request.rating > 5.0) 5.0 else request.rating)
            Reviews.insert(request.toDTO().copy(rating = rating))
            call.respond(HttpStatusCode.Created)
        }
    }

}
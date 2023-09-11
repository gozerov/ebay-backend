package ru.gozerov.features.reviews

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureReviewRouting() {
    routing {
        post("/reviews/add") {
            val request = call.receive(AddReviewRequest::class)
            val token = call.request.headers["Bearer-Authorization"]
            val controller = ReviewController(call)
            controller.addReview(token, request)
        }
    }
}
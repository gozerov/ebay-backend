package ru.gozerov.features.verification

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureVerificationRouting() {
    routing {
        post("/verification") {
            val requestBody = call.receive(VerificationCodeRequest::class)
            val controller = VerificationController(call)
            controller.checkVerificationCode(requestBody)
        }
        post("/verification/cancel") {
            val requestBody = call.receive(CancelVerificationRequest::class)
            val controller = VerificationController(call)
            controller.removeVerificationToken(requestBody.token)
        }
    }
}
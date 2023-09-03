package ru.gozerov.features.verification

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureVerificationRouting() {
    routing {
        post("/verification"){
            val requestBody = call.receive(VerificationCodeRequest::class)
            println(requestBody)
            val controller = VerificationController(call)
            controller.checkVerificationCode(requestBody)
        }
    }
}
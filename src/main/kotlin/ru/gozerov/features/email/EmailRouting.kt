package ru.gozerov.features.email

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureEmailRouting() {
    routing {
        post(path = "/reset") {
            val emailController = EmailController(call)
            val requestBody = call.receive(ResetEmailRequest::class)
            emailController.sendVerificationCode(requestBody.email)
        }
    }
}
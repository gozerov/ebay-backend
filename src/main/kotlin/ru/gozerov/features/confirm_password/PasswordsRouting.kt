package ru.gozerov.features.confirm_password

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configurePasswordsRouting() {
    routing {
        post("/password") {
            val requestBody = call.receive<ConfirmPasswordRequestBody>()
            val controller = PasswordsController(call)
            controller.confirmPassword(requestBody)
        }
    }
}
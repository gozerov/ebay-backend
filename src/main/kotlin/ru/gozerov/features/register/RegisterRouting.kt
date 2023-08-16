package ru.gozerov.features.register

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val requestBody = call.receive(RegisterRequest::class)
            val registerController = RegisterController(call)
            registerController.registerNewUser(requestBody)

        }
    }
}
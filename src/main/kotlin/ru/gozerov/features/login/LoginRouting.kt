package ru.gozerov.features.login

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*


fun Application.configureLoginRouting() {
    routing {
        post(path = "/login") {
            val requestBody = call.receive(LoginRequest::class)
            val loginController = LoginController(call)
            loginController.performLogin(requestBody)
        }
    }
}
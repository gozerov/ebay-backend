package ru.gozerov.features.register

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import ru.gozerov.features.email.EmailController

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val requestBody = call.receive(RegisterRequest::class)
            val emailController = EmailController(call)
                emailController.startRegistration(requestBody.email)
        }
        post("/setaccountdata") {
            val requestBody = call.receive(SetAccountDataRequest::class)
            val setAccountDataController = SetAccountDataController(call)
            setAccountDataController.setAccountData(requestBody)
        }
    }
}
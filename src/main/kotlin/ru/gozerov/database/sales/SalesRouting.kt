package ru.gozerov.database.sales

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSalesRouting() {
    routing {
        get("/sales") {
            val token = call.request.headers["Bearer-Authorization"]
            val controller = SalesController(call)
            controller.getSales(token)
        }
    }
}
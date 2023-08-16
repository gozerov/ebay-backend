package ru.gozerov.features.goods

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureGoodsRouting() {
    routing {
        get("/goods/get") {
            val token = call.request.headers["Bearer-Authorization"]
            val goodController = GoodsController(call)
            goodController.getGoods(token)
        }
        post("/goods/add") {
            val token = call.request.headers["Bearer-Authorization"]
            val requestBody = call.receive(AddGoodRequest::class)
            val goodController = GoodsController(call)
            goodController.addGood(requestBody, token)
        }
    }
}

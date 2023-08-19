package ru.gozerov.features.goods

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureGoodsRouting() {
    routing {
        get("/goods/get") {
            val token = call.request.headers["Bearer-Authorization"]
            val page = call.request.queryParameters["page"]?.toInt() ?: -1
            val goodId = call.request.queryParameters["id"]?.toInt() ?: -1
            val goodsController = GoodsController(call)
            if (page == -1) {
                if (goodId == -1)
                    goodsController.getGoods(token)
                else
                    goodsController.getGoodById(token, goodId)
            }
            else
                goodsController.getGoodsInParts(GetGoodsInPartsRequest(page), token)

        }
        post("/goods/add") {
            val token = call.request.headers["Bearer-Authorization"]
            val requestBody = call.receive(AddGoodRequest::class)
            val goodController = GoodsController(call)
            goodController.addGood(requestBody, token)
        }
        get("/goods/size") {
            val token = call.request.headers["Bearer-Authorization"]
            val goodsController = GoodsController(call)
            goodsController.getGoodsSize(token)
        }
        get {
            call.respond("Message")
        }
    }
}

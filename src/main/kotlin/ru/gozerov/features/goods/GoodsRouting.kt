package ru.gozerov.features.goods

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureGoodsRouting() {
    routing {
        get("/goods/get") {
            val token = call.request.headers["Bearer-Authorization"]
            val page = call.request.queryParameters["page"]?.toInt() ?: -1
            val goodId = call.request.queryParameters["id"]?.toInt() ?: -1
            val name = call.request.queryParameters["name"].toString()
            val category = call.request.queryParameters["category"].toString()
            val goodsController = GoodsController(call)
            if (page == -1) {
                if (goodId == -1) {
                    if (category == "null") {
                        if (name == "null")
                            goodsController.getGoods(token)
                        else
                            goodsController.searchProductsByName(token, name)
                    }
                    else
                        goodsController.getGoodsByCategory(token, category)
                } else
                    goodsController.getGoodById(token, goodId)
            } else
                goodsController.getGoodsInParts(GetGoodsInPartsRequest(page), token)

        }
        post("/goods/add") {
            val token = call.request.headers["Bearer-Authorization"]
            val requestBody = call.receive(AddGoodRequest::class)
            val goodsController = GoodsController(call)
            goodsController.addGood(requestBody, token)
        }
        post("/goods/pack") {
            val token = call.request.headers["Bearer-Authorization"]
            val goodsController = GoodsController(call)
            goodsController.getGoodsPack(token)
        }
        get("/category") {
            val token = call.request.headers["Bearer-Authorization"]
            val goodsController = GoodsController(call)
            goodsController.getCategories(token)
        }
        get("/goods/size") {
            val token = call.request.headers["Bearer-Authorization"]
            val goodsController = GoodsController(call)
            goodsController.getGoodsSize(token)
        }
    }
}

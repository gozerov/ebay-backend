package ru.gozerov.features.goods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.goods.GoodDTO
import ru.gozerov.database.goods.Goods
import ru.gozerov.database.tokens.Tokens

class GoodsController(
    private val call: ApplicationCall
) {

    suspend fun addGood(addGoodRequest: AddGoodRequest, token: String?) {
        if (Tokens.getTokens().firstOrNull { it.token == token } != null) {
            val isGoodDoesNotExist = Goods.getGoods()?.firstOrNull { it.name == addGoodRequest.name } == null
            if (isGoodDoesNotExist) {
                Goods.insert(
                    GoodDTO(
                        vendorCode = 0,
                        name = addGoodRequest.name,
                        description = addGoodRequest.description,
                        price = addGoodRequest.price,
                        images = addGoodRequest.images
                    )
                )
                call.respond(HttpStatusCode.Created, "Good has added")
            } else
                call.respond(HttpStatusCode.BadRequest, "Good is already exist")
        } else
            call.respond(HttpStatusCode.BadRequest, "Token isn`t valid")
    }

    suspend fun getGoods(token: String?) {
        if (Tokens.getTokens().firstOrNull { it.token == token } != null) {
            val goods = Goods.getGoods()
            println(goods.toString())
            if (goods != null && goods.isNotEmpty()) {
                call.respond(GetGoodsResponse(
                    goods = goods.map { good ->
                        GoodRemote(
                            vendorCode = good.vendorCode,
                            name = good.name,
                            description = good.description,
                            price = good.price,
                            images = good.images
                        )
                    }
                ))
            }
            else
                call.respond(HttpStatusCode.BadRequest, "No goods")
        } else
            call.respond(HttpStatusCode.BadRequest, "Token isn`t valid")
    }

}
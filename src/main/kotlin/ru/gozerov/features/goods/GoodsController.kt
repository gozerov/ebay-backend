package ru.gozerov.features.goods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.goods.GoodDTO
import ru.gozerov.database.goods.Goods
import ru.gozerov.database.tokens.checkToken

class GoodsController(
    private val call: ApplicationCall
) {

    suspend fun addGood(addGoodRequest: AddGoodRequest, token: String?) {
        call.checkToken(token) {
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
                call.respond(AddGoodResponse(isSuccessful = true))
            } else
                call.respond(HttpStatusCode.BadRequest, "Good is already exist")
        }
    }

    suspend fun getGoods(token: String?) {
        call.checkToken(token) {
            val goods = Goods.getGoods()
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
            } else
                call.respond(HttpStatusCode.BadRequest, "No goods")
        }
    }

    suspend fun getGoodsSize(token: String?) {
        call.checkToken(token) {
            call.respond(GetGoodsSizeResponse(Goods.getGoodsSize() ?: 0))
        }
    }

    suspend fun getGoodsInParts(getGoodsInPartsRequest: GetGoodsInPartsRequest, token: String?) {
        call.checkToken(token) {
            val goods = Goods.getPartOfGoods(getGoodsInPartsRequest.index)
            goods?.run {
                call.respond(GetGoodsInPartsResponse(this))
            } ?: call.respond(HttpStatusCode.BadRequest, "No goods")
        }
    }


    suspend fun getGoodById(token: String?, goodId: Int) {
        call.checkToken(token) {
            val good = Goods.getGoodById(goodId)
            good?.let {
                call.respond(GetGoodByIdResponse(it))
            } ?: call.respond(HttpStatusCode.BadRequest, "No such good")
        }
    }


}

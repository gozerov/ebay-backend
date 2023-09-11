package ru.gozerov.database.sales

import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.tokens.checkToken

class SalesController(
    private val call: ApplicationCall
) {

    suspend fun getSales(token: String?) {
       call.checkToken(token) {
            call.respond(GetSalesResponse(sales))
       }
    }

    companion object {
        private val sales = listOf(
            Sale(0, "New Sale 1", "New Sale 1 desc", "https://github.com/gozerov", "https://cms.mvideo.ru/magnoliaPublic/.imaging/webp/dam/228b3756-8f21-4e2c-82c3-7df846567b24"),
            Sale(1, "New Sale 2", "New Sale 2 desc", "https://github.com/gozerov", "https://cms.mvideo.ru/magnoliaPublic/.imaging/webp/dam/228b3756-8f21-4e2c-82c3-7df846567b24"),
            Sale(2, "New Sale 3", "New Sale 3 desc", "https://github.com/gozerov", "https://cms.mvideo.ru/magnoliaPublic/.imaging/webp/dam/228b3756-8f21-4e2c-82c3-7df846567b24"),
            Sale(3, "New Sale 4", "New Sale 4 desc", "https://github.com/gozerov", "https://cms.mvideo.ru/magnoliaPublic/.imaging/webp/dam/228b3756-8f21-4e2c-82c3-7df846567b24"),
            Sale(4, "New Sale 5", "New Sale 5 desc", "https://github.com/gozerov", "https://cms.mvideo.ru/magnoliaPublic/.imaging/webp/dam/228b3756-8f21-4e2c-82c3-7df846567b24"),
            Sale(5, "New Sale 6", "New Sale 6 desc", "https://github.com/gozerov", "https://cms.mvideo.ru/magnoliaPublic/.imaging/webp/dam/228b3756-8f21-4e2c-82c3-7df846567b24")
        )
    }

}
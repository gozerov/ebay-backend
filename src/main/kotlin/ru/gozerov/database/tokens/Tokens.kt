package ru.gozerov.database.tokens

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : Table(name = "tokens"){

    val id = Tokens.varchar("id", 50)
    val token = Tokens.varchar("token", 75)
    val userId = Tokens.integer("user_id")

    fun insert(tokenDTO: TokenDTO) = transaction {
        Tokens.insert {
            it[id] = tokenDTO.id
            it[userId] = tokenDTO.userId
            it[token] = tokenDTO.token
        }
    }

    fun getTokens(): List<TokenDTO> = transaction {
        Tokens.selectAll().map {
            TokenDTO(
                id = it[this@Tokens.id],
                userId = it[userId],
                token = it[token]
            )
        }
    }
}

suspend fun ApplicationCall.checkToken(token: String?, onValidToken: suspend (token: String) -> Unit) {
    val t = Tokens.getTokens().firstOrNull { it.token == token }
    if (t != null )  {
        onValidToken(t.token)
    } else
        respond(HttpStatusCode.BadRequest, "Token isn`t valid")
}
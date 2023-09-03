package ru.gozerov.database.tokens

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

fun checkToken(token: String?): Boolean = Tokens.getTokens().firstOrNull { it.token == token } != null
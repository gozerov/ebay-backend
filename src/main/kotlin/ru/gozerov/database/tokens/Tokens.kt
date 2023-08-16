package ru.gozerov.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : Table(name = "tokens"){

    val id = Tokens.varchar("id", 50)
    val login = Tokens.varchar("login", 50)
    val token = Tokens.varchar("token", 75)

    fun insert(tokenDTO: TokenDTO) = transaction {
        Tokens.insert {
            it[id] = tokenDTO.id
            it[login] = tokenDTO.login
            it[token] = tokenDTO.token
        }
    }

    fun getTokens(): List<TokenDTO> = transaction {
        Tokens.selectAll().map {
            TokenDTO(
                id = it[this@Tokens.id],
                login = it[login],
                token = it[token]
            )
        }
    }
}
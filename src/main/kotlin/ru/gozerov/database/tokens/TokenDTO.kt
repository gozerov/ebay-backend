package ru.gozerov.database.tokens

data class TokenDTO(
    val id: String,
    val token: String,
    val userId: Int,
)
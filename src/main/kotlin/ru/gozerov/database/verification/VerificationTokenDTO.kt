package ru.gozerov.database.verification

data class VerificationTokenDTO(
    val token: String,
    val code: Int,
    val email: String
)
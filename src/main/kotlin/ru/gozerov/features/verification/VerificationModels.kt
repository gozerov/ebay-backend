package ru.gozerov.features.verification

import kotlinx.serialization.Serializable

@Serializable
data class VerificationCodeRequest(
    val token: String,
    val code: Int
)

@Serializable
data class VerificationCodeResponse(
    val token: String
)
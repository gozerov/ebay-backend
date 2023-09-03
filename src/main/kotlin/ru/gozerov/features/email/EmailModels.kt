package ru.gozerov.features.email

import kotlinx.serialization.Serializable

@Serializable
data class ResetEmailRequest(
    val email: String
)

@Serializable
data class ResetEmailResponse(
    val token: String
)
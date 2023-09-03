package ru.gozerov.features.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String
)


/**
 * it returns verification token, not login*/
@Serializable
data class SetAccountDataRequest(
    val token: String,
    val username: String,
    val password: String
)

/**
 * it returns login token*/
@Serializable
data class SetAccountDataResponse(
    val token: String
)
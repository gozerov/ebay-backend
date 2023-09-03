package ru.gozerov.features.confirm_password

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmPasswordRequestBody(
    val token: String,
    val password: String,
    val confirmedPassword: String
)
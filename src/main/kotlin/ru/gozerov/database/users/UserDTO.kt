package ru.gozerov.database.users

data class UserDTO(
    val login: String,
    val password: String,
    val username: String,
    val email: String?
)
package ru.gozerov.database.users

data class UserDTO(
    val id: Int,
    val password: String,
    val username: String,
    val email: String?
)
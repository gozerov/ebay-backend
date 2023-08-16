package ru.gozerov.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.tokens.TokenDTO
import ru.gozerov.database.tokens.Tokens
import ru.gozerov.database.users.Users
import java.util.*

class LoginController(
    private val call: ApplicationCall
) {

    suspend fun performLogin(loginRequest: LoginRequest) {
        val user = Users.getUser(loginRequest.login)

        if (user != null){
            if (user.password == loginRequest.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        id = UUID.randomUUID().toString(),
                        login = loginRequest.login,
                        token = token
                    )
                )
                call.respond(LoginResponse(token))

            } else
                call.respond(HttpStatusCode.Unauthorized, "Wrong password")
        } else
            call.respond(HttpStatusCode.BadRequest, "User not found ")
    }

}
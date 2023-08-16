package ru.gozerov.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.tokens.TokenDTO
import ru.gozerov.database.tokens.Tokens
import ru.gozerov.database.users.UserDTO
import ru.gozerov.database.users.Users
import ru.gozerov.features.login.LoginResponse
import ru.gozerov.utils.isValidEmail
import java.util.*

class RegisterController(
    private val call: ApplicationCall
) {

    suspend fun registerNewUser(registerRequest: RegisterRequest) {
        val isUserExist = Users.getUser(registerRequest.login) != null
        if (!isUserExist) {
            if (registerRequest.email.isValidEmail()) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        id = UUID.randomUUID().toString(),
                        login = registerRequest.login,
                        token = token
                    )
                )
                Users.insert(
                    UserDTO(
                        login = registerRequest.login,
                        password = registerRequest.password,
                        username = "",
                        email = registerRequest.email
                    )
                )
                call.respond(LoginResponse(token))

            } else
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        } else
            call.respond(HttpStatusCode.BadRequest, "User has already been registered")
    }

}
package ru.gozerov.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.tokens.TokenDTO
import ru.gozerov.database.tokens.Tokens
import ru.gozerov.database.users.UserDTO
import ru.gozerov.database.users.Users
import ru.gozerov.database.verification.VerificationTokens
import java.util.*

class SetAccountDataController(
    private val call: ApplicationCall
) {

    suspend fun setAccountData(requestBody: SetAccountDataRequest) {
        if (VerificationTokens.checkIsTokenValid(requestBody.token)) {
            val email = VerificationTokens.getEmailByToken(requestBody.token)
            Users.insert(UserDTO(
                id = 0,
                password = requestBody.password,
                username = requestBody.username,
                email = email
            ))
            val token = UUID.randomUUID().toString()
            val userId = checkNotNull(Users.getUserByEmail(email)).id
            Tokens.insert(
                TokenDTO(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    token = token
                )
            )
            call.respond(SetAccountDataResponse(token))
        } else
            call.respond(HttpStatusCode.BadRequest)

    }

}
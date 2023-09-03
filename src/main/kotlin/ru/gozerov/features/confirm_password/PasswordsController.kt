package ru.gozerov.features.confirm_password

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.gozerov.database.users.Users
import ru.gozerov.database.verification.VerificationTokens

class PasswordsController(
    private val call: ApplicationCall
) {

    suspend fun confirmPassword(requestBody: ConfirmPasswordRequestBody) {
        if (VerificationTokens.checkIsTokenValid(requestBody.token)) {
            if (requestBody.password != requestBody.confirmedPassword)
                call.respond(HttpStatusCode.BadRequest)
            else {
                Users.updateUserPassword(token = requestBody.token, newPassword = requestBody.password)
                call.respond(HttpStatusCode.OK)
                VerificationTokens.deleteVerificationTokenByToken(requestBody.token)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

}